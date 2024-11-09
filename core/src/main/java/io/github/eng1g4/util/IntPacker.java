package io.github.eng1g4.util;

import static java.lang.Math.*;

/** Helper class to pack x,y coordinates into a double */
public class IntPacker {

    /** Squash 2 integers into a double
      * for example (3, 12) will be squashed to 3.121
      * Notice the trailing 1 to avoid ambiguity in the case of (3, 120), etc.
     */
    public static double pack(int x, int y) {
        double adjustedY = (double) y * 10 + 1;
        // appending 1 to the y
        // avoids ambiguity encoding trailing zeroes
        double lenY = ceil(log10(adjustedY));
        double dy = adjustedY / pow(10, lenY);

        return ((double) x )+ dy;
    }
}
