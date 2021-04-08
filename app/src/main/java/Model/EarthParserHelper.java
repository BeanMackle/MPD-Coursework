package Model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


//Gavin Mackle S1513818
public class EarthParserHelper {



    private String title;

    private String link;

    private String description;

    private String pubDate;

    private String category;

    @SerializedName("geo:lat")
    private double lat;

    @SerializedName("geo:long")
    private double longNum;

    public EarthParserHelper(String title, String link, String description, String pubDate, String category, double lat, double longNum) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.category = category;
        this.lat = lat;
        this.longNum = longNum;
    }

    public EarthParserHelper()
    {
        title = "";
        link = "";
        description = "";
        pubDate = "";
        category = "";
        lat = 0;
        longNum = 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public  String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongNum() {
        return longNum;
    }


    public void setLongNum(double longNum) {
        this.longNum = longNum;
    }

    public EarthQuake MapToEarthQuake()
    {

          String[] extractedDetails = FilterDescription();


          Description desc = new Description(Double.parseDouble(extractedDetails[1]), Double.parseDouble(extractedDetails[2]));

          Location loc = new Location(extractedDetails[0], longNum, lat);

          return BuildEarthQuake(loc, desc);
    }

    private EarthQuake BuildEarthQuake(Location loc, Description desc)
    {
        EarthQuake earthQuake = new EarthQuake(title, desc, link, parseDate(), loc);

        return earthQuake;
    }

    private Date parseDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");

        try
        {
            return formatter.parse(pubDate);
        }
        catch(ParseException e)
        {
            Log.e("Date Parse Error", e.getLocalizedMessage());
            return new Date();
        }
    }

    private String[] FilterDescription()
    {
        if(description.length() == 0)
        {
            return null;
        }

        String[] filteredDescription = new String[3];

        filteredDescription[0] = ExtractLocationName();
        filteredDescription[1] = ExtractDepth();
        filteredDescription[2] = ExtractMagnitude();


        return filteredDescription;

    }


    private String ExtractLocationName()
    {
        String locationConst = "Location:";
        int startIndex = 0;
        int lastIndex = 0;

        startIndex = description.indexOf(locationConst) + (locationConst.length() - 1 );

        //need to find the second ; which is the end of the location
        lastIndex = description.indexOf(";", description.indexOf(";") + 1);

        return BuildString(description, startIndex + 2, lastIndex);
    }

    private String ExtractDepth()
    {
        String depthConst = "Depth:";
        int startIndex = 0;
        int lastIndex = 0;

        startIndex = description.indexOf(depthConst) + (depthConst.length() - 1);

        //need to find the second ; which is the end of the location
        lastIndex = description.lastIndexOf(";");

        return BuildString(description, startIndex + 2, lastIndex - 4);
    }

    private String ExtractMagnitude()
    {
        String MagnitudeConst = "Magnitude:";
        int startIndex = 0;
        int lastIndex = 0;

        startIndex = description.indexOf(MagnitudeConst) + (MagnitudeConst.length() - 1);

        //need to find the second ; which is the end of the location
        lastIndex = description.length();

        return BuildString(description, startIndex + 2, lastIndex);
    }

    private String BuildString(String description, int startIndex, int lastIndex)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = startIndex; i < lastIndex; i++)
        {
            stringBuilder.append(description.charAt(i));
        }

        return stringBuilder.toString();
    }

}
