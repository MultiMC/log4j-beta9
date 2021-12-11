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
package org.apache.logging.log4j.core.appender.db.jpa.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.logging.log4j.ThreadContext;

/**
 * A JPA 2.1 attribute converter for 
 * {@link org.apache.logging.log4j.ThreadContext.ContextStack ThreadContext.ContextStack}s in
 * {@link org.apache.logging.log4j.core.LogEvent}s. This converter is only capable of converting to {@link String}s. The
 * {@link #convertToEntityAttribute(String)} method throws an {@link UnsupportedOperationException}. If you need to
 * support converting to an entity attribute, you should use the {@link ContextStackJsonAttributeConverter} for
 * conversion both ways.
 */
@Converter(autoApply = false)
public class ContextStackAttributeConverter implements AttributeConverter<ThreadContext.ContextStack, String> {
    @Override
    public String convertToDatabaseColumn(final ThreadContext.ContextStack contextStack) {
        if (contextStack == null) {
            return null;
        }

        final StringBuilder builder = new StringBuilder();
        for (final String value : contextStack.asList()) {
            if (builder.length() > 0) {
                builder.append('\n');
            }
            builder.append(value);
        }
        return builder.toString();
    }

    @Override
    public ThreadContext.ContextStack convertToEntityAttribute(final String s) {
        throw new UnsupportedOperationException("Log events can only be persisted, not extracted.");
    }
}
