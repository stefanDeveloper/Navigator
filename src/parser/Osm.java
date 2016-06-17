package parser;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlRootElement(name = "osm")
@XmlType
public class Osm {
	int ref;
	Object tag;
	int id;
	
	@XmlAttribute
	public void setId(int id) {
		this.id = id;
	}
	
	@XmlElement
	public void setRef(int ref){
		this.ref = ref;
	}
	
	@XmlElement
	public void setTag(Object tag){
		this.tag = tag;
	}
	
	public int getId(){
		return this.id;
	}
	
	public Object getTag(){
		return this.tag;
	}
}
