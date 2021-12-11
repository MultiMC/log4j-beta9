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
package org.apache.logging.log4j.core.async;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.logging.log4j.core.LoggerContext;
import org.junit.Test;

public class AsyncLoggerContextSelectorTest {

    @Test
    public void testContextReturnsAsyncLoggerContext() {
        final AsyncLoggerContextSelector selector = new AsyncLoggerContextSelector();
        final LoggerContext context = selector.getContext(null, null, false);

        assertTrue(context instanceof AsyncLoggerContext);
    }

    @Test
    public void testContext2ReturnsAsyncLoggerContext() {
        final AsyncLoggerContextSelector selector = new AsyncLoggerContextSelector();
        final LoggerContext context = selector.getContext(null, null, false, null);

        assertTrue(context instanceof AsyncLoggerContext);
    }

    @Test
    public void testLoggerContextsReturnsAsyncLoggerContext() {
        final AsyncLoggerContextSelector selector = new AsyncLoggerContextSelector();
        final List<LoggerContext> list = selector.getLoggerContexts();

        assertEquals(1, list.size());
        assertTrue(list.get(0) instanceof AsyncLoggerContext);
    }

    @Test
    public void testContextNameIsAsyncLoggerContext() {
        final AsyncLoggerContextSelector selector = new AsyncLoggerContextSelector();
        final LoggerContext context = selector.getContext(null, null, false);

        assertEquals("AsyncLoggerContext", context.getName());
    }

}
