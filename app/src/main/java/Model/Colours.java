package Model;

import android.graphics.Color;


import com.google.android.gms.maps.model.BitmapDescriptorFactory;

//Gavin Mackle S1513818
public class Colours {

    /** This whole thing isn't very nice, There's much better ways to do this */

    private static final String Weak  = "#CCFFFF";

    private static final String Light = "#66FFFF";

    private static final String Moderate = "#99FF00";

    private static final String Strong = "#FFFF00";

    private static final String VeryStrong = "#FFCC00";

    private static final String Severe = "#FF6600";

    private static final String Violent = "#FF0000";

    private static final String Extreme = "#990000";

    public static int GetEarthquakeDisplayColour(double magnitude)
    {
       String colour = ReturnColour(magnitude);

       return Color.parseColor(colour);
    }

    public static float GetPinColour(double magnitude)
    {
       String colour = ReturnColour(magnitude);

    return GetPinColour(colour);
    }

    private static String ReturnColour(double magnitude)
    {
        if(magnitude < 0.3)
        {
            return Weak;
        }
        else if(magnitude >= 0.3 && magnitude < 0.6)
            {
                return Light;
            }
        else if(magnitude >= 0.6 && magnitude < 0.9)
        {
            return Moderate;
        }
        else if(magnitude >= 0.9 && magnitude < 1.2)
        {
            return Strong;
        }
        else if(magnitude >= 1.2 && magnitude < 1.5)
        {
            return VeryStrong;
        }
        else if(magnitude >= 1.5 && magnitude < 1.8)
        {
            return Severe;
        }
        else if(magnitude >= 1.8 && magnitude < 2.1)
        {
            return Violent;
        }
        else
            {
                return Extreme;
            }

    }

    private static float GetPinColour(String colour)
    {
        if(colour == Weak || colour == Light)
        {
            return BitmapDescriptorFactory.HUE_CYAN;
        }
        if(colour == Moderate)
        {
            return BitmapDescriptorFactory.HUE_GREEN;
        }
        if(colour == Strong)
        {
            return BitmapDescriptorFactory.HUE_YELLOW;
        }

        if(colour == VeryStrong)
        {
            return 45f;
        }

        if(colour == Severe)
        {
            return BitmapDescriptorFactory.HUE_ORANGE;
        }

        else
            {
                return BitmapDescriptorFactory.HUE_RED;
            }
    }
}
