package com.skscd91.bktree;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Created by Sean Deneen on 4/5/17.
 */
class BKTreeTest {

    private static final String[] testStrings = new String[]{"some", "soft", "same", "mole", "soda", "salmon"};
    private static final DistanceFunction<CharSequence> distFunc = DistanceFunctions.levenshteinDistance();

    @Test
    void add() {
        BKTree<CharSequence> tree = new BKTree<>(distFunc);
        assertTrue(tree.add("some"), "first add");
        assertFalse(tree.add("some"), "add equivalent");
        assertTrue(tree.add("somb"), "add new object");
        assertFalse(tree.add("somb"), "add equivalent again");
        assertThrows(NullPointerException.class, () -> tree.add(null)); // Must throw NullpointerException when null.
    }

    @Test
    void search() {
        BKTree<CharSequence> tree = new BKTree<>(distFunc);
        for (String s : testStrings)
            tree.add(s);
        List<BKTree.SearchResult<CharSequence>> results = tree.search("sort", 2);
        assertEquals(3, results.size(), "result size");
        assertEquals(1, results.get(0).getDistance(), "sorted by distance");
        assertEquals("soft", results.get(0).getItem());

        assertTrue(results.get(1).getDistance() == results.get(2).getDistance(),
                "last 2 are same distance");
        assertEquals(2, results.get(1).getDistance(), "last 2 have distance of 2");
        if ("some".equals(results.get(1).getItem())) {
            assertEquals("soda", results.get(2).getItem());
        } else {
            assertEquals("soda", results.get(1).getItem());
            assertEquals("some", results.get(2).getItem());
        }
        assertTrue(tree.search(null, 2).isEmpty(), "no search when t is null");
    }

    @Test
    void contains() {
        BKTree<CharSequence> tree = new BKTree<>(distFunc);
        assertFalse(tree.contains("some"), "false if empty");
        for (String s : testStrings)
            tree.add(s);
        for (String s : testStrings) {
            assertTrue(tree.contains(s), s + " is in tree");
            assertTrue(tree.contains(new StringBuffer(s)), s + " equivalent CharSequence is in tree");
        }
        assertFalse(tree.contains("sort"), "sort not in tree");
        assertFalse(tree.contains(null), "null not in tree");
        assertFalse(tree.contains(new Object()), "non CharSequence not in tree");
    }

    @Test
    void remove() {
        BKTree<CharSequence> tree = new BKTree<>(distFunc);
        assertFalse(tree.remove("some"), "false if empty");
        for (String s : testStrings)
            tree.add(s);
        assertFalse(tree.remove("sort"), "sort not in tree");
        assertFalse(tree.remove(null), "null not in tree");
        assertFalse(tree.remove(new Object()), "non CharSequence not in tree");

        assertTrue(tree.remove("same"), "node without children was removed");
        assertFalse(tree.remove("same"), "last node not in tree (1)");
        assertTrue(tree.remove("soft"), "node with children removed");
        assertFalse(tree.remove("soft"), "last node not in tree (2)");
        assertEquals(2, tree.search("sort", 2).size(), "confirm node was removed");
        assertTrue(tree.remove("some"), "root node removed");
        assertFalse(tree.remove("some"), "last node not in tree (3)");

        assertTrue(tree.remove(new StringBuffer("salmon")), "equivalent CharSequence was removed from tree");
    }

    @Test
    void iterator() {
        String[] bfsTestStrings = new String[] {"some", "same", "soft", "salmon", "soda", "mole"};
        BKTree<CharSequence> tree = new BKTree<>(distFunc);
        assertFalse(tree.iterator().hasNext(), "false if empty");
        for (String s : testStrings)
            tree.add(s);
        int i = 0;
        for (CharSequence s : tree) {
            assertTrue(i < bfsTestStrings.length);
            assertEquals(bfsTestStrings[i], s, bfsTestStrings[i] + " is equal");
            i++;
        }
    }

    @Test
    void size() {
        BKTree<CharSequence> tree = new BKTree<>(distFunc);
        for (String s : testStrings)
            tree.add(s);
        assertEquals(testStrings.length, tree.size(), "initial size");
        tree.add("sort");
        assertEquals(testStrings.length + 1, tree.size(), "one added");
        tree.remove("sort");
        assertEquals(testStrings.length, tree.size(), "one removed");
    }

}