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
package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.message.Message;

/**
 * This filter returns the onMatch result if the marker in the LogEvent is the same as or has the
 * configured marker as a parent.
 *
 */
@Plugin(name = "MarkerFilter", category = "Core", elementType = "filter", printObject = true)
public final class MarkerFilter extends AbstractFilter {

    private final String name;

    private MarkerFilter(final String name, final Result onMatch, final Result onMismatch) {
        super(onMatch, onMismatch);
        this.name = name;
    }

    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg,
                         final Object... params) {
        return filter(marker);
    }

    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final Object msg,
                         final Throwable t) {
        return filter(marker);
    }

    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final Message msg,
                         final Throwable t) {
        return filter(marker);
    }

    @Override
    public Result filter(final LogEvent event) {
        return filter(event.getMarker());
    }

    private Result filter(final Marker marker) {
        return marker != null && marker.isInstanceOf(name) ? onMatch : onMismatch;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Create the MarkerFilter.
     * @param marker The Marker name to match.
     * @param match The action to take if a match occurs.
     * @param mismatch The action to take if no match occurs.
     * @return A MarkerFilter.
     */
    @PluginFactory
    public static MarkerFilter createFilter(
            @PluginAttribute("marker") final String marker,
            @PluginAttribute("onMatch") final String match,
            @PluginAttribute("onMismatch") final String mismatch) {

        if (marker == null) {
            LOGGER.error("A marker must be provided for MarkerFilter");
            return null;
        }
        final Result onMatch = Result.toResult(match);
        final Result onMismatch = Result.toResult(mismatch);
        return new MarkerFilter(marker, onMatch, onMismatch);
    }

}
