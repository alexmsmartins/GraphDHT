package org.neo4j.kernel.impl.nioneo.store;

import org.neo4j.kernel.impl.nioneo.store.AbstractRecord;
import org.neo4j.kernel.impl.nioneo.store.DynamicRecord;
import org.neo4j.kernel.impl.nioneo.store.PropertyType;
import org.neo4j.kernel.impl.nioneo.store.Record;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class is virtually a copy/paste of org.neo4j.kernel.impl.nioneo.store.PropertyRecord.
 * This was needed since PropertyRecord is not Serializable.
 * Date: 24/Mai/2010
 * Time: 23:34:33
 */
public class NSimplePropertyRecord extends AbstractRecord implements Serializable
{
    //TODO - remove unnecessary code
    //TODO - change the package when possible. AbstractRecord non public constructor makes it impossible now :(
    private PropertyType type;
    private int keyIndexId = Record.NO_NEXT_BLOCK.intValue();
    private long propBlock = Record.NO_NEXT_BLOCK.intValue();
    private int prevProp = Record.NO_PREVIOUS_PROPERTY.intValue();
    private int nextProp = Record.NO_NEXT_PROPERTY.intValue();
    private List<DynamicRecord> valueRecords = new ArrayList<DynamicRecord>();
    private boolean isLight = false;
    private int nodeRelId = -1;
    private boolean nodeIdSet = false;

    public NSimplePropertyRecord( int id )
    {
        super( id );
    }

    public void setType( PropertyType type )
    {
        this.type = type;
    }

    public void setNodeId( int nodeId )
    {
        nodeIdSet = true;
        nodeRelId = nodeId;
    }

    public void setRelId( int relId )
    {
        nodeIdSet = false;
        nodeRelId = relId;
    }

    public int getNodeId()
    {
        if ( nodeIdSet )
        {
            return nodeRelId;
        }
        return -1;
    }

    public int getRelId()
    {
        if ( !nodeIdSet )
        {
            return nodeRelId;
        }
        return -1;
    }

    void setIsLight( boolean status )
    {
        isLight = status;
    }

    public boolean isLight()
    {
        return isLight;
    }

    public Collection<DynamicRecord> getValueRecords()
    {
        assert !isLight;
        return valueRecords;
    }

    public void addValueRecord( DynamicRecord record )
    {
        assert !isLight;
        valueRecords.add( record );
    }

    public PropertyType getType()
    {
        return type;
    }

    public int getKeyIndexId()
    {
        return keyIndexId;
    }

    public void setKeyIndexId( int keyId )
    {
        this.keyIndexId = keyId;
    }

    public long getPropBlock()
    {
        return propBlock;
    }

    public void setPropBlock( long propBlock )
    {
        this.propBlock = propBlock;
    }

    public int getPrevProp()
    {
        return prevProp;
    }

    public void setPrevProp( int prevProp )
    {
        this.prevProp = prevProp;
    }

    public int getNextProp()
    {
        return nextProp;
    }

    public void setNextProp( int nextProp )
    {
        this.nextProp = nextProp;
    }

    @Override
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append( "PropertyRecord[" ).append( getId() ).append( "," ).append(
            inUse() ).append( "," ).append( type ).append( "," ).append(
            keyIndexId ).append( "," ).append( propBlock ).append( "," )
            .append( prevProp ).append( "," ).append( nextProp );
        buf.append( ", Value[" );
        for ( DynamicRecord record : valueRecords )
        {
            buf.append( record );
        }
        buf.append( "]]" );
        return buf.toString();
    }
}