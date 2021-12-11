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
package org.apache.log4j;


import org.apache.log4j.spi.LoggerFactory;
import org.apache.logging.log4j.core.LoggerContext;

/**
 *
 */
public class Logger extends Category {

    private static final String FQCN = Logger.class.getName();

    protected Logger(final String name) {
        super((LoggerContext) PrivateManager.getContext(), name);
    }

    Logger(final LoggerContext context, final String name) {
        super(context, name);
    }

    public static Logger getLogger(final String name) {
        return (Logger) Category.getInstance((LoggerContext) PrivateManager.getContext(), name);
    }

    public static Logger getLogger(final Class<?> clazz) {
        return (Logger) Category.getInstance((LoggerContext) PrivateManager.getContext(), clazz);
    }

    public static Logger getRootLogger() {
        return (Logger) Category.getRoot((LoggerContext) PrivateManager.getContext());
    }

    public static Logger getLogger(final String name, final LoggerFactory factory) {
        return (Logger) Category.getInstance((LoggerContext) PrivateManager.getContext(), name, factory);
    }

    /**
     * Internal Log Manager.
     */
    private static class PrivateManager extends org.apache.logging.log4j.LogManager {
        private static final String FQCN = Logger.class.getName();

        public static org.apache.logging.log4j.spi.LoggerContext getContext() {
            return getContext(FQCN, false);
        }

        public static org.apache.logging.log4j.Logger getLogger(final String name) {
            return getLogger(FQCN, name);
        }
    }
}
