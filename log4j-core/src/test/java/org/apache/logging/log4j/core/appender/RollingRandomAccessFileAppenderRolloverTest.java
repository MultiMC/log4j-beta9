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
package org.apache.logging.log4j.core.appender;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.junit.BeforeClass;
import org.junit.Test;

public class RollingRandomAccessFileAppenderRolloverTest {

    @BeforeClass
    public static void beforeClass() {
        System.setProperty(ConfigurationFactory.CONFIGURATION_FILE_PROPERTY,
                "RollingRandomAccessFileAppenderTest.xml");
    }

    @Test
    public void testRollover() throws Exception {
        final File f = new File("target", "RollingRandomAccessFileAppenderTest.log");
        // System.out.println(f.getAbsolutePath());
        final File after1 = new File("target", "afterRollover-1.log");
        f.delete();
        after1.delete();

        final Logger log = LogManager.getLogger("com.foo.Bar");
        final String msg = "First a short message that does not trigger rollover";
        log.info(msg);
        Thread.sleep(50);

        BufferedReader reader = new BufferedReader(new FileReader(f));
        final String line1 = reader.readLine();
        assertTrue(line1.contains(msg));
        reader.close();

        assertFalse("afterRollover-1.log not created yet", after1.exists());

        String exceed = "Long message that exceeds rollover size... ";
        final char[] padding = new char[250];
        Arrays.fill(padding, 'X');
        exceed += new String(padding);
        log.warn(exceed);
        assertFalse("exceeded size but afterRollover-1.log not created yet", after1.exists());

        final String trigger = "This message triggers rollover.";
        log.warn(trigger);

        ((LifeCycle) LogManager.getContext()).stop(); // stop async thread

        assertTrue("afterRollover-1.log created", after1.exists());

        reader = new BufferedReader(new FileReader(f));
        final String new1 = reader.readLine();
        assertTrue("after rollover only new msg", new1.contains(trigger));
        assertNull("No more lines", reader.readLine());
        reader.close();
        f.delete();

        reader = new BufferedReader(new FileReader(after1));
        final String old1 = reader.readLine();
        assertTrue("renamed file line 1", old1.contains(msg));
        final String old2 = reader.readLine();
        assertTrue("renamed file line 2", old2.contains(exceed));
        final String line = reader.readLine();
        assertNull("No more lines", line);
        reader.close();
        after1.delete();
    }
}
