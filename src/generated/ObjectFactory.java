//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.06.17 um 11:26:16 AM CEST 
//


package generated;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Osm }
     * 
     */
    public Osm createOsm() {
        return new Osm();
    }

    /**
     * Create an instance of {@link Osm.Way }
     * 
     */
    public Osm.Way createOsmWay() {
        return new Osm.Way();
    }

    /**
     * Create an instance of {@link Osm.Meta }
     * 
     */
    public Osm.Meta createOsmMeta() {
        return new Osm.Meta();
    }

    /**
     * Create an instance of {@link Osm.Node }
     * 
     */
    public Osm.Node createOsmNode() {
        return new Osm.Node();
    }

    /**
     * Create an instance of {@link Osm.Way.Nd }
     * 
     */
    public Osm.Way.Nd createOsmWayNd() {
        return new Osm.Way.Nd();
    }

    /**
     * Create an instance of {@link Osm.Way.Tag }
     * 
     */
    public Osm.Way.Tag createOsmWayTag() {
        return new Osm.Way.Tag();
    }

}
