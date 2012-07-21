package org.neo4j.datasetbuilder.numbergenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.neo4j.datasetbuilder.numbergenerators.NormalDistributionUniqueRandomNumberGenerator
        .normalDistribution;

import java.util.List;
import java.util.Random;

import org.junit.Test;

public class NormalDistributionUniqueRandomNumberGeneratorTest
{
    @Test
    public void shouldReturnListOfNumbers() throws Exception
    {
        // given
        NumberGenerator generator = normalDistribution();

        // when
        List<Integer> results = generator.generate( 5, 1, 5, new Random() );

        // then
        assertTrue( results.contains( 1 ) );
        assertTrue( results.contains( 2 ) );
        assertTrue( results.contains( 3 ) );
        assertTrue( results.contains( 4 ) );
        assertTrue( results.contains( 5 ) );
    }

    @Test
    public void shouldReturnSingleNumber() throws Exception
    {
        // given
        NumberGenerator generator = normalDistribution();

        // when
        int result = generator.generateSingle( 1, 1, new Random() );

        // then
        assertEquals( 1, result );
    }
}
