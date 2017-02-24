package cms.service.template;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cms.service.app.ApplicationConstants;
import cms.service.app.PartitionObject;





/**
 * Title:        Semantic Application
 * Description:  Semantic Main Infrastructure Project
 * Copyright:    Copyright (c) 2001
 * Company:      SemanticJava Soft
 * @author
 * @version 1.0
 */

public class TemplateXml{
	static Log logger = LogFactory.getLog(TemplateXml.class);
    private String input="";
    private static String dbobject="";
   // private String fieldname="";
    //private String fdatatype="";
    private boolean isrequired=false;
    private String fvalue="";
    private String insertquery="";
    private String insertvalue="";
    private String updatequery="";
    private String objid="";
    private String querymode;
    private String relation;
    private  String username;
    private  String groupuser;
    private  int[] index;
    private  String[] fields;
    private  String[] datatype;
    private  String[] value;
    private  int colcount=0;
    private TemplateUtility tu=new TemplateUtility();
    private PartitionObject key= new PartitionObject();
    private ApplicationConstants ACONST=new ApplicationConstants();
    private  String dbtype="";
    private  boolean isparent=false;
    private  String relfield;
    private  String strBulksql="";
    private  String parentObjId="";
    private  String selectstr="";
    private  String childidlist="";
    private  TemplateTable data;
    private Document doc;



    

    public TemplateXml() {
    }
    
    public void setRelation(String relation){
      this.relation=relation;
    }
    public String getRelation(){
      return(relation);
    }
    public void setDbType(String dbtype){
      this.dbtype=dbtype;
    }
    public String getDbType(){
      return(dbtype);
    }
    public void setUserName(String uname){
      this.username=uname;
    }
    public String getUserName(){
      return(username);
    }
     public void setGroupUser(String guser){
      this.groupuser=guser;
    }
    public String getGroupUser(){
      return(groupuser);
    }
    public void setParentObjId(String objid){
      parentObjId=objid;
    }
    public String getParentObjId(){
      return(parentObjId);
    }
    public void setInput(String input){
      this.input=input;
    }
    public TemplateTable getTableData(){
        return(data);
    }
    public StringReader getInput(){
      return(new java.io.StringReader(this.input));
    }
    public String getInputString(){
      return(this.input);
    }
    
    public String getPrimaryKey(){
    	return(key.getPrimaryKey());
    	
    }
    
    public boolean parseXml(String xml){
    	
        List<Element> records=new ArrayList<Element>();
        dbobject="";
        strBulksql="";
        childidlist="";
    	if(ApplicationConstants.GENERATE_LOG){
    		logger.info("XML Data="+xml);
    	}
        try {
        	 doc=new SAXReader().read( new StringReader(xml));
        	 records.addAll(doc.getRootElement().elements());
            setInput(xml);
            
            for (Element record: records){
            	objid="";
            	parseRecord(record);
            }
           
           
            //logger.info("after Calling makesql()");
            if(!isparent &&colcount>0)
              strBulksql+="\n\t\tupdate Table_"+dbobject+" set "+getRelation()+"="+(dbtype.equalsIgnoreCase("Oracle")?"parentid":"@parentid")+
                          " where objid in("+childidlist+(getDbType().equalsIgnoreCase("oracle")?");\n":")\n");
            
        }
        catch(DocumentException e){
        		String error=">>>Exception:<<<"+this.getClass().getName()+">>> Failed in reading XML="+xml;
        		logger.info(e.getMessage());
        		logger.info(error);
        }catch (Exception e) {
            e.printStackTrace(System.err);
            return(false);
        }
        return(true);
    }
   
    
    private void parseRecord(Element record) {
    	 List<Element> fileds= record.elements();
    	 dbobject=doc.getRootElement().getName();
    	 fields=new String[fileds.size()];
         datatype=new String[fileds.size()];
         value=new String[fileds.size()];
         index=new int[fileds.size()];
         
         //set query
         insertquery="\tinsert into table_"+ dbobject+ "( "+(isparent?"":relfield+",");
         insertvalue=")values("+(isparent?"":"'0',");
         updatequery="\tupdate table_"+ dbobject+ " set ";
         
         colcount=0;
    	for (Element field:fileds){
    		String elmXml=field.asXML();
    		String fname=field.getName();
    		String fvalue=field.getText();
    		fields[colcount]=field.getName();
    		datatype[colcount]=field.attributeValue("type");
    		index[colcount]=colcount;
    		setValue( fvalue,field.getName(), field.attributeValue("type"),field.attributeValue("isRequired"));
    		
    		
    	}
    	   makeSql();
		
	}

