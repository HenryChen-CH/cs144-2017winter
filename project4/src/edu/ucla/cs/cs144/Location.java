package edu.ucla.cs.cs144;

import javax.xml.bind.annotation.*;

/**
 * Created by ChenHao on 2/9/17.
 */
public class Location {
    @XmlValue
    public String location;

    @XmlAttribute(name = "Latitude")
    public String latitude;

    @XmlAttribute(name = "Longitude")
    public String longitude;
}
