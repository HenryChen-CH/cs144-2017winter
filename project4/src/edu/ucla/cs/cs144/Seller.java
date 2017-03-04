package edu.ucla.cs.cs144;

import javax.xml.bind.annotation.*;

/**
 * Created by ChenHao on 2/9/17.
 */
public class Seller {
    @XmlAttribute(name = "Rating")
    public String rating;

    @XmlAttribute(name = "UserID")
    public String userID;
}
