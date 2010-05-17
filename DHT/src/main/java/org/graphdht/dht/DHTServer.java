/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.graphdht.dht;

import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 *
 *
 *
 * @param <K>
 * @param <V>
 * @author root
 */
public class DHTServer<K, V> implements DHTService<K, V> {

    private ExecutorService executor = Executors.newCachedThreadPool();

    public DHTServer() {
        try {
            ServerSocket s = new ServerSocket(5001);
//            addToNetwork();
            System.out.println("\n\nGraphDHT node UP!\n\n");
        } catch (Exception e) {
            System.out.println("Cannot set up GraphDHT node!!!");
            System.exit(0);
        }
    }

    @Override
    public V get(K key) {
        System.out.println("To implement!!!");
        return null;
    }

    @Override
    public V put(K key, V value) {
        System.out.println("To implement!!!");
        return null;
    }

    @Override
    public V remove(K key) {
        System.out.println("To implement!!!");
        return null;
    }

    @Override
    public void putAll(Map<K, V> m) {
        System.out.println("To implement!!!");
    }
}
