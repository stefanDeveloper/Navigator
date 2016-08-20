package de.dhbw.navigator.models;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Konrd on 14.08.2016.
 */
public class NodeCoordinate implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal lon;
    private BigDecimal lat;

    public NodeCoordinate(Node n){
        setLon(n.getLon());
        setLat(n.getLat());
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }
}
