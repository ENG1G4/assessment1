package io.github.eng1g4;

import static java.lang.Math.*;

public class IntTuple {
    public int x;
    public int y;

    public IntTuple(int x, int y){
        this.x = x;
        this.y = y;
    }

    // Squash 2 ints into a double
    // for example (3, 12) will be squashed to 3.121
    public double pack(){
        double adjustedY = (double) y * 10 + 1;
        // appending 1 to the y
        // disambiguates encoding trailing zeroes
        double lenY = ceil(log10(adjustedY));
        double dy = adjustedY / pow(10, lenY);

        return ((double) x )+ dy;
    }
}
