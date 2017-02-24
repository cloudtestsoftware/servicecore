package cms.service.dhtmlx.forms;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import cms.service.dhtmlx.Setting;
import cms.service.dhtmlx.Userdata;
import cms.service.dhtmlx.forms.Item;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Items {
	
	private List<Item> item;
	private Setting setting;
	
	private List<Userdata> userdata;
	
	

	
	
	public Items() {
		
	}

   public Items(List<Item> item) {
		
		this.item = item;
		
	}
	public Items(List<Item> item, Setting setting) {
		
		this.item = item;
		this.setting = setting;
	}

	public List<Item> getItem() {
		return item;
	}

	public void setItem(List<Item> Item) {
		this.item = Item;
	}
	
	public List<Userdata> getUserdata() {
		return userdata;
	}

	public void setUserdata(List<Userdata> userdata) {
		this.userdata = userdata;
	}

	public Setting getSetting() {
		return setting;
	}

	public void setSetting(Setting setting) {
		this.setting = setting;
	}



}
