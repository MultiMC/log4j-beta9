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

/**
 *
 */
public class StructuredDataMessageTest {

    @Test
    public void testMsg() {
        final String testMsg = "Test message {}";
        final StructuredDataMessage msg = new StructuredDataMessage("MsgId@12345", testMsg, "Alert");
        msg.put("message", testMsg);
        msg.put("project", "Log4j");
        msg.put("memo", "This is a very long test memo to prevent regression of LOG4J2-114");
        final String result = msg.getFormattedMessage();
        final String expected = "Alert [MsgId@12345 memo=\"This is a very long test memo to prevent regression of LOG4J2-114\" message=\"Test message {}\" project=\"Log4j\"] Test message {}";
        assertEquals(expected, result);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testMsgWithKeyTooLong() {
        final String testMsg = "Test message {}";
        final StructuredDataMessage msg = new StructuredDataMessage("MsgId@12345", testMsg, "Alert");
        msg.put("This is a very long key that will violate the key length validation", "Testing");
    }
}
