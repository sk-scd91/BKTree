package com.skscd91.bktree;

/**
 * Various distance functions for use with the BKTree.
 *
 * Created by Sean Deneen on 4/4/17.
 */
public final class DistanceFunctions {

    private DistanceFunctions() {}

    public static DistanceFunction<CharSequence> hammingDistance() {
        return hammingDistance(true);
    }

    public static DistanceFunction<CharSequence> hammingDistance(boolean isCaseSensitive) {
        return new HammingDistanceFunction(isCaseSensitive);
    }

    public static DistanceFunction<CharSequence> levenshteinDistance() {
        return levenshteinDistance(true);
    }

    public static DistanceFunction<CharSequence> levenshteinDistance(boolean isCaseSensitive) {
        return new LevenshteinDistanceFunction(isCaseSensitive);
    }

    private static boolean charEquals(char a, char b, boolean isCaseSensitive) {
        if (isCaseSensitive)
            return a == b;

        return Character.toUpperCase(a) == Character.toUpperCase(b);
    }

    // Word distance by letter changes only.
    private static class HammingDistanceFunction implements DistanceFunction<CharSequence> {

        private final boolean isCaseSensitive;

        public HammingDistanceFunction(boolean isCaseSensitive) {
            this.isCaseSensitive = isCaseSensitive;
        }

        @Override
        public int distance(CharSequence left, CharSequence right) {
            int length = Math.min(left.length(), right.length());
            int wordDistance = Math.abs(left.length() - right.length()); // Treat size difference as blank characters.

            for (int i = 0; i < length; i++) {
                if (!charEquals(left.charAt(i), right.charAt(i), isCaseSensitive))
                    wordDistance++;
            }

            return wordDistance;
        }
    }

    // Word distance by letter changes, insertions, and deletions
    private static class LevenshteinDistanceFunction implements DistanceFunction<CharSequence> {

        private final boolean isCaseSensitive;

        public LevenshteinDistanceFunction(boolean isCaseSensitive) {
            this.isCaseSensitive = isCaseSensitive;
        }

        @Override
        public int distance(CharSequence left, CharSequence right) {
            int leftLength = left.length(), rightLength = right.length();

            // special cases.
            if (leftLength == 0)
                return rightLength;
            if (rightLength == 0)
                return leftLength;

            // Use the iterative matrix method.
            int[] currentRow = new int[rightLength + 1];
            int[] nextRow    = new int[rightLength + 1];

            // Fill first row with all edit counts.
            for (int i = 0; i < rightLength; i++)
                currentRow[i] = i;

            for (int i = 1; i <= leftLength; i++) {
                nextRow[0] = i;

                for(int j = 1; j <= rightLength; j++) {
                    int changeDistance = currentRow[j - 1]; // Distance without insertions or deletions.
                    if (!charEquals(left.charAt(i - 1), right.charAt(j - 1), isCaseSensitive))
                            changeDistance++;
                    nextRow[j] = Math.min(Math.min(nextRow[j - 1], currentRow[j]) + 1, changeDistance);
                }

                // Swap rows, use last row for next row.
                int[] t = currentRow;
                currentRow = nextRow;
                nextRow = t;
            }

            return currentRow[rightLength];
        }

    }
}
