/**
 * Copyright © 2017 Kyumars Sheykh Esmaili (kyumarss@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.eightyfourcodes.kafka.connect.rabbitmq;

import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;

public class RabbitMQSinkConnectorConfig extends RabbitMQConnectorConfig {
  static final String KAFKA_TOPIC_TEMPLATE = "kafkaTopicTemplate";
  public static final String TOPIC_CONF = "topics";
  static final String TOPIC_DOC = "Kafka topic to read the messages from.";

  //TODO: add the support for queue destinations

  public static final String EXCHANGE_CONF = "rabbitmq.exchange";
  static final String EXCHANGE_DOC = "exchange to publish the messages on.";

  public static final String ROUTING_KEY_CONF = "rabbitmq.routing.key";
  static final String ROUTING_KEY_DOC = "routing key used for publishing the messages.";

  //TODO: include other config variables here

  public final String kafkaTopic;
  public final String exchange;
  public final String routingKey;

  public RabbitMQSinkConnectorConfig(Map<String, String> settings) {
    super(config(), settings);
    this.kafkaTopic = this.getString(TOPIC_CONF);
    this.exchange = this.getString(EXCHANGE_CONF);
    this.routingKey = this.getString(ROUTING_KEY_CONF);
  }

  public static ConfigDef config() {
    return RabbitMQConnectorConfig.config()
        .define(TOPIC_CONF, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, TOPIC_DOC)
        .define(EXCHANGE_CONF, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, EXCHANGE_DOC)
        .define(ROUTING_KEY_CONF, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, ROUTING_KEY_DOC);
  }

}
