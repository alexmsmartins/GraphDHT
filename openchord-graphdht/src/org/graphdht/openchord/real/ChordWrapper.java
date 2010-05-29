/**********************************************************
 * Doctoral Program in Science and Information Technology
 * Department of Informatics Engineering
 * University of Coimbra
 **********************************************************
 * Large Scale Concurrent Systems
 *
 * Pedro Alexandre Mesquita Santos Martins - pamm@dei.uc.pt
 * Nuno Manuel dos Santos Antunes - nmsa@dei.uc.pt
 **********************************************************/
package org.graphdht.openchord.real;

import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.Key;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author nuno
 */
public class ChordWrapper implements Chord {
    private final Chord chord;

    public ChordWrapper(Chord chord) {
        this.chord = chord;
    }

    @Override
    public URL getURL() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setURL(URL url) throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ID getID() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setID(ID id) throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void create() throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void create(URL url) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void create(URL url, ID id) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void join(URL url) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void join(URL url, URL url1) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void join(URL url, ID id, URL url1) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void leave() throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void insert(Key key, Serializable srlzbl) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<Serializable> retrieve(Key key) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(Key key, Serializable srlzbl) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
