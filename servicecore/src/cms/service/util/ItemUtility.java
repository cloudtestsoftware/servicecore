/*
 * Copyright (C) 2014 Artitelly Inc,
 *
 * Licensed under the Common Public Attribution License Version 1.0 (CPAL-1.0) (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/CPAL-1.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cms.service.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;



public class ItemUtility {
	
	static Log logger = LogFactory.getLog(ItemUtility.class);
	private final String USER_AGENT = "Mozilla/5.0";
	/*
	 * Replace single item_id from the XML which is matching the name
	 * @param itemNode= Item for which ship options
	 */
	public Element replaceVariable(Element elm, String var, String value){
		Element elmNew=null;	
		
		try {
			String xml=elm.asXML().replaceAll(var, value);
			elmNew = new SAXReader().read( new StringReader(xml)).getRootElement();
		} catch (DocumentException e) {
			logger.info(">>>Exception:<<<"+this.getClass().getName()+">>> Failed to replace parameter "+var +" with value="+value);
	       // TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			logger.info(">>>Exception:<<<"+this.getClass().getName()+">>> Failed to replace parameter "+var +" with value="+value +"for xml="+elm.asXML());
	      // TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return(elmNew);
	}
	/*
	 * Replace single item_id from the XML which is matching the name
	 * @param itemNode= Item for which ship options
	 */
	public Element replaceAttributeValue(Element elm, String var, String value){
		Element elmNew=null;	
		
		try {
			String xml=elm.asXML().replaceAll(var, value);
			elmNew = new SAXReader().read( new StringReader(xml)).getRootElement();
		} catch (DocumentException e) {
			logger.info(">>>Exception:<<<"+this.getClass().getName()+">>> Failed to replace parameter "+var +" with value="+value);
	      	// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			logger.info(">>>Exception:<<<"+this.getClass().getName()+">>> Failed to replace parameter "+var +" with value="+value +"for xml="+elm.asXML());
	       // TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return(elmNew);
	}
	
	
	public Element replaceData(Element target,HashMap<String,String> varmap){
		  //ItemUtility iu=new ItemUtility();
		  String xml=target.asXML();
		 
			for(String key :varmap.keySet()){
				xml=xml.replaceAll(key, varmap.get(key).trim());
			}
			Element newElm=this.getRootElementFromXML(xml);
			
			return newElm;
	  }
 	
	
	
	/*
	 * Set variable with value
	 * @varlist itemNode= Item for which ship options
	 */
	public HashMap<String,String> getItemValueMap( String xml){
			List<Element> itemlist;
			List<Element> items;
			HashMap<String,String> varmap= new HashMap<String,String>();
			Element root=this.getRootElementFromXML(xml);
			if(root!=null){
				items= root.elements();
				if(items.size()==1 &&items.get(0).hasContent()){
					itemlist=items.get(0).elements();
				}else{
					itemlist=items;
				}
				for(Element var:itemlist){
				  String name=var.getName();
				  String value=var.getText();	
				  varmap.put(name,value);
				}
			}
			
	     return varmap;
	}
	
	
	
	/*
	 * Replace all item_id from the XML which is matching the name
	 * @param key= key should be with "@" key name in the XML. 
	 * While passing the key pass only key name without "@"
	 */
	public Element replaceAllGlobalData(Element elm, String key, String value){
		Element elmNew=null;		
		String xml=elm.asXML().replaceAll("@"+key, value);
		//System.out.println(xml);
		try {
			elmNew = new SAXReader().read( new StringReader(xml)).getRootElement();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return(elmNew);
	}
	
	public Document getDocument(String xml){
	    
	     Document doc=null;
	 
	     try{
	         doc=new SAXReader().read( new StringReader(xml));	         
	         
	     }catch(DocumentException e){
	         logger.info(">>>Exception:<<<"+this.getClass().getName()+">>> Failed in reading XML Input data to the Webservice ");
	     } 
	     
	     return doc;
	 }
	
	/*
	 * Root element from xml
	 * @param xml
	 */
	public Element getRootElementFromXML( String xml){
		Element elmNew=null;		
		
		try {
			elmNew = new SAXReader().read( new StringReader(xml)).getRootElement();
		} catch (DocumentException e) {
			logger.info(">>>Exception:<<<"+this.getClass().getName()+">>> Failed in reading XML Input data to the Webservice ");
			logger.info("XML="+xml);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return(elmNew);
	}
	
	/*
	 * Element value from XML for a xpath
	 * @param xml & xpath of the node
	 */
	public String getElementValueByXpath( String xml, String xpath,String index){
		String val="";
		int idx=Integer.parseInt(index);
		int count=0;
		//System.out.println(xml);
		List<Node> nodes = this.getDocument(xml).selectNodes(xpath);
		for(Node n:nodes ){
			if(count==idx){
			 val=n.getText();
			 break;
			}
			count++;
		}
			
		return(val);
	}
	
	
	public String getRandomNumber(){
		 Random generator = new Random();      
		return(String.valueOf(generator.nextInt(999999999)));
	}
	
	public String getSmallRandomNumber(){
		 Random generator = new Random();      
		return(String.valueOf(generator.nextInt(999)));
	}
	public String getColumnValue(ArrayList<ArrayList<Object>> rsData,int rowId, String column){
 		int colIndex=0;
 		for (Object col:rsData.get(0)){
 			if(col.toString().equalsIgnoreCase(column))
 				break;
 			else 				
 				colIndex++;
 		}
 		int rowIdx=(rowId==0?1:rowId);
 		String val=null;
 		try{
 			val=rsData.get(rowIdx).get(colIndex).toString();
 		}catch (Exception e) { 			
 		}
 		return(val==null?"":val);
 		
 	}
	
	 
	 public static boolean isEmptyValue(String val){
			if(val==null|| val.isEmpty()){
				return true;
			}
			return false;
		}
	 
	// HTTP POST request
     public String sendPost(String url, String urlParams) throws Exception {
	       
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
			//add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	 
			
	 
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParams);
			wr.flush();
			wr.close();
	 
			int responseCode = con.getResponseCode();
			logger.info("\nSending 'POST' request to URL : " + url);
			logger.info("Post parameters : " + urlParams);
			logger.info("Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	 
			//print result
			return(response.toString());
	 
		}

}
