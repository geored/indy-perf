/**
 * Copyright (C) 2011 Red Hat, Inc. (jdcasey@commonjava.org)
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

import org.commonjava.indy.autoprox.data.*
import org.commonjava.indy.pkg.maven.model.MavenPackageTypeDescriptor;

import java.net.MalformedURLException;
import org.commonjava.indy.model.core.*;

class RedHatRule extends AbstractAutoProxRule
{
    boolean matches( String packageType, String name ){
        MavenPackageTypeDescriptor.MAVEN_PKG_KEY.equals( packageType ) && name.startsWith( "RH-" )
    }

    RemoteRepository createRemoteRepository( StoreKey key )
        throws MalformedURLException
    {
        if ( named == "RH-all" || named == "RH-techpreview" ){
          new RemoteRepository( key.getPackageType(), name: key.getName(), url: "http://maven.repository.redhat.com/techpreview/all/" )
        }
        else if ( named == "RH-earlyaccess" ){
          new RemoteRepository( key.getPackageType(), name: key.getName(), url: "http://maven.repository.redhat.com/earlyaccess/all/" )
        }
    }

    Group createGroup( StoreKey key )
    {
        Group g = new Group( key.getPackageType(), key.getName() );
        g.addConstituent( new StoreKey( key.getPackageType(), StoreType.remote, key.getName() ) )
        
        g
    }
}
