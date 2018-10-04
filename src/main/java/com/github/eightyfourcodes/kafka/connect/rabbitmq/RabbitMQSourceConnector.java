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
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RabbitMQSourceConnector extends SourceConnector {
    Map<String, String> settings;
    RabbitMQSourceConnectorConfig config;

    @Override
    public String version() {
        return "1.0.0";
    }

    @Override
    public void start(Map<String, String> settings) {
        this.config = new RabbitMQSourceConnectorConfig(settings);
        this.settings = settings;
    }

    @Override
    public Class<? extends Task> taskClass() {
        return RabbitMQSourceTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        List<Map<String, String>> taskConfigs = new ArrayList<>();
        for (int i = 0; i < maxTasks; i++) {
            taskConfigs.add(this.settings);
        }
        return taskConfigs;
    }

    @Override
    public void stop() {

    }

    @Override
    public ConfigDef config() {
        return RabbitMQSourceConnectorConfig.config();
    }
}
