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
package org.apache.logging.log4j.core.appender.db;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;

/**
 * An abstract Appender for writing events to a database of some type, be it relational or NoSQL. All database appenders
 * should inherit from this base appender. Three implementations are currently provided:
 * {@link org.apache.logging.log4j.core.appender.db.jdbc JDBC}, {@link org.apache.logging.log4j.core.appender.db.jpa
 * JPA}, and {@link org.apache.logging.log4j.core.appender.db.nosql NoSQL}.
 *
 * @param <T> Specifies which type of {@link AbstractDatabaseManager} this Appender requires.
 */
public abstract class AbstractDatabaseAppender<T extends AbstractDatabaseManager> extends AbstractAppender {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    private T manager;

    /**
     * Instantiates the base appender.
     *
     * @param name The appender name.
     * @param filter The filter, if any, to use.
     * @param ignoreExceptions If {@code true} exceptions encountered when appending events are logged; otherwise
     *                         they are propagated to the caller.
     * @param manager The matching {@link AbstractDatabaseManager} implementation.
     */
    protected AbstractDatabaseAppender(final String name, final Filter filter, final boolean ignoreExceptions,
                                       final T manager) {
        super(name, filter, null, ignoreExceptions);
        this.manager = manager;
    }

    /**
     * This always returns {@code null}, as database appenders do not use a single layout. The JPA and NoSQL appenders
     * do not use a layout at all. The JDBC appender has a layout-per-column pattern.
     *
     * @return {@code null}.
     */
    @Override
    public final Layout<LogEvent> getLayout() {
        return null;
    }

    /**
     * Returns the underlying manager in use within this appender.
     *
     * @return the manager.
     */
    public final T getManager() {
        return this.manager;
    }

    @Override
    public final void start() {
        if (this.getManager() == null) {
            LOGGER.error("No AbstractDatabaseManager set for the appender named [{}].", this.getName());
        }
        super.start();
        if (this.getManager() != null) {
            this.getManager().connect();
        }
    }

    @Override
    public final void stop() {
        super.stop();
        if (this.getManager() != null) {
            this.getManager().release();
        }
    }

    @Override
    public final void append(final LogEvent event) {
        this.readLock.lock();
        try {
            this.getManager().write(event);
        } catch (final LoggingException e) {
            LOGGER.error("Unable to write to database [{}] for appender [{}].", this.getManager().getName(),
                    this.getName(), e);
            throw e;
        } catch (final Exception e) {
            LOGGER.error("Unable to write to database [{}] for appender [{}].", this.getManager().getName(),
                    this.getName(), e);
            throw new AppenderLoggingException("Unable to write to database in appender: " + e.getMessage(), e);
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * Replaces the underlying manager in use within this appender. This can be useful for manually changing the way log
     * events are written to the database without losing buffered or in-progress events. The existing manager is
     * released only after the new manager has been installed. This method is thread-safe.
     *
     * @param manager The new manager to install.
     */
    protected final void replaceManager(final T manager) {
        this.writeLock.lock();
        try {
            final T old = this.getManager();
            if (!manager.isConnected()) {
                manager.connect();
            }
            this.manager = manager;
            old.release();
        } finally {
            this.writeLock.unlock();
        }
    }
}
