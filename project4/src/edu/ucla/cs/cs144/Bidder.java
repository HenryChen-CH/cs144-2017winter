package edu.ucla.cs.cs144;

import javax.xml.bind.annotation.*;

/**
 * Created by ChenHao on 2/9/17.
 */
public class Bidder {
    @XmlAttribute(name = "Rating")
    public String rating;

    @XmlAttribute(name = "UserID")
    public String userID;

    @XmlElement(name = "Location")
    public String location;

    @XmlElement(name = "Country")
    public String country;
}
