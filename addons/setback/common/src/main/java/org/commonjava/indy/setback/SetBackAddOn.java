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
package org.commonjava.indy.setback;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Named;

import org.commonjava.indy.model.spi.IndyAddOnID;
import org.commonjava.indy.spi.IndyAddOn;

@ApplicationScoped
@Default
@Named( "setback" )
public class SetBackAddOn
    implements IndyAddOn
{

    private IndyAddOnID id;

    @Override
    public IndyAddOnID getId()
    {
        if ( id == null )
        {
            id = new IndyAddOnID().withName( "AutoProx" );
        }

        return id;
    }
}