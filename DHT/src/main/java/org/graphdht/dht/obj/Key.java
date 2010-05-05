/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.graphdht.dht.obj;

import java.io.Serializable;

/**
 *
 * @author root
 */
public class Key implements Serializable {

    private String key;

    public Key(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
