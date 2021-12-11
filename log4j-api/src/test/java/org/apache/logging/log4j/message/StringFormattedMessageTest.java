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
package org.apache.logging.log4j.message;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 *
 */
public class StringFormattedMessageTest {

    private static final int LOOP_CNT = 500;
    String[] array = new String[LOOP_CNT];

    @Test
    public void testNoArgs() {
        final String testMsg = "Test message %1s";
        StringFormattedMessage msg = new StringFormattedMessage(testMsg, (Object[]) null);
        String result = msg.getFormattedMessage();
        final String expected = "Test message null";
        assertEquals(expected, result);
        final Object[] array = null;
        msg = new StringFormattedMessage(testMsg, array, null);
        result = msg.getFormattedMessage();
        assertEquals(expected, result);
    }

    @Test
    public void testOneArg() {
        final String testMsg = "Test message %1s";
        final StringFormattedMessage msg = new StringFormattedMessage(testMsg, "Apache");
        final String result = msg.getFormattedMessage();
        final String expected = "Test message Apache";
        assertEquals(expected, result);
    }

    @Test
    public void testException() {
        final String testMsg = "Test message {0}";
        final MessageFormatMessage msg = new MessageFormatMessage(testMsg, "Apache", new NullPointerException("Null"));
        final String result = msg.getFormattedMessage();
        final String expected = "Test message Apache";
        assertEquals(expected, result);
        final Throwable t = msg.getThrowable();
        assertNotNull("No Throwable", t);
    }
}
