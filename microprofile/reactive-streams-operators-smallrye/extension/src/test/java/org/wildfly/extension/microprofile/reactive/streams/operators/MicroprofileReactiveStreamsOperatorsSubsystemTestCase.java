/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2020, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.wildfly.extension.microprofile.reactive.streams.operators;

import static org.wildfly.extension.microprofile.reactive.streams.operators.MicroProfileReactiveStreamsOperatorsExtension.WELD_CAPABILITY_NAME;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Locale;

import org.jboss.as.subsystem.test.AbstractSubsystemBaseTest;
import org.jboss.as.subsystem.test.AdditionalInitialization;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author <a href="mailto:kabir.khan@jboss.com">Kabir Khan</a>
 */
@RunWith(Parameterized.class)
public class MicroprofileReactiveStreamsOperatorsSubsystemTestCase extends AbstractSubsystemBaseTest {
    @Parameters
    public static Iterable<MicroProfileReactiveStreamsOperatorsSubsystemSchema> parameters() {
        return EnumSet.allOf(MicroProfileReactiveStreamsOperatorsSubsystemSchema.class);
    }

    private final MicroProfileReactiveStreamsOperatorsSubsystemSchema schema;

    public MicroprofileReactiveStreamsOperatorsSubsystemTestCase(MicroProfileReactiveStreamsOperatorsSubsystemSchema schema) {
        super(MicroProfileReactiveStreamsOperatorsExtension.SUBSYSTEM_NAME, new MicroProfileReactiveStreamsOperatorsExtension());
        this.schema = schema;
    }

    @Override
    protected String getSubsystemXml() throws IOException {
        //test configuration put in standalone.xml
        return readResource(String.format(Locale.ROOT, "reactive-streams-operators-%d.%d.xml", this.schema.getVersion().major(), this.schema.getVersion().minor()));
    }

    @Override
    protected String getSubsystemXsdPath() throws Exception {
        return String.format(Locale.ROOT, "schema/wildfly-microprofile-reactive-streams-operators-smallrye_%d_%d.xsd", this.schema.getVersion().major(), this.schema.getVersion().minor());
    }

    @Override
    protected AdditionalInitialization createAdditionalInitialization() {
        return AdditionalInitialization.withCapabilities(WELD_CAPABILITY_NAME);
    }
}
