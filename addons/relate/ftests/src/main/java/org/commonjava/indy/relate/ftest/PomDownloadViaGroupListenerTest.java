/**
 * Copyright (C) 2011-2018 Red Hat, Inc. (https://github.com/Commonjava/indy)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.commonjava.indy.relate.ftest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.commonjava.indy.ftest.core.AbstractIndyFunctionalTest;
import org.commonjava.indy.model.core.Group;
import org.commonjava.indy.model.core.RemoteRepository;
import org.commonjava.test.http.expect.ExpectationServer;
import org.junit.Rule;
import org.junit.Test;

import java.io.InputStream;

import static org.commonjava.indy.model.core.StoreType.group;
import static org.commonjava.indy.model.core.StoreType.remote;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by ruhan on 2/17/17.
 *
 * <b>GIVEN:</b>
 * <ul>
 *     <li>{@link Group} A contains {@link RemoteRepository} B which proxy an upstream server</li>
 *     <li>Path P points to a POM file in {@link RemoteRepository} B</li>
 *     <li>Path R points to the Rel file of the target POM</li>
 * </ul>
 *
 * <br/>
 * <b>WHEN:</b>
 * <ul>
 *     <li>Path R is requested from {@link Group} A</li>
 *     <li>Path P is requested from {@link Group} A</li>
 * </ul>
 *
 * <br/>
 * <b>THEN:</b>
 * <ul>
 *     <li>{@link Group} A returns notNull (exists) for Path R</li>
 *     <li>{@link Group} A returns notNull (exists) for Path P</li>
 * </ul>
 */
public class PomDownloadViaGroupListenerTest
        extends AbstractRelateFunctionalTest
{
    private static final String path = "org/foo/bar/1/bar-1.pom";

    private static final String pathRel = path + ".rel";

    private static final String content =
                    "<project><modelVersion>4.0.0</modelVersion><groupId>org.foo</groupId><artifactId>bar</artifactId><version>1</version></project>";

    @Rule
    public ExpectationServer server = new ExpectationServer();

    @Test
    public void run() throws Exception
    {
        final String repo1 = "repo1";
        final String group1 = "group1";

        server.expect( server.formatUrl( repo1, path ), 200, content );

        RemoteRepository remote1 = new RemoteRepository( repo1, server.formatUrl( repo1 ) );
        client.stores().create( remote1, "adding remote", RemoteRepository.class );

        client.stores().create( new Group( group1, remote1.getKey() ), "adding group", Group.class );

        InputStream is = client.content().get( group, group1, path );
        String s = IOUtils.toString( is );
        assertThat( s, equalTo( content ) );

        waitForEventPropagation();

        // Check .rel exist on group1
        boolean exists = client.content().exists( group, group1, pathRel, true );
        assertThat( exists, equalTo( true ) );

        // Check .rel exist on remote1
        exists = client.content().exists( remote, repo1, pathRel, true );
        assertThat( exists, equalTo( true ) );

        // Check .rel content is not empty
        InputStream ris = client.content().get( group, group1, pathRel );
        String rel = IOUtils.toString( ris );
        logger.debug( ">>> " + rel );
        assertThat( StringUtils.isNotEmpty( rel ), equalTo( true ) );
    }
}
