/**
 * Copyright © 2017 Jeremy Custenborder (jcustenborder@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.eightyfourcodes.kafka.connect.rabbitmq;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.*;
import org.apache.kafka.connect.errors.DataException;
import org.apache.kafka.connect.transforms.Transformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class ExtractHeader<R extends ConnectRecord<R>> implements Transformation<R> {
    private static final Logger log = LoggerFactory.getLogger(ExtractHeader.class);
    ExtractHeaderConfig config;

    public static class Key<R extends ConnectRecord<R>> extends ExtractHeader<R> {

        @Override
        public R apply(R r) {
            final SchemaAndValue headerValue = extractHeader(r);

            return r.newRecord(
                    r.topic(),
                    r.kafkaPartition(),
                    headerValue.schema(),
                    headerValue.value(),
                    r.valueSchema(),
                    r.value(),
                    r.timestamp()
            );
        }
    }

    public static class Value<R extends ConnectRecord<R>> extends ExtractHeader<R> {
        @Override
        public R apply(R r) {
            final SchemaAndValue headerValue = extractHeader(r);

            return r.newRecord(
                    r.topic(),
                    r.kafkaPartition(),
                    r.keySchema(),
                    r.key(),
                    headerValue.schema(),
                    headerValue.value(),
                    r.timestamp()
            );
        }
    }


    @Override
    public ConfigDef config() {
        return ExtractHeaderConfig.config();
    }

    SchemaAndValue extractHeader(R record) {
        final Struct input = (Struct) record.value();

        if (null == record.value()) {
            throw new DataException("value() cannot be null.");
        }
        if (this.config.headerName != null && !this.config.headerName.isEmpty()) {
            return extractFromHeader(input);
        } else {
            return extractFromEnvelope(input);
        }
    }

    private SchemaAndValue extractFromHeader(Struct input) {
        final Struct basicProperties = input.getStruct(MessageConverter.FIELD_MESSAGE_BASICPROPERTIES);

        if (null == basicProperties) {
            throw new DataException(
                    String.format("Struct does not contain '%s'", MessageConverter.FIELD_MESSAGE_BASICPROPERTIES)
            );
        }

        final Map<String, Struct> headers = basicProperties.getMap(MessageConverter.FIELD_BASIC_PROPERTIES_HEADERS);

        if (null == headers) {
            throw new DataException(
                    String.format("Struct(%s) does not contain '%s'", MessageConverter.FIELD_MESSAGE_BASICPROPERTIES, MessageConverter.FIELD_BASIC_PROPERTIES_HEADERS)
            );
        }

        Struct header = headers.get(this.config.headerName);

        if (null == header) {
            throw new DataException(
                    String.format("Headers does does not contain '%s' header.", this.config.headerName)
            );
        }

        final String type = header.getString(MessageConverter.FIELD_BASIC_PROPERTIES_TYPE);
        final Field storageField = header.schema().field(type);
        final Object value = header.get(storageField);
        final Schema schema = SchemaBuilder.type(storageField.schema().type()).build();
        return new SchemaAndValue(schema, value);
    }

    private SchemaAndValue extractFromEnvelope(Struct input) {
        final Struct envelope = input.getStruct(MessageConverter.FIELD_MESSAGE_ENVELOPE);
        if (null == envelope) {
            throw new DataException(
                    String.format("Struct does not contain '%s'", MessageConverter.FIELD_MESSAGE_ENVELOPE)
            );
        }
        log.error("extractFromEnvelope: {}, {}", envelope, envelope.getString(this.config.envelopeName));
        final Schema schema = SchemaBuilder.string();
        return new SchemaAndValue(schema, envelope.getString(this.config.envelopeName));
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> settings) {
        this.config = new ExtractHeaderConfig(settings);
    }
}
