/**
 * Copyright (c) 2002-2013 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.test.ha;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.rules.ExternalResource;

import org.neo4j.graphdb.factory.HighlyAvailableGraphDatabaseFactory;
import org.neo4j.test.TargetDirectory;

import static org.neo4j.cluster.ClusterSettings.default_timeout;
import static org.neo4j.helpers.collection.MapUtil.stringMap;
import static org.neo4j.kernel.ha.HaSettings.tx_push_factor;
import static org.neo4j.test.ha.ClusterManager.masterAvailable;

public class ClusterRule extends ExternalResource
{
    private final File storeDirectory;
    private final ClusterManager.Provider provider;
    private ClusterManager clusterManager;

    public ClusterRule(Class<?> testClass, ClusterManager.Provider provider )
    {
        this.storeDirectory = TargetDirectory.forTest( testClass ).directory( "cluster", true );
        this.provider = provider;
    }

    public ClusterManager.ManagedCluster startCluster() throws Exception
    {
        return startCluster( new HighlyAvailableGraphDatabaseFactory() );
    }

    public ClusterManager.ManagedCluster startCluster( HighlyAvailableGraphDatabaseFactory databaseFactory )
            throws Exception
    {
        clusterManager = new ClusterManager( provider, storeDirectory, stringMap(
                default_timeout.name(), "1s", tx_push_factor.name(), "0" ),
                new HashMap<Integer, Map<String,String>>(), databaseFactory );
        try
        {
            clusterManager.start();
        }
        catch ( Throwable throwable )
        {
            throw new RuntimeException( throwable );
        }
        ClusterManager.ManagedCluster cluster = clusterManager.getDefaultCluster();
        cluster.await( masterAvailable() );
        return cluster;
    }

    @Override
    protected void after()
    {
        try
        {
            if ( clusterManager != null )
            {
                clusterManager.stop();
            }
        }
        catch ( Throwable throwable )
        {
            throwable.printStackTrace();
        }
    }
}
