/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.graphdht.hashgraph;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

/**
 *
 * @author alex
 */
public class SimpleHashGraphDatabaseTest {

    public SimpleHashGraphDatabaseTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    public enum MyRelationshipType implements RelationshipType {
        KNOWS
    }

    /**
     * set of Tests fromNeo4J Getting Started Guide
     */
    @Test
    public void testGettingStartedGuide() throws Exception {
        System.out.println("Start Getting Started Guide!");
        GraphDatabaseService neo = new SimpleHashGraphDatabase("var/graphdb");

        //TODO transactions for a later time
        //Transaction tx = neo.beginTx();

        try {
            Node firstNode = neo.createNode();
            Node secondNode = neo.createNode();
            Relationship relationship = firstNode.createRelationshipTo(secondNode, MyRelationshipType.KNOWS);
            firstNode.setProperty("message", "Hello, ");
            secondNode.setProperty("message", "world!");
            relationship.setProperty("message", "brave Neo ");
            //tx.success();

            System.out.print(firstNode.getProperty("message"));
            System.out.print(relationship.getProperty("message"));
            System.out.print(secondNode.getProperty("message"));
        } catch (Exception e) {
            throw e;
            //fail("The Simple example failed with " + e.getStackTrace().toString() );
        } finally {
            //tx.finish();
        }
        neo.shutdown();

        System.out.println("\nEnd Getting Started Guide!");
    }
    /**
     * Test of createNode method, of class SimpleHashGraphDatabase.
     *
    @Test
    public void testCreateNode() {
    System.out.println("createNode");
    SimpleHashGraphDatabase instance = new SimpleHashGraphDatabase();
    Node expResult = null;
    Node result = instance.createNode();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
    }

    /**
     * Test of getNodeById method, of class SimpleHashGraphDatabase.
     *
    @Test
    public void testGetNodeById() {
    System.out.println("getNodeById");
    long id = 0L;
    SimpleHashGraphDatabase instance = new SimpleHashGraphDatabase();
    Node expResult = null;
    Node result = instance.getNodeById(id);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
    }

    /**
     * Test of getRelationshipById method, of class SimpleHashGraphDatabase.
     *
    @Test
    public void testGetRelationshipById() {
    System.out.println("getRelationshipById");
    long id = 0L;
    SimpleHashGraphDatabase instance = new SimpleHashGraphDatabase();
    Relationship expResult = null;
    Relationship result = instance.getRelationshipById(id);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
    }

    /**
     * Test of getReferenceNode method, of class SimpleHashGraphDatabase.
     *
    @Test
    public void testGetReferenceNode() {
    System.out.println("getReferenceNode");
    SimpleHashGraphDatabase instance = new SimpleHashGraphDatabase();
    Node expResult = null;
    Node result = instance.getReferenceNode();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
    }

    /**
     * Test of getAllNodes method, of class SimpleHashGraphDatabase.
     *
    @Test
    public void testGetAllNodes() {
    System.out.println("getAllNodes");
    SimpleHashGraphDatabase instance = new SimpleHashGraphDatabase();
    Iterable expResult = null;
    Iterable result = instance.getAllNodes();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
    }

    /**
     * Test of getRelationshipTypes method, of class SimpleHashGraphDatabase.
     *
    @Test
    public void testGetRelationshipTypes() {
    System.out.println("getRelationshipTypes");
    SimpleHashGraphDatabase instance = new SimpleHashGraphDatabase();
    Iterable expResult = null;
    Iterable result = instance.getRelationshipTypes();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
    }

    /**
     * Test of shutdown method, of class SimpleHashGraphDatabase.
     *
    @Test
    public void testShutdown() {
    System.out.println("shutdown");
    SimpleHashGraphDatabase instance = new SimpleHashGraphDatabase();
    instance.shutdown();
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
    }

    /**
     * Test of enableRemoteShell method, of class SimpleHashGraphDatabase.
     *
    @Test
    public void testEnableRemoteShell_0args() {
    System.out.println("enableRemoteShell");
    SimpleHashGraphDatabase instance = new SimpleHashGraphDatabase();
    boolean expResult = false;
    boolean result = instance.enableRemoteShell();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
    }

    /**
     * Test of enableRemoteShell method, of class SimpleHashGraphDatabase.
     *
    @Test
    public void testEnableRemoteShell_Map() {
    System.out.println("enableRemoteShell");
    Map<String, Serializable> initialProperties = null;
    SimpleHashGraphDatabase instance = new SimpleHashGraphDatabase();
    boolean expResult = false;
    boolean result = instance.enableRemoteShell(initialProperties);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
    }

    /**
     * Test of beginTx method, of class SimpleHashGraphDatabase.
     *
    @Test
    public void testBeginTx() {
    System.out.println("beginTx");
    SimpleHashGraphDatabase instance = new SimpleHashGraphDatabase();
    Transaction expResult = null;
    Transaction result = instance.beginTx();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
    }*/
}
