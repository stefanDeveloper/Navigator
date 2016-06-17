package parser;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

import Models.Way;

@XmlRootElement(name = "osm")
@XmlType
public class Osm<E>{
	ArrayList<Way> way;
	Object node;
	int id;
	String note;
	
	public int getId(){
		return this.id;
	}
	
	@XmlAttribute
	public void setId(int id) {
		this.id = id;
	}
	
	public ArrayList<Way> getWay(){
		return this.way;
	}
	
	@XmlElement
	public void setWay(ArrayList<Way> way){
		this.way = way;
	}
	
	public Object getNode(){
		return this.node;
	}
	
	@XmlElement
	public void setNode(Object node){
		this.node = node;
	}
	
	public String getNote(){
		return this.note;
	}
	
	@XmlElement
	public void setNote(String note){
		this.note = note;
	}
}
