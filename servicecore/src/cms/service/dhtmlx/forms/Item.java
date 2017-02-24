package cms.service.dhtmlx.forms;

import java.util.List;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;




@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class Item {
	
	@XmlAttribute(required=true) 
	 private String type;
	
	 @XmlAttribute(required=false) 
	 private String label;
	 
	 @XmlAttribute(required=false) 
	 private String checked;
	 
	 @XmlAttribute(required=false) 
	 private String name;
	 
	 @XmlAttribute(required=false) 
	 private String inputWidth;
	 
	 @XmlAttribute(required=false) 
	 private String position;
	 
	 @XmlAttribute(required=false) 
	 private String labelWidth;
	 
	 @XmlAttribute(required=true) 
	 private String id;
		
	 @XmlAttribute(required=false) 
	 private String dateFormat;
	
	 @XmlAttribute(required=false) 
	 private String value;
	 
	 @XmlAttribute(required=false) 
	 private String inputHeight;
	 
	 @XmlAttribute(required=false) 
	 private String toolbar;
	 
	 @XmlAttribute(required=false) 
	 private String iconsPath;
	 
     
	
	private List<Option> option;
	//private List<Item> item;
	
	
   
	
	public String getId() {
		return id;
	}



	public String getInputHeight() {
		return inputHeight;
	}



	public void setInputHeight(String inputHeight) {
		this.inputHeight = inputHeight;
	}



	

	public void setId(String id) {
		this.id = id;
	}



	public String getDateFormat() {
		return dateFormat;
	}



	public void setDateFormat(String format) {
		this.dateFormat = format;
	}
/*
	public List<Item> getItem() {
		return item;
	}



	public void setItem(List<Item> item) {
		this.item = item;
	}
	*/



	public String getLabel() {
		return label;
	}



	public void setLabel(String label) {
		this.label = label;
	}



	public String getChecked() {
		return checked;
	}



	public void setChecked(String checked) {
		this.checked = checked;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getInputWidth() {
		return inputWidth;
	}



	public void setInputWidth(String inputWidth) {
		this.inputWidth = inputWidth;
	}



	public String getPosition() {
		return position;
	}



	public void setPosition(String position) {
		this.position = position;
	}



	public String getLabelWidth() {
		return labelWidth;
	}



	public void setLabelWidth(String labelWidth) {
		this.labelWidth = labelWidth;
	}



	public String getValue() {
		return value;
	}



	public void setValue(String value) {
		this.value = value;
	}



	
	public List<Option> getOption() {
		return option;
	}



	public void setOption(List<Option> option) {
		this.option = option;
	}




	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}


	
	public String getToolbar() {
		return toolbar;
	}



	public void setToolbar(String toolbar) {
		this.toolbar = toolbar;
	}



	public String getIconsPath() {
		return iconsPath;
	}



	public void setIconsPath(String iconsPath) {
		this.iconsPath = iconsPath;
	}



	public Item(String value) {
	
	}
	public Item(List<Option> option) {
		this.option=option;
	}
	
	
	public Item() {
		
	}

	

	public Item(String label, String checked,String type, String name, String labelWidth, String position,String inputWidth,
			List<Item> item) {
		this.label=label;
		this.checked=checked;
		this.type = type;
		this.name=name;
		this.labelWidth = labelWidth;
		this.position=position;
		this.inputWidth=inputWidth;
		//this.item = item;
	}


	public Item(String type, String name, String labelWidth, String position,String inputWidth) {
		this.inputWidth=inputWidth;
		this.type = type;
		this.name=name;
		this.labelWidth = labelWidth;
		this.position=position;
		
	}

	public Item(String type, String name, String inputWidth, String label) {
		this.inputWidth=inputWidth;
		this.type = type;
		this.name=name;
		this.inputWidth=inputWidth;
		
	}
	
	public Item(String label, String checked,String type, String name, String labelWidth, String position,String value) {
		this.label=label;
		this.checked=checked;
		this.type = type;
		this.name=name;
		this.labelWidth = labelWidth;
		this.position=position;
		this.value = value;
	}
	
	public Item(String label, String type, String name, List<Option> options) {
		this.label=label;
		this.type = type;
		this.name=name;
		this.option = option;
	}
	
	public Item(String type, String name, String value) {
		this.type = type;
		this.name=name;
		this.value=value;
	}
	public Item( String name, String value) {
		this.name=name;
		this.value=value;
	}


	
	

}
