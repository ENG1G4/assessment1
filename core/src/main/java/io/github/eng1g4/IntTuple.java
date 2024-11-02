package io.github.eng1g4;

import static java.lang.Math.*;

public class IntTuple {
    public int x;
    public int y;
    public IntTuple(int x, int y){
        this.x = x;
        this.y = y;
    }

    public double pack(){
        double adjustedY = (double) y * 10 + 1;
        double lenY = ceil(log10(adjustedY));
        double dy = adjustedY / pow(10, lenY);

        return ((double) x )+ dy;
    }
}
