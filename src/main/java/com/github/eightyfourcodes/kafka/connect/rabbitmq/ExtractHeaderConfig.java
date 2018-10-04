/**
 * Copyright © 2017 Jeremy Custenborder (jcustenborder@gmail.com)
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

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ExtractHeaderConfig extends AbstractConfig {

  public final String headerName;
  public final String envelopeName;

  public ExtractHeaderConfig(Map<?, ?> originals) {
    super(config(), originals);
    this.headerName = getString(HEADER_NAME_CONF);
    this.envelopeName = getString(ENVELOPE_NAME_CONF);
  }

  public static final String HEADER_NAME_CONF = "header.name";
  public static final String HEADER_NAME_DOC = "Header name.";

  public static final String ENVELOPE_NAME_CONF = "envelope.name";
  public static final String ENVELOPE_NAME_DOC = "Envelope name.";

  public static ConfigDef config() {
    List<String> dependents = Collections.emptyList();
    ConfigDef.ConfigKey header = new ConfigDef.ConfigKey(HEADER_NAME_CONF, ConfigDef.Type.STRING, "", null, ConfigDef.Importance.HIGH, HEADER_NAME_DOC, "", -1, ConfigDef.Width.NONE, HEADER_NAME_CONF, dependents, null, true);
    ConfigDef.ConfigKey envelope = new ConfigDef.ConfigKey(ENVELOPE_NAME_CONF, ConfigDef.Type.STRING, "", null, ConfigDef.Importance.HIGH, ENVELOPE_NAME_DOC, "", -1, ConfigDef.Width.NONE, ENVELOPE_NAME_CONF, dependents, null, true);
    return new ConfigDef().define(header).define(envelope);
  }

}
