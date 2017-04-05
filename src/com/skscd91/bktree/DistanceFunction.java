package com.skscd91.bktree;

/**
 * A binary function that computes the distance between two objects of the same type.
 *
 * @author Created by Sean Deneen on 4/4/17.
 */
public interface DistanceFunction<T> {
    int distance(T left, T right);
}
