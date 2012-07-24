package org.neo4j.neode.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.neo4j.neode.DomainEntity;
import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.Dataset;
import org.neo4j.neode.commands.interfaces.AddTo;
import org.neo4j.neode.logging.Log;
import org.neo4j.graphdb.GraphDatabaseService;

public class DomainEntityBatchCommandBuilder implements AddTo
{
    private static final int DEFAULT_BATCH_SIZE = 50000;

    public static DomainEntityBatchCommandBuilder createEntities( DomainEntity domainEntity )
    {
        return new DomainEntityBatchCommandBuilder( domainEntity );
    }

    private DomainEntity domainEntity;
    private int numberOfIterations = 0;

    private DomainEntityBatchCommandBuilder( DomainEntity domainEntity )
    {
        this.domainEntity = domainEntity;
    }

    public DomainEntityBatchCommandBuilder quantity( int value )
    {
        numberOfIterations = value;
        return this;
    }

    public DomainEntityInfo addTo( Dataset dataset, int batchSize )
    {
        DomainEntityBatchCommand command =
                new DomainEntityBatchCommand( domainEntity, numberOfIterations, batchSize );
        return dataset.execute( command );
    }

    public DomainEntityInfo addTo( Dataset dataset )
    {
        return addTo( dataset, DEFAULT_BATCH_SIZE );
    }

    private static class DomainEntityBatchCommand implements BatchCommand
    {
        private final DomainEntity domainEntity;
        private final int numberOfIterations;
        private final int batchSize;
        private final List<Long> nodeIds;

        public DomainEntityBatchCommand( DomainEntity domainEntity, int numberOfIterations,
                                         int batchSize )
        {
            this.domainEntity = domainEntity;
            this.numberOfIterations = numberOfIterations;
            this.batchSize = batchSize;
            nodeIds = new ArrayList<Long>();
        }

        @Override
        public int numberOfIterations()
        {
            return numberOfIterations;
        }

        @Override
        public int batchSize()
        {
            return batchSize;
        }

        @Override
        public void execute( GraphDatabaseService db, int index, Random random )
        {
            nodeIds.add( domainEntity.build( db, index ) );
        }

        @Override
        public String description()
        {
            return "Creating '" + shortDescription() + "' nodes.";
        }

        @Override
        public String shortDescription()
        {
            return domainEntity.entityName();
        }

        @Override
        public void onBegin( Log log )
        {
            // Do nothing
        }

        @Override
        public void onEnd( Log log )
        {
            // Do nothing
        }

        @Override
        public DomainEntityInfo results()
        {
            return new DomainEntityInfo( domainEntity.entityName(), nodeIds );
        }
    }
}
