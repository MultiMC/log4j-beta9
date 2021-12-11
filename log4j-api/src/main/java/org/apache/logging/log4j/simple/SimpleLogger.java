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
package org.apache.logging.log4j.simple;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

/**
 *  This is the default logger that is used when no suitable logging implementation is available.
 */
public class SimpleLogger extends AbstractLogger {

    private static final char SPACE = ' ';

	/**
     * Used to format times.
     * <p>
     * Note that DateFormat is not Thread-safe.
     * </p>
     */
    private DateFormat dateFormatter;

    private Level level;

    private final boolean showDateTime;

    private final boolean showContextMap;

    private PrintStream stream;

    private final String logName;


    public SimpleLogger(final String name, final Level defaultLevel, final boolean showLogName,
                        final boolean showShortLogName, final boolean showDateTime, final boolean showContextMap,
                        final String dateTimeFormat, final MessageFactory messageFactory, final PropertiesUtil props,
                        final PrintStream stream) {
        super(name, messageFactory);
        final String lvl = props.getStringProperty(SimpleLoggerContext.SYSTEM_PREFIX + name + ".level");
        this.level = Level.toLevel(lvl, defaultLevel);
        if (showShortLogName) {
            final int index = name.lastIndexOf(".");
            if (index > 0 && index < name.length()) {
                this.logName = name.substring(index + 1);
            } else {
                this.logName = name;
            }
        } else if (showLogName) {
            this.logName = name;
        } else {
        	this.logName = null;
        }
        this.showDateTime = showDateTime;
        this.showContextMap = showContextMap;
        this.stream = stream;

        if (showDateTime) {
            try {
                this.dateFormatter = new SimpleDateFormat(dateTimeFormat);
            } catch (final IllegalArgumentException e) {
                // If the format pattern is invalid - use the default format
                this.dateFormatter = new SimpleDateFormat(SimpleLoggerContext.DEFAULT_DATE_TIME_FORMAT);
            }
        }
    }

    public void setStream(final PrintStream stream) {
        this.stream = stream;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(final Level level) {
        if (level != null) {
            this.level = level;
        }
    }

    @Override
    public void log(final Marker marker, final String fqcn, final Level level, final Message msg,
                    final Throwable throwable) {
        final StringBuilder sb = new StringBuilder();
        // Append date-time if so configured
        if (showDateTime) {
            final Date now = new Date();
            String dateText;
            synchronized (dateFormatter) {
                dateText = dateFormatter.format(now);
            }
            sb.append(dateText);
            sb.append(SPACE);
        }

        sb.append(level.toString());
        sb.append(SPACE);
        if (logName != null && logName.length() > 0) {
            sb.append(logName);
            sb.append(SPACE);
        }
        sb.append(msg.getFormattedMessage());
        if (showContextMap) {
            final Map<String, String> mdc = ThreadContext.getContext();
            if (mdc.size() > 0) {
                sb.append(SPACE);
                sb.append(mdc.toString());
                sb.append(SPACE);
            }
        }
        final Object[] params = msg.getParameters();
        Throwable t;
        if (throwable == null && params != null && params[params.length - 1] instanceof Throwable) {
            t = (Throwable) params[params.length - 1];
        } else {
            t = throwable;
        }
        if (t != null) {
            sb.append(SPACE);
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            t.printStackTrace(new PrintStream(baos));
            sb.append(baos.toString());
        }
        stream.println(sb.toString());
    }

    @Override
    protected boolean isEnabled(final Level level, final Marker marker, final String msg) {
        return this.level.intLevel() >= level.intLevel();
    }


    @Override
    protected boolean isEnabled(final Level level, final Marker marker, final String msg, final Throwable t) {
        return this.level.intLevel() >= level.intLevel();
    }

    @Override
    protected boolean isEnabled(final Level level, final Marker marker, final String msg, final Object... p1) {
        return this.level.intLevel() >= level.intLevel();
    }

    @Override
    protected boolean isEnabled(final Level level, final Marker marker, final Object msg, final Throwable t) {
        return this.level.intLevel() >= level.intLevel();
    }

    @Override
    protected boolean isEnabled(final Level level, final Marker marker, final Message msg, final Throwable t) {
        return this.level.intLevel() >= level.intLevel();
    }

}
