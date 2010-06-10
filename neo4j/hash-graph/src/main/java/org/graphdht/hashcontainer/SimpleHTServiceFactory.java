package org.graphdht.hashcontainer;

import org.graphdht.dht.HTService;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 8/Jun/2010
 * Time: 9:56:20
 * To change this template use File | Settings | File Templates.
 */
public class SimpleHTServiceFactory<T> implements HTServiceFactory {

    @Override
    public HTService createHTService() {
        return new SimpleHT<Long, T>();
    }
}
