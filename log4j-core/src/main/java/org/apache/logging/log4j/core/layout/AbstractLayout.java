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
package org.apache.logging.log4j.core.layout;

import java.io.Serializable;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.status.StatusLogger;

/**
 * Abstract base class for Layouts.
 * @param <T> The Class that the Layout will format the LogEvent into.
 */
public abstract class AbstractLayout<T extends Serializable> implements Layout<T> {
    /**
     * Allow subclasses access to the status logger without creating another instance.
     */
    protected static final Logger LOGGER = StatusLogger.getLogger();
    /**
     * The header to include when the stream is opened. May be null.
     */
    protected byte[] header;
    /**
     * The footer to add when the stream is closed. May be null.
     */
    protected byte[] footer;

    /**
     * Returns the header, if one is available.
     * @return A byte array containing the header.
     */
    @Override
    public byte[] getHeader() {
        return header;
    }

    /**
     * Set the header.
     * @param header The header.
     */
    public void setHeader(final byte[] header) {
        this.header = header;
    }

    /**
     * Returns the footer, if one is available.
     * @return A byte array containing the footer.
     */
    @Override
    public byte[] getFooter() {
        return footer;
    }

    /**
     * Set the footer.
     * @param footer The footer.
     */
    public void setFooter(final byte[] footer) {
        this.footer = footer;
    }
}