    /** Characters. */
    public void setValue(String fvalue, String fieldname, String fdatatype, String isrequired) {
       
        //fvalue= fvalue.replaceAll("(\\t|\\r?\\n)+", "");
       
        
        //verify if field is objid
        if(!tu.isEmptyValue(fieldname) && fieldname.equalsIgnoreCase("objid")){
        	
        	if(!tu.isEmptyValue(fvalue) && fvalue.trim().length()>=32){
        		objid=fvalue;
        	}else if(tu.isEmptyValue(objid)){
        		 objid=getPrimaryKey().replaceAll("'", "");
        	}
        	 fvalue=objid;
             value[colcount]=objid;
             childidlist+=(childidlist.equals("")?"'"+objid+"'":",'"+objid+"'");
             if(isparent)
             setParentObjId(objid);
             
        }else  if(!tu.isEmptyValue(fieldname) &&!fieldname.equalsIgnoreCase("objid")){
        	//if(isrequired.equalsIgnoreCase("true") &&tu.isEmptyValue(fvalue)){
        	if(tu.isEmptyValue(fvalue)){
        		if(fdatatype.equalsIgnoreCase("NUMBER")||fdatatype.equalsIgnoreCase("INTEGER"))
                    fvalue="0";
                  else
                    fvalue="null";
        	}
        }
        
      value[colcount]=(datatype[colcount].equalsIgnoreCase("DATE")?tu.getConvertDateTime(dbtype,"Insert",fields[colcount],ACONST.DEFAULT_DATE_FORMAT,fvalue): tu.replaceSingleQouteForDatabase(fvalue));
      insertquery+=(colcount==0? fieldname:","+fieldname);
      insertvalue+=(colcount==0? "?":",?");
      updatequery+=(colcount==0? fieldname+"=?":","+fieldname+"=?");
      colcount++;
    } 
   

    //make sql
    private void makeSql(){
                String parentid="";
		String parentname="";
		String mycount="";
                //String bulksql="";
                String updateFilter="";
                String relvalue="";
            //verify if the current object is parent object and do the following
            if(dbtype.equalsIgnoreCase("Oracle")){
			parentid="parentid";
			parentname="parentname";
			mycount="mycount";
		}else if(dbtype.equalsIgnoreCase("Mssql")){
			parentid="@parentid";
			parentname="@parentname";
			mycount="@mycount";
		}

                    if(isparent){
                      int relindex=(relfield!=null &&!relfield.equals("")?tu.getArrayFieldIndex(relfield,fields):0);

                      relvalue=value[relindex];
                    }else{
                      relvalue=parentid;
                    }
                    updateFilter=((relfield!=null && !relfield.equals("")&& relvalue!=null && !relvalue.equals("")) ? (relfield+"="+relvalue +" and ") : "" );
                //Identify the parent table to declare the stored proc variable seting parent objid
		if (isparent) {

			if (dbtype.equalsIgnoreCase("Oracle")) {
				strBulksql = "\n \t \t declare \n \t\t\t " + mycount + " integer :=0; " + "\n \t\t\t " + parentid
						+ " raw(16);" + "\n \t\t\t " + parentname + " nvarchar2(50);" + "\n \t\t Begin \n "
						+ "\n\t\t\t " + parentid + " := '" + parentObjId + "';" + "\n\t\t\t " + parentname + " := '"
						+ dbobject + "';";

			} else if (dbtype.equalsIgnoreCase("Mssql")) {
				strBulksql = "\n \t\t\t declare  " + mycount + " integer; " + "\n \t\t\t declare  " + parentid
						+ " integer;" + "\n \t\t\t declare  " + parentname + " varchar;" + "\n \t\t Begin \n "
						+ "\n\t\t\t select  " + parentid + " = " + parentObjId + ";" + "\n\t\t\t select " + parentname
						+ " = '" + dbobject + "';";

			}

		}

            selectstr=" \n\t\t\t select " + (dbtype.equalsIgnoreCase("Oracle")==true ?  " count(ObjId)  into "+ mycount :mycount+"= count(ObjId)") + "  from Table_"+ dbobject +
                                    " where " + //((updateFilter.equals("")&& !isparent)==true ? (relfield +"="+parentid + " and "): updateFilter )+
                                    "objid ='"+value[tu.getArrayFieldIndex("objid",fields)] +(getDbType().equalsIgnoreCase("oracle")?"';":"");
                                    //"upper(ltrim(rtrim(name))) = upper(ltrim(rtrim('"+value[tu.getArrayFieldIndex("name",fields)]+"')))" +(getDbType().equalsIgnoreCase("oracle")?";":"");
            //complete the update and insert query
            insertquery+=",groupuser,genuser,gendate"+insertvalue+",'"+getGroupUser()+"','"+getUserName()+"',"+
            (getDbType().equalsIgnoreCase("oracle")?"sysdate);":"getdate())");
            //logger.info("insertquery="+insertquery);
            updatequery+=",ModUser='"+getUserName()+"',ModDate="+(getDbType().equalsIgnoreCase("oracle")?"sysdate where objid='"+value[0]+"';":"getdate() where objid='"+value[0]+"'");
            TemplateQuery query = new TemplateQuery();

            query.setInputTable(tu.getIntegerArrayByLength(index,colcount),tu.convertDataType(tu.getStringArrayByLength(datatype,colcount)),tu.getStringArrayByLength(value,colcount));
            query.setQuery(insertquery);
            String tmpinsert=query.getQuery();
            query.setQuery(updatequery);
            String tmpupdate=query.getQuery();
            //added these row to return all row set for the current atble
            if(data!=null&&data.getRowCount()<1)
              data.addColumns(tu.getStringArrayByLength(fields,colcount));
              data.addRow(tu.getStringArrayByLength(value,colcount));
            strBulksql+="\n\n\t\tBegin \n \t\t\t\t\t" + selectstr +
                      "\n\t\t\t if( "+ mycount +">0)"+ (dbtype.equalsIgnoreCase("Oracle")==true ?  " then " :"" )+
                      "\n\t\t\t"+tmpupdate+  "\n\t\t\t else \n \t\t\t " +
                      tmpinsert +(dbtype.equalsIgnoreCase("Oracle")==true ?  "\n \t\t\t end if;" :"" )   +
                      "\n\t\t\t" + (dbtype.equalsIgnoreCase("Oracle")==true ? ("\t exception \n\t\t\t\t when no_data_found then \n \t\t\t\t "+ mycount + ":=0; \n \t\tend;" ): ("\n\t\tend;")) ;

           // logger.info(strBulksql);
    }
    

  

