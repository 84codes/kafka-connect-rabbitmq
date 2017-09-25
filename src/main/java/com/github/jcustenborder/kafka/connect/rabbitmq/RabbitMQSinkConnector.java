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

package com.github.jcustenborder.kafka.connect.rabbitmq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;

import com.github.jcustenborder.kafka.connect.utils.VersionUtil;
import com.github.jcustenborder.kafka.connect.utils.config.Description;

@Description("Connector is used to read data from a Kafka topic and publish it on a RabbitMQ exchange and routing key pair.")
public class RabbitMQSinkConnector extends SinkConnector {
  Map<String, String> settings;
  RabbitMQSinkConnectorConfig config;
  


  @Override
  public ConfigDef config() {
    return RabbitMQSinkConnectorConfig.config();
  }

  @Override
  public void start(Map<String, String> settings) {
    this.config = new RabbitMQSinkConnectorConfig(settings);
    this.settings = settings;
    // TODO Auto-generated method stub      
  }

  @Override
  public void stop() {
    // TODO Auto-generated method stub      
  }

  @Override
  public Class<? extends Task> taskClass() {
    return RabbitMQSinkTask.class;
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
  public String version() {
    return VersionUtil.version(this.getClass());
  }

}
