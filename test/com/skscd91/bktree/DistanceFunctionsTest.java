package com.skscd91.bktree;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Created by Sean Deneen on 4/5/17.
 */
class DistanceFunctionsTest {
    @Test
    void hammingDistanceEqualsCaseSensitive() {
        DistanceFunction<CharSequence> hammingDistance = DistanceFunctions.hammingDistance();
        assertEquals(0, hammingDistance.distance("test", "test"));
        assertEquals(4, hammingDistance.distance("test", "TEST"));
    }

    @Test
    void hammingDistanceEqualsCaseInsensitive() {
        DistanceFunction<CharSequence> hammingDistance = DistanceFunctions.hammingDistance(false);
        assertEquals(0, hammingDistance.distance("test", "test"), "same case");
        assertEquals(0, hammingDistance.distance("test", "TEST"), "different case");
    }

    @Test
    void hammingDistance() {
        DistanceFunction<CharSequence> hammingDistance = DistanceFunctions.hammingDistance();
        assertEquals(1, hammingDistance.distance("same","some"), "'same' and 'some'");
        assertThrows(IllegalArgumentException.class, () -> hammingDistance.distance("same", "sam"));
        assertEquals(4, hammingDistance.distance("same", "abcd"), "'same' and 'abcd'");
    }

    @Test
    void levenshteinDistanceEqualsCaseSensitive() {
        DistanceFunction<CharSequence> levenshteinDistance = DistanceFunctions.levenshteinDistance();
        assertEquals(0, levenshteinDistance.distance("test", "test"));
        assertEquals(4, levenshteinDistance.distance("test", "TEST"));
    }

    @Test
    void levenshteinDistanceEqualsCaseInensitive() {
        DistanceFunction<CharSequence> levenshteinDistance = DistanceFunctions.levenshteinDistance(false);
        assertEquals(0, levenshteinDistance.distance("test", "test"), "same case");
        assertEquals(0, levenshteinDistance.distance("test", "TEST"), "different case");
    }

    @Test
    void levenshteinDistanceEmpty() {
        DistanceFunction<CharSequence> levenshteinDistance = DistanceFunctions.levenshteinDistance();
        assertEquals(4, levenshteinDistance.distance("", "test"), "left empty");
        assertEquals(4, levenshteinDistance.distance("test", ""), "right empty");
    }

    @Test
    void levenshteinDistance() {
        DistanceFunction<CharSequence> levenshteinDistance = DistanceFunctions.levenshteinDistance();
        assertEquals(1, levenshteinDistance.distance("same", "some"),"'same' and 'some'");
        assertEquals(1, levenshteinDistance.distance("same", "sam"), "'same' and 'sam");
        assertEquals(1, levenshteinDistance.distance("sam", "same"), "'sam' and 'same");
        assertEquals(1, levenshteinDistance.distance("same", "ame"), "'same' and 'ame");
        assertEquals(1, levenshteinDistance.distance("ame", "same"), "'ame' and 'same");

        assertEquals(2, levenshteinDistance.distance("some", "soft"),"'some' and 'soft'");
        assertEquals(2, levenshteinDistance.distance("some", "soda"),"'some' and 'soda'");
        assertEquals(2, levenshteinDistance.distance("some", "mole"),"'some' and 'mole'");
        assertEquals(2, levenshteinDistance.distance("soft", "soda"),"'soft' and 'soda'");
        assertEquals(3, levenshteinDistance.distance("soft", "mole"),"'soft' and 'mole'");

        assertEquals(4, levenshteinDistance.distance("some", "salmon"),"'same' and 'salmon'");
    }

}