///*
// * Copyright (c) 2008-2009 "Neo Technology,"
// *     Network Engine for Objects in Lund AB [http://neotechnology.com]
// *
// * This file is part of Neo4j.
// *
// * Neo4j is free software: you can redistribute it and/or modify
// * it under the terms of the GNU Affero General Public License as
// * published by the Free Software Foundation, either version 3 of the
// * License, or (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU Affero General Public License for more details.
// *
// * You should have received a copy of the GNU Affero General Public License
// * along with this program. If not, see <http://www.gnu.org/licenses/>.
// */
//package org.graphdht.hashgraph;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//import junit.framework.Assert;
//
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.neo4j.graphdb.Direction;
//import org.neo4j.graphdb.GraphDatabaseService;
//import org.neo4j.graphdb.Node;
//import org.neo4j.graphdb.Relationship;
//import org.neo4j.graphdb.RelationshipType;
//import org.neo4j.graphdb.ReturnableEvaluator;
//import org.neo4j.graphdb.StopEvaluator;
//import org.neo4j.graphdb.Transaction;
//import org.neo4j.graphdb.TraversalPosition;
//import org.neo4j.graphdb.Traverser;
//import org.neo4j.graphdb.Traverser.Order;
//import org.neo4j.remote.BasicGraphDatabaseServer;
//import org.neo4j.remote.RemoteIndexService;
//import org.neo4j.remote.RemoteGraphDatabase;
//import org.neo4j.remote.transports.LocalGraphDatabase;
//import org.neo4j.index.IndexService;
//import org.neo4j.index.lucene.LuceneIndexService;
//import org.neo4j.kernel.EmbeddedGraphDatabase;
//
//public class MatrixTest
//{
//
//    private GraphDatabaseService neo;
//
//    private IndexService index;
//
//    @Before
//    public void connect()
//    {
//        neo = new SimpleHashGraphDatabase( "simple" );
//    }
//
//    @After
//    public void disconnect()
//    {
//        neo.shutdown();
//    }
//
//    private static enum MatrixRelation implements RelationshipType
//    {
//        KNOWS, CODED_BY, LOVES
//    }
//
//    private static void defineMatrix( GraphDatabaseService neo)
//        throws Exception
//    {
//        // Define nodes
//        System.out.println("Before defining nodes");
//        Node mrAndersson, morpheus, trinity, cypher, agentSmith, theArchitect;
//        mrAndersson = neo.createNode();
//        morpheus = neo.createNode();
//        trinity = neo.createNode();
//        cypher = neo.createNode();
//        agentSmith = neo.createNode();
//        theArchitect = neo.createNode();
//        // Define relationships
//        //@SuppressWarnings( "unused" )
//        System.out.println("Before defining relationships");
//        Relationship aKm, aKt, mKt, mKc, cKs, sCa, tLa;
//        System.out.println("Before defining matrix");
//        aKm = mrAndersson.createRelationshipTo( morpheus, MatrixRelation.KNOWS );
//        System.out.println("After mrAndersson -> morpheus");
//        aKt = mrAndersson.createRelationshipTo( trinity, MatrixRelation.KNOWS );
//        System.out.println("After mrAndersson -> trinity");
//        mKt = morpheus.createRelationshipTo( trinity, MatrixRelation.KNOWS );
//        System.out.println("After morpheus -> trinity");
//        mKc = morpheus.createRelationshipTo( cypher, MatrixRelation.KNOWS );
//        System.out.println("After morpheus -> cypher");
//        cKs = cypher.createRelationshipTo( agentSmith, MatrixRelation.KNOWS );
//        System.out.println("After cypher -> agentSmith");
//        sCa = agentSmith.createRelationshipTo( theArchitect,
//            MatrixRelation.CODED_BY );
//        System.out.println("After agentSmith -> theArchitect");
//        tLa = trinity.createRelationshipTo( mrAndersson, MatrixRelation.LOVES );
//        // Define node properties
//        System.out.println("Before defining properties");
//        mrAndersson.setProperty( "name", "Thomas Andersson" );
//        morpheus.setProperty( "name", "Morpheus" );
//        trinity.setProperty( "name", "Trinity" );
//        cypher.setProperty( "name", "Cypher" );
//        agentSmith.setProperty( "name", "Agent Smith" );
//        theArchitect.setProperty( "name", "The Architect" );
//        // Define relationship properties
//    }
//
//    private static void verifyFriendsOf( Node thomas ) throws Exception
//    {
//        Traverser traverser = thomas.traverse( Order.BREADTH_FIRST,
//            StopEvaluator.END_OF_GRAPH, ReturnableEvaluator.ALL_BUT_START_NODE,
//            MatrixRelation.KNOWS, Direction.OUTGOING );
//        Set<String> actual = new HashSet<String>();
//        for ( Node friend : traverser )
//        {
//            Assert.assertTrue( "Same friend added twice.", actual
//                .add( ( String ) friend.getProperty( "name" ) ) );
//        }
//        Assert.assertEquals( "Thomas Anderssons friends are incorrect.",
//            new HashSet<String>( Arrays.asList( "Trinity", "Morpheus",
//                "Cypher", "Agent Smith" ) ), actual );
//    }
//
//    @SuppressWarnings( "serial" )
//    private static void verifyHackersInNetworkOf( Node thomas )
//        throws Exception
//    {
//        Traverser traverser = thomas.traverse( Order.BREADTH_FIRST,
//            StopEvaluator.END_OF_GRAPH, new ReturnableEvaluator()
//            {
//                public boolean isReturnableNode( TraversalPosition pos )
//                {
//                    return pos.notStartNode()
//                        && pos.lastRelationshipTraversed().isType(
//                            MatrixRelation.CODED_BY );
//                }
//            }, MatrixRelation.CODED_BY, Direction.OUTGOING,
//            MatrixRelation.KNOWS, Direction.OUTGOING );
//        Map<String, Integer> actual = new HashMap<String, Integer>();
//        for ( Node hacker : traverser )
//        {
//            Assert.assertNull( "Same hacker found twice.", actual.put(
//                ( String ) hacker.getProperty( "name" ), traverser
//                    .currentPosition().depth() ) );
//        }
//        Assert.assertEquals( "", new HashMap<String, Integer>()
//        {
//            {
//                put( "The Architect", 4 );
//            }
//        }, actual );
//    }
//
//    private enum PlaceboVerifiction
//    {
//        REQUIRE_PROPER
//        {
//            @Override
//            void of( String id, Transaction tx )
//            {
//                Assert.assertFalse( "Transaction \"" + id
//                    + "\" is a placebo transaction.", tx.toString().startsWith(
//                    "Placebo" ) );
//            }
//        },
//        REQUIRE_PLACEBO
//        {
//            @Override
//            void of( String id, Transaction tx )
//            {
//                Assert.assertTrue( "Transaction \"" + id
//                    + "\" is not a placebo transaction.", tx.toString()
//                    .startsWith( "Placebo" ) );
//            }
//        },
//        EITHER
//        {
//            @Override
//            void of( String id, Transaction tx )
//            {
//            }
//        };
//
//        abstract void of( String id, Transaction tx );
//
//    }
//
//    private static void verifyTransaction( String id, Transaction tx,
//        PlaceboVerifiction verifcation )
//    {
//        Assert.assertNotNull( "Transaction \"" + id + "\" is null.", tx );
//        verifcation.of( id, tx );
//    }
//
//    @Test
//    public void testTheMatrix() throws Exception
//    {
//        //Transaction tx = neo.beginTx();
//        //verifyTransaction( "nr 1", tx, PlaceboVerifiction.REQUIRE_PROPER );
//        try
//        {
//            System.out.println("Before defining matrix");
//            defineMatrix( neo );
//            System.out.println("After defining matrix");
//            //tx.success();
//        }
//        finally
//        {
//            //tx.finish();
//        }
//        //tx = neo.beginTx();
//        //verifyTransaction( "nr 2", tx, PlaceboVerifiction.REQUIRE_PROPER );
//        try
//        {
//            Node thomasAndersson = null;
//            //find Node wit "name" = "Thomas Andersson"
//            Iterable<Node> nodes =neo.getAllNodes();
//            for(Node node: nodes){
//                if(node.getProperty("name") == "Thomas Andersson"){
//                    thomasAndersson = node;
//                    break;
//                }
//            }
//
//            System.out.println("Before verifying friends");
//            verifyFriendsOf( thomasAndersson );
//            System.out.println("Before verifying hackers");
//            verifyHackersInNetworkOf( thomasAndersson );
//            System.out.println("After verifying hackers");
//            //tx.success();
//        }
//        finally
//        {
//            //tx.finish();
//        }
//    }
//}
