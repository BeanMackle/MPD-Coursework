package Model;

import java.io.Serializable;
import java.util.Date;

//Gavin Mackle S1513818
public class EarthQuake implements Serializable {

    private String Title;

    private Description Description;

    private String Link;

    private Date PublishDate;

    private Location Location;


    public EarthQuake()
    {
     Title = "";
     Description = new Description();
     Link = "";
     PublishDate = new Date();
     Location = new Location();
    }

    public EarthQuake(String title, Description description, String link, Date publishDate, Location location)
    {
        Title = title;
        Description = description;
        Link = link;
        PublishDate = publishDate;
        Location = location;
    }

    public EarthQuake(String title, double magnitude, double depth, String link, Date publishDate, String locationName, double longitude, double latitude)
    {
        Title = title;
        Description = new Description(depth, magnitude);
        Link = link;
        PublishDate = publishDate;
        Location = new Location(locationName, longitude, latitude);
    }
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Description getDescription() {
        return Description;
    }

    public void setDescription(Description description) {
        Description = description;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public Date getPublishDate() {
        return PublishDate;
    }

    public void setPublishDate(Date publishDate) {
        PublishDate = publishDate;
    }

    public Model.Location getLocation() {
        return Location;
    }

    public void setLocation(Model.Location location) {
        Location = location;
    }
}
