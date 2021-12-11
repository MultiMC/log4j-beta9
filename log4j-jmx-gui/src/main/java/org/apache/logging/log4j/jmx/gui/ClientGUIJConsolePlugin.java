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
package org.apache.logging.log4j.jmx.gui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.SwingWorker;

import com.sun.tools.jconsole.JConsolePlugin;

/**
 * Adapts the {@code ClientGUI} to the {@code JConsolePlugin} API.
 */
public class ClientGUIJConsolePlugin extends JConsolePlugin {

    @Override
    public Map<String, JPanel> getTabs() {
        try {
            final Client client = new Client(getContext().getMBeanServerConnection());
            final ClientGUI gui = new ClientGUI(client);
            final Map<String, JPanel> result = new HashMap<String, JPanel>();
            result.put("Log4j2", gui);
            return result;
        } catch (final Throwable ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public SwingWorker<?, ?> newSwingWorker() {
        return null;
    }
}
