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

import static org.junit.Assert.assertEquals;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.SimpleMessage;
import org.junit.Test;

/**
 *
 */
public class LevelPatternConverterTest {

    private void testLevelLength(int length, String debug, String warn) {
        final Message msg = new SimpleMessage("Hello");
        LogEvent event = new Log4jLogEvent("MyLogger", null, null, Level.DEBUG, msg, null);
        final StringBuilder sb = new StringBuilder();
        LevelPatternConverter converter = LevelPatternConverter.newInstance(null);
        converter.format(event, sb);
        assertEquals(Level.DEBUG.toString(), sb.toString());
        final String[] opts = new String[] { "length=" + length };
        converter = LevelPatternConverter.newInstance(opts);
        sb.setLength(0);
        converter.format(event, sb);
        assertEquals(debug, sb.toString());
        event = new Log4jLogEvent("MyLogger", null, null, Level.WARN, msg, null);
        sb.setLength(0);
        converter.format(event, sb);
        assertEquals(warn, sb.toString());
    }

    @Test
    public void testLevelLength1() {
        testLevelLength(1, "D", "W");
    }

    @Test
    public void testLevelLength10() {
        testLevelLength(10, "DEBUG", "WARN");
    }

    @Test
    public void testLevelLength2() {
        testLevelLength(2, "DE", "WA");
    }

    @Test
    public void testLevelLength5() {
        testLevelLength(5, "DEBUG", "WARN");
    }

    @Test
    public void testLevelLowerCase() {
        final Message msg = new SimpleMessage("Hello");
        LogEvent event = new Log4jLogEvent("MyLogger", null, null, Level.DEBUG, msg, null);
        final StringBuilder sb = new StringBuilder();
        LevelPatternConverter converter = LevelPatternConverter.newInstance(null);
        converter.format(event, sb);
        assertEquals(Level.DEBUG.toString(), sb.toString());
        final String[] opts = new String[] { "lowerCase=true" };
        converter = LevelPatternConverter.newInstance(opts);
        sb.setLength(0);
        converter.format(event, sb);
        assertEquals("debug", sb.toString());
        event = new Log4jLogEvent("MyLogger", null, null, Level.WARN, msg, null);
        sb.setLength(0);
        converter.format(event, sb);
        assertEquals("warn", sb.toString());
    }

    @Test
    public void testLevelMap() {
        final Message msg = new SimpleMessage("Hello");
        LogEvent event = new Log4jLogEvent("MyLogger", null, null, Level.DEBUG, msg, null);
        final StringBuilder sb = new StringBuilder();
        LevelPatternConverter converter = LevelPatternConverter.newInstance(null);
        converter.format(event, sb);
        assertEquals(Level.DEBUG.toString(), sb.toString());
        final String[] opts = new String[] { "WARN=Warning, DEBUG=Debug, ERROR=Error, TRACE=Trace, INFO=Info" };
        converter = LevelPatternConverter.newInstance(opts);
        sb.setLength(0);
        converter.format(event, sb);
        assertEquals("Debug", sb.toString());
        event = new Log4jLogEvent("MyLogger", null, null, Level.WARN, msg, null);
        sb.setLength(0);
        converter.format(event, sb);
        assertEquals("Warning", sb.toString());
    }

    @Test
    public void testLevelMapWithLength() {
        final Message msg = new SimpleMessage("Hello");
        LogEvent event = new Log4jLogEvent("MyLogger", null, null, Level.DEBUG, msg, null);
        final StringBuilder sb = new StringBuilder();
        LevelPatternConverter converter = LevelPatternConverter.newInstance(null);
        converter.format(event, sb);
        assertEquals(Level.DEBUG.toString(), sb.toString());
        final String[] opts = new String[] { "WARN=Warning, length=2" };
        converter = LevelPatternConverter.newInstance(opts);
        sb.setLength(0);
        converter.format(event, sb);
        assertEquals("DE", sb.toString());
        event = new Log4jLogEvent("MyLogger", null, null, Level.WARN, msg, null);
        sb.setLength(0);
        converter.format(event, sb);
        assertEquals("Warning", sb.toString());
    }

    @Test
    public void testLevelMapWithLengthAndLowerCase() {
        final Message msg = new SimpleMessage("Hello");
        LogEvent event = new Log4jLogEvent("MyLogger", null, null, Level.DEBUG, msg, null);
        final StringBuilder sb = new StringBuilder();
        LevelPatternConverter converter = LevelPatternConverter.newInstance(null);
        converter.format(event, sb);
        assertEquals(Level.DEBUG.toString(), sb.toString());
        final String[] opts = new String[] { "WARN=Warning, length=2, lowerCase=true" };
        converter = LevelPatternConverter.newInstance(opts);
        sb.setLength(0);
        converter.format(event, sb);
        assertEquals("de", sb.toString());
        event = new Log4jLogEvent("MyLogger", null, null, Level.WARN, msg, null);
        sb.setLength(0);
        converter.format(event, sb);
        assertEquals("Warning", sb.toString());
    }

}
