package edu.ucla.cs.cs144;

import javax.xml.bind.annotation.*;

/**
 * Created by ChenHao on 2/9/17.
 */
public class Bid {
    @XmlElement(name = "Bidder")
    public Bidder bidder;

    @XmlElement(name = "Time")
    public String time;

    @XmlElement(name = "Amount")
    public String amount;
}
