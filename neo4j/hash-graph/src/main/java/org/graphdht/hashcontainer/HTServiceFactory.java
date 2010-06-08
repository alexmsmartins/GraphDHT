package org.graphdht.hashcontainer;

import org.graphdht.dht.HTService;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 8/Jun/2010
 * Time: 9:49:49
 * To change this template use File | Settings | File Templates.
 */
public interface HTServiceFactory {
    HTService createHTService();
}
