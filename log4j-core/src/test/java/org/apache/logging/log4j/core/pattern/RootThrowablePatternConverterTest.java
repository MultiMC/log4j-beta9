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
package org.apache.logging.log4j.core.pattern;

import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.SimpleMessage;
import org.junit.Test;

/**
 *
 */
public class RootThrowablePatternConverterTest {

    @Test
    public void testFull1() {
        final RootThrowablePatternConverter converter = RootThrowablePatternConverter.newInstance(null);
        final Throwable cause = new NullPointerException("null pointer");
        final Throwable parent = new IllegalArgumentException("IllegalArgument", cause);
        final LogEvent event = new Log4jLogEvent("testLogger", null, this.getClass().getName(), Level.DEBUG,
                new SimpleMessage("test exception"), parent);
        final StringBuilder sb = new StringBuilder();
        converter.format(event, sb);
        final String result = sb.toString();
        // System.out.print(result);
        assertTrue("Missing Exception",
                result.contains("Wrapped by: java.lang.IllegalArgumentException: IllegalArgument"));
        assertTrue("Incorrect start of msg", result.startsWith("java.lang.NullPointerException: null pointer"));
    }

    /**
     * Sanity check for testFull1() above, makes sure that the way testFull1 is written matches actually throwing
     * exceptions.
     */
    @Test
    public void testFull2() {
        final RootThrowablePatternConverter converter = RootThrowablePatternConverter.newInstance(null);
        Throwable parent;
        try {
            try {
                throw new NullPointerException("null pointer");
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("IllegalArgument", e);
            }
        } catch (IllegalArgumentException e) {
            parent = e;
        }
        final LogEvent event = new Log4jLogEvent("testLogger", null, this.getClass().getName(), Level.DEBUG,
                new SimpleMessage("test exception"), parent);
        final StringBuilder sb = new StringBuilder();
        converter.format(event, sb);
        final String result = sb.toString();
        // System.out.print(result);
        assertTrue("Missing Exception",
                result.contains("Wrapped by: java.lang.IllegalArgumentException: IllegalArgument"));
        assertTrue("Incorrect start of msg", result.startsWith("java.lang.NullPointerException: null pointer"));
    }
}
