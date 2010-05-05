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
public class Value implements Serializable {

    private String value;

    public Value(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
