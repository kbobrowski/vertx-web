/*
 * Copyright 2019 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.ext.web.handler.graphql.impl;

import io.vertx.core.Future;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.graphql.ApolloWSMessage;
import io.vertx.ext.web.handler.graphql.ApolloWSMessageType;

/**
 * @author Thomas Segismont
 */
public class ApolloWSMessageImpl implements ApolloWSMessage {

  private final ServerWebSocket serverWebSocket;
  private final ApolloWSMessageType type;
  private final JsonObject content;
  private final Object connectionParams;
  private Future<Object> future;

  public ApolloWSMessageImpl(ServerWebSocket serverWebSocket, ApolloWSMessageType type, JsonObject content, Object connectionParams) {
    this.serverWebSocket = serverWebSocket;
    this.type = type;
    this.content = content;
    this.connectionParams = connectionParams;
  }

  @Override
  public ServerWebSocket serverWebSocket() {
    return serverWebSocket;
  }

  @Override
  public ApolloWSMessageType type() {
    return type;
  }

  @Override
  public JsonObject content() {
    return content;
  }

  @Override
  public <T> T connectionParams() {
    return (T) connectionParams;
  }

  @Override
  public <T> void setHandshake(Future<T> future) {
    if (type != ApolloWSMessageType.CONNECTION_INIT) {
      throw new IllegalStateException("setHandshake method can only be used on a message of type CONNECTION_INIT");
    }
    synchronized (this) {
      this.future = (Future<Object>) future;
    }
  }

  @Override
  public synchronized <T> Future<T> future() {
    return (Future<T>) future;
  }
}
