package Model;

import java.io.Serializable;

//Gavin Mackle S1513818
public class Description implements Serializable {

    private double Depth;

    private double Magnitude;

    public Description()
    {
        Depth = 0;
        Magnitude = 0;
    }

    public Description(double depth, double magnitude)
    {
        Depth = depth;
        Magnitude = magnitude;
    }

    public double getDepth()
    {
        return Depth;
    }

    public void setDepth(double depth)
    {
        Depth = depth;
    }

    public double getMagnitude()
    {
        return Magnitude;
    }

    public void setMagnitude(double magnitude)
    {
        Magnitude = magnitude;
    }
}
