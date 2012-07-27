/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.commands;

import java.util.Random;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;

public enum Uniqueness implements RelationshipUniqueness
{
    SINGLE_DIRECTION
            {
                public Relationship createRelationship( GraphDatabaseService db, Node firstNode, Node secondNode,
                                                        RelationshipInfo relationshipInfo, Random random )
                {
                    PathFinder<Path> pathPathFinder =
                            GraphAlgoFactory.shortestPath( relationshipInfo.expander(), 1 );

                    if ( pathPathFinder.findSinglePath( firstNode, secondNode ) != null )
                    {
                        return null;
                    }

                    return relationshipInfo.createRelationship( firstNode, secondNode, db, random );
                }
            },
    BOTH_DIRECTIONS
            {
                public Relationship createRelationship( GraphDatabaseService db, Node firstNode, Node secondNode,
                                                        RelationshipInfo relationshipInfo, Random random )
                {
                    PathFinder<Path> pathPathFinder = GraphAlgoFactory.shortestPath(
                            relationshipInfo.expanderIgnoreDirection(), 1 );

                    if ( pathPathFinder.findSinglePath( firstNode, secondNode ) != null )
                    {
                        return null;
                    }

                    return relationshipInfo.createRelationship( firstNode, secondNode, db, random );
                }
            },
    ALLOW_MULTIPLE
            {
                public Relationship createRelationship( GraphDatabaseService db, Node firstNode, Node secondNode,
                                                        RelationshipInfo relationshipInfo, Random random )
                {
                    return relationshipInfo.createRelationship( firstNode, secondNode, db, random );
                }
            };

    public abstract Relationship createRelationship( GraphDatabaseService db, Node firstNode,
                                                     Node secondNode,
                                                     RelationshipInfo relationshipInfo, Random random );


}
