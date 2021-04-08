package Model;

import java.io.Serializable;

//Gavin Mackle S1513818
public class Location implements Serializable {

    private String Name;

    private double Longitude;

    private double Latitude;

    public Location()
    {
        Name = "";
        Longitude = 0;
        Latitude = 0;
    }

    public Location(String name, double longitude, double latitude)
    {
        Name = name;
        Longitude = longitude;
        Latitude = latitude;
    }

    public void setName(String name)
    {
        Name = name;
    }


    public void SetLongtitude(double longitude)
    {
        Longitude = longitude;
    }

    public void SetLatitude(double latitude){
        Latitude = latitude;
    }

    public String getName() {
        return Name;
    }

    public double GetLongtitude()
    {
        return Longitude;
    }

    public double GetLatitude()
    {
        return Latitude;
    }
}
