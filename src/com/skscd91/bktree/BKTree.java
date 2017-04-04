package com.skscd91.bktree;

import java.util.AbstractSet;
import java.util.Iterator;

/**
 * A tree used to find objects of type T within a certain distance.
 * Commonly used for approximate string checking.
 *
 * Created by Sean Deneen on 4/4/17.
 * Inspired by post by Michele Lacchia. http://signal-to-noise.xyz/post/bk-tree/
 */
public class BKTree<T> extends AbstractSet<T> {

    private final DistanceFunction<T> distanceFunction;

    public BKTree(DistanceFunction<T> distanceFunction) {
        this.distanceFunction = distanceFunction;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
