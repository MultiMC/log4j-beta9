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

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.test.appender.ListAppender;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class LoggingTest {

    private static final String CONFIG = "log4j2-config.xml";
    private static Configuration config;
    private static ListAppender app;
    private static LoggerContext ctx;

    @BeforeClass
    public static void setupClass() {
        ctx = Configurator.initialize("Test1", CONFIG);
    }

    @AfterClass
    public static void cleanupClass() {
        ctx.stop();
        StatusLogger.getLogger().reset();
    }

    @Before
    public void before() {
        config = ctx.getConfiguration();
    }

    @Test
    public void testParent() {
        final Logger a = Logger.getLogger("org.apache.test.logging.Test");
        final Category parent = a.getParent();
        assertNotNull("No parent Logger", parent);
        assertTrue("Incorrect parent logger", parent.getName().equals("org.apache.test.logging"));
    }

}
