package com.skscd91.bktree;

import java.util.*;

/**
 * A tree used to find objects of type T within a certain distance.
 * Commonly used for approximate string checking.
 *
 * Created by Sean Deneen on 4/4/17.
 * Inspired by post by Michele Lacchia. http://signal-to-noise.xyz/post/bk-tree/
 */
public class BKTree<T> extends AbstractSet<T> {

    private final DistanceFunction<T> distanceFunction;
    private Node<T> rootNode;
    private int length;

    public BKTree(DistanceFunction<T> distanceFunction) {
        if (distanceFunction == null)
            throw new NullPointerException("distanceFunction cannot be null.");

        this.distanceFunction = distanceFunction;
        length = 0;
    }

    public List<Result<T>> search(T t, int radius) {
        ArrayList<Result<T>> results = new ArrayList<>();
        ArrayDeque<Node<T>> nextNodes = new ArrayDeque<>();
        if (rootNode != null)
            nextNodes.add(rootNode);

        while(!nextNodes.isEmpty()) {
            Node<T> nextNode = nextNodes.poll();
            int distance = distanceFunction.distance(nextNode.item, t);
            if (distance <= radius)
                results.add(new Result<>(distance, nextNode.item));
            int lowBound = Math.max(0, distance - radius), highBound = distance + radius;
            for (Integer i = lowBound; i <= highBound; i++) {
                if (nextNode.children.containsKey(i))
                    nextNodes.add(nextNode.children.get(i));
            }
        }

        results.trimToSize();
        Collections.sort(results);
        return Collections.unmodifiableList(results);
    }

    @Override
    public boolean add(T t) {
        if (t == null)
            throw new NullPointerException();

        if (rootNode == null) {
            rootNode = new Node<>(t);
            length = 1;
            return true;
        }

        Node<T> parentNode = rootNode;
        Integer distance;
        while ((distance = distanceFunction.distance(parentNode.item, t)) != 0) {
            Node<T> childNode = parentNode.children.get(distance);
            if (childNode == null) {
                parentNode.children.put(distance, new Node<>(t));
                length++;
                return true;
            }
            parentNode = childNode;
        }

        return false;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null || rootNode == null)
            return false;
        try {
            @SuppressWarnings("unchecked")
            List<Result<T>> searchList = search((T) o, 0);
            return searchList.size() > 0;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o == null || rootNode == null)
            return false;
        if (rootNode.item.equals(o)) {
            length--;
            rootNode = replaceNode(rootNode);
            return true;
        }
        try {
            @SuppressWarnings("unchecked")
            T t = (T)o;

            for (Node<T> parentNode = rootNode, childNode; parentNode != null; parentNode = childNode) {
                Integer distance = distanceFunction.distance(parentNode.item, t);
                childNode = parentNode.children.get(distance);
                if (childNode != null && childNode.item.equals(o)) {
                    length--;
                    childNode = replaceNode(parentNode.children.remove(distance));
                    if (childNode != null)
                        parentNode.children.put(distance, childNode);
                    return true;
                }
            }

            return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

    private Node<T> replaceNode(Node<T> oldNode) {
        Iterator<Map.Entry<Integer, Node<T>>> entries = oldNode.children.entrySet().iterator();
        if (!entries.hasNext())
            return null;
        Node<T> newNode = entries.next().getValue();
        while(entries.hasNext()) {
            Node<T> valNode = entries.next().getValue();

            for (Node<T> parentNode = newNode, childNode; ; parentNode = childNode) {
                Integer distance = distanceFunction.distance(parentNode.item, valNode.item);
                childNode = parentNode.children.get(distance);
                if (childNode == null) {
                    parentNode.children.put(distance, valNode);
                    break;
                }
            }
        }
        return newNode;
    }

    @Override
    public Iterator<T> iterator() {
        if (length == 0)
            return Collections.emptyIterator();
        ArrayList<T> iterable = new ArrayList<>(length);
        rootNode.collect(iterable);
        return iterable.iterator();
    }

    @Override
    public int size() {
        return length;
    }

    public static class Result<T> implements Comparable<Result<T>> {
        private final int distance;
        private final T item;

        public Result(int distance, T item) {
            this.distance = distance;
            this.item = item;
        }

        public int getDistance() {
            return distance;
        }

        public T getItem() {
            return item;
        }

        @Override
        public int compareTo(Result<T> o) {
            if (o == null) // null first.
                return 1;
            return Integer.compare(distance, o.distance);
        }
    }

    private static class Node<T> {
        public final T item;
        public final Map<Integer, Node<T>> children;

        public Node(T item) {
            this.item = item;
            this.children = new TreeMap<>();
        }

        public void collect(Collection<T> collection) {
            collection.add(item);
            for (Map.Entry<Integer, Node<T>> child : children.entrySet())
                child.getValue().collect(collection);
        }
    }
}
