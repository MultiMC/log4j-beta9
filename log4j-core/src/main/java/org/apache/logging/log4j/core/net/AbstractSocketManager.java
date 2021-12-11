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
package org.apache.logging.log4j.core.net;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.OutputStreamManager;

import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for managing sockets.
 */
public abstract class AbstractSocketManager extends OutputStreamManager {

    /**
     * The internet address of the host.
     */
    protected final InetAddress address;
    /**
     * The name of the host.
     */
    protected final String host;
    /**
     * The port on the host.
     */
    protected final int port;

    /**
     * The Constructor.
     * @param name The unique name of this connection.
     * @param os The OutputStream to manage.
     * @param addr The internet address.
     * @param host The target host name.
     * @param port The target port number.
     */
    public AbstractSocketManager(final String name, final OutputStream os, final InetAddress addr, final String host,
                                 final int port, final Layout<? extends Serializable> layout) {
        super(os, name, layout);
        this.address = addr;
        this.host = host;
        this.port = port;
    }

    /**
     * AbstractSocketManager's content format is specified by:<p/>
     * Key: "port" Value: provided "port" param<p/>
     * Key: "address" Value: provided "address" param
     * @return Map of content format keys supporting AbstractSocketManager
     */
    @Override
    public Map<String, String> getContentFormat()
    {
        final Map<String, String> result = new HashMap<String, String>(super.getContentFormat());
        result.put("port", Integer.toString(port));
        result.put("address", address.getHostAddress());

        return result;
    }
}
