# BKTree

A [BK-tree](https://en.wikipedia.org/wiki/BK-tree) library in Java that
implements [Set](https://docs.oracle.com/javase/8/docs/api/java/util/Set.html).

A BK-tree is a search tree that is optimized for nearest neighbor searches
with O(log N) time complexity through a
[metric space](https://en.wikipedia.org/wiki/Metric_space). The edges of
the tree are indexed by the distance between the parent and the child,
based on a distance function, also known as a metric.

Also included are implementations of the
[Levenshtein distance function](https://en.wikipedia.org/wiki/Levenshtein_distance)
and [Hamming distance function](https://en.wikipedia.org/wiki/Hamming_distance).

Inspired by the post by [Michele Lacchia](http://signal-to-noise.xyz/post/bk-tree/).
Licensed under the MIT License.

