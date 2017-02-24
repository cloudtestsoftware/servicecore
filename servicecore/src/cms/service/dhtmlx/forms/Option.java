package cms.service.dhtmlx.forms;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;




@XmlAccessorType(XmlAccessType.FIELD)

public class Option {
	 @XmlAttribute(required=true) 
	private String value;
	 
	 @XmlAttribute(required=true) 
	private String label;
	
	 /*
	@XmlElementRef(name = "item", type = Item.class)
	@XmlMixed
	private List<String> text;
	
	*/
	
	public Option() {
		
	}
	public Option(String text) {
		
	}

	public Option(String value, String label) {
		
		this.value = value;
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

	
	
	

}