    /* User should supply the follwing values for each form
	 ** dbtype= "Oracle" or "Mssql"
	 ** table = object name without Table_
         ** isparent= whether the table is parent table in current screen object? true or false, if false relation value=null
	 ** fields = list of all database fields name same as table fields
	 ** datatype = database specific datatype for each fields
         ** data = all the data for each form
	 ** querymode = insert,update,delete,modify
         ** in modify mode the data will be updated which has objid!=null and inserted for the records which has objid=null
         ** in modify mode the records which are deleted from the grid will be updated for their relation =null
	 ** relfield = relation field of the child table i.e. foreign key
         ** relvalue = relation value of the child table i.e. foreign key value
	 ** mtmtable= mtm table for relation will have atleast 4 fields ObjId2Parent, ParentObject, ObjId2Child,ChildObject
         ** Name and relation field is always should be composite key and unique in current design
         **
         **To Fix::::
         **
         ** Fix the problem while updating querymode=update/modify you are loosing extra objid for parent object
	*/

	public String makeBulkSQL(String  dbtype,boolean isparent,String xml,
                             String relfield,String username,String groupuser){
                this.dbtype=dbtype;
                this.isparent=isparent;
                this.querymode=querymode;
                this.relfield=relfield;
                data=new TemplateTable();
               // System.out.print(">>>>>>>>>>>>>xml="+xml);
                setDbType(dbtype);
               
                setRelation(relfield);
                setUserName(username);
                setGroupUser(groupuser);
                //System.out.print(">>>>>>>>>>>>>relation="+getRelation());
                if(xml==null||xml.equals(""))
                  return("");
                if(parseXml(xml)){
                    dbtype="";
                    username="";
                    System.out.println(strBulksql);
                    return(strBulksql);
                }
            return("");

        }
       

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TemplateXml test= new TemplateXml();
		
		/* String data="";
		for(String x:args){
			data+=x +" ";
		}
		String xml=data.replaceAll("> <", ">\n<");
		System.out.println(xml);
		*/
		
		
     String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
    		  "<entity>"+
      "<record id=\"0\">"+
      "<objid isRequired=\"true\" type=\"RAW\">0D91A7B8DDE2662CE050B90A27933D75</objid>"+
      "<name isRequired=\"false\" type=\"VARCHAR\">Soft</name>"+
      "<description isRequired=\"false\" type=\"VARCHAR\">This is for payroll project&lt;div&gt;Lets try to do&lt;/div&gt;</description>"+
      "<expertise isRequired=\"false\" type=\"VARCHAR\">Test</expertise>"+
      "<licensecount isRequired=\"false\" type=\"NUMBER\">100</licensecount>"+
      "<virtuallicense isRequired=\"false\" type=\"NUMBER\">100</virtuallicense>"+
      "<licenseused isRequired=\"false\" type=\"NUMBER\">0</licenseused>"+
      "<virtuallicenseused isRequired=\"true\" type=\"NUMBER\">0</virtuallicenseused>"+
      "</record>"+
      "</entity>";
   
		try {
			test.makeBulkSQL("oracle", true, xml, "", "sa", "sa");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
   

} // class TemplateXml
