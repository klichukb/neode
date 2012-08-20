package org.neo4j.neode;

import static org.neo4j.kernel.Traversal.expanderForTypes;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Expander;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.neode.properties.Property;

public class RelationshipSpecification
{
    private final RelationshipType relationshipType;
    private final List<Property> properties;

    RelationshipSpecification( RelationshipType relationshipType, List<Property> properties )
    {
        this.relationshipType = relationshipType;
        this.properties = properties;
    }

    Relationship createRelationship( Node startNode, Node endNode, GraphDatabaseService db, int iteration,
                                     Random random )
    {
        Relationship rel = startNode.createRelationshipTo( endNode, relationshipType );

        for ( Property property : properties )
        {
            property.setProperty( rel, db, relationshipType.name(), iteration, random );
        }

        return rel;
    }

    String label()
    {
        return relationshipType.name();
    }

    Expander expander( Direction direction )
    {
        return expanderForTypes( relationshipType, direction );
    }
}