/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.logging.slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.LoggerContext;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class SLF4JLoggerContext implements LoggerContext {
    private final ConcurrentMap<String, SLF4JLogger> loggers = new ConcurrentHashMap<String, SLF4JLogger>();

    @Override
    public Object getExternalContext() {
        return null;
    }

    @Override
    public Logger getLogger(final String name) {
        if (!loggers.containsKey(name)) {
            loggers.putIfAbsent(name, new SLF4JLogger(name, LoggerFactory.getLogger(name)));
        }
        return loggers.get(name);
    }

    @Override
    public Logger getLogger(final String name, final MessageFactory messageFactory) {
        if (!loggers.containsKey(name)) {
            loggers.putIfAbsent(name, new SLF4JLogger(name, messageFactory, LoggerFactory.getLogger(name)));
        }
        return loggers.get(name);
    }

    @Override
    public boolean hasLogger(final String name) {
        return loggers.containsKey(name);
    }
}
