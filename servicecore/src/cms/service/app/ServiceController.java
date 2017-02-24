package cms.service.app;


import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cms.service.template.TemplateUtility;
import cms.service.util.Base64Util;

import java.util.*;

/**
 * Title:        Semantic ServiceController
 * Description:  This project is developed for Semantic ServiceController
 * Copyright:    Copyright (c) 2001
 * Company:      Semanticjava Soft
 * @author S .K.jana
 * @version 1.0
 */

public class ServiceController extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1658580752127244463L;
	static Log logger = LogFactory.getLog(ServiceController.class);

    private ApplicationConstants CONST=new ApplicationConstants();
    private static ServiceManager m_service;
    private TemplateUtility tu=new TemplateUtility();
    private ServletContext m_autoContext;
    public static String contextPath;
    
  public void init (javax.servlet.ServletConfig conf) throws javax.servlet.ServletException
  {
        logger.info("\n Initializing Controller Servlet ");
        logger.info("\n  ");
        logger.info("\n  "+conf.getServletContext().getRealPath("WEB-INF"));
        contextPath=conf.getServletContext().getRealPath("WEB-INF");
        super.init(conf);
        m_service=new ServiceManager();  
        this.m_autoContext = conf.getServletContext();
     
  }
  /**
    * Handle the GET requests (they are the default)
    */
    public void doGet(HttpServletRequest request,
                          HttpServletResponse response)
           throws ServletException, IOException {
           doPost(request, response);
    }
     public void doPost (HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException
    {
    	HashMap<String, String> userdata;
    	AccessToken access=null;
    	AccessToken regaccess=null;
        RequestDispatcher rd;
        String strNextPage = null;
        boolean isvalidtoken=false;
        boolean subscription=false;
        String reguser="registration";
        String regpassword="reg$56*123";
        String baseurl=request.getRequestURL().toString().split("/service")[0];
        String remotehost=request.getRemoteHost();
        String remoteaddress=request.getRemoteAddr();
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String useraction=request.getParameter("useraction");
        String adminuser=request.getParameter("adminuser");
        String usertoken=request.getParameter("usertoken");
        String servicekey=request.getParameter("servicekey");
        String amount=request.getParameter("amount");
        String objid=request.getParameter("refobjid");
        String description=request.getParameter("description");
        String leadactions="campaign whitepapers contactus training";
        String sendreference=request.getParameter("sendreference");;
       
        Date date= new Date();

        //set remote address
        request.setCharacterEncoding("UTF-8");
        
       
      
        /*if(custom!=null&&!custom.equals("")){
          custom="dan@softlean.com#-1624640437#127.0.0.1";
          item_name="xyx";
          item_number="899";
        }
      
        subscription=(custom!=null&&!custom.equals(""))?true:false;
        logger.info(date.toString()+":-subscription="+subscription+" custom="+custom);
        if(subscription){

          custret=custom.split("-");
          if(custret.length==6)
          username=custret[3];
          usertoken=custret[4];
         
          tu.updatePayment(custret);
        }
        */
        //first verify remote client whether the request is from same client
        if(!tu.isEmptyValue(servicekey)){
        	try {
				String val=new String(Base64Util.decode(servicekey.getBytes()));
				logger.info("servicekey="+val);
				if(!tu.isEmptyValue(val)){
					String[] items=val.split(CONST.IPSEPERATOR);
					if(items.length>=2){
						username=items[0];
						password=items[1];
					}
					if(items.length==3){
						useraction=items[2];
						if(useraction.equalsIgnoreCase("campaign") &&tu.isEmptyValue(sendreference)){
							sendreference="2";
						}
					}
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        //logger.info("\n"+date.toString()+":-username="+username+ " password="+password+" useraction="+useraction);
        
        if(CONST.GENERATE_LOG){
          logger.info("\n"+date.toString()+ ":-URI="+request.getRequestURI());
          logger.info("\n"+date.toString()+":-Request Remote address="+remoteaddress+ " Remote Host="+remotehost);
        }
        //Identify the current page as login page
        if (!tu.isEmptyValue(username) &&!tu.isEmptyValue(password) ){
         
            //Do not check license for hosting solution
        	access= m_service.verifyLogin(username,password,remoteaddress);
            
        }else{
        	regaccess=m_service.verifyLogin(reguser,regpassword,remoteaddress);
        	
        }
        
        
        if(access!=null && tu.isEmptyValue(useraction)){            
           logger.info("\n"+date.toString()+" Loged in User:="+username+" "+":-Assigned Token="+access.getToken()+":-Assigned Modules="+access.getModules());
           //access token is appended with Client IP in the indexpage
           strNextPage="/src/index.jsp?username="+username+"&firstname="+access.getFirstname()+"&usertoken="+access.getToken()+CONST.IPSEPERATOR+remoteaddress+CONST.IPSEPERATOR+username+"&baseurl="
           +baseurl+"&modules="+access.getModules();     
        }else if(access!=null && !tu.isEmptyValue(useraction) && leadactions.toLowerCase().contains(useraction.toLowerCase()) ){  
        	 strNextPage="/src/campaign/"+useraction+".jsp?username="+username+"&firstname="+access.getFirstname()+"&usertoken="+access.getToken()+CONST.IPSEPERATOR+remoteaddress+CONST.IPSEPERATOR+username+"&baseurl="
        	           +baseurl+"&campaignid="+objid+"&sendreference="+sendreference;
        	 //logger.info("\n"+date.toString()+"strNextPage:="+strNextPage);
        //}else if(access!=null && !tu.isEmptyValue(useraction) && useraction.equalsIgnoreCase("whitepapers")){  
       	 //strNextPage="/src/campaign/white_paper.jsp?username="+username+"&firstname="+access.getFirstname()+"&usertoken="+access.getToken()+CONST.IPSEPERATOR+remoteaddress+CONST.IPSEPERATOR+username+"&baseurl="
       	  //         +baseurl+"&campaignid="+objid;
       	 	//logger.info("\n"+date.toString()+"strNextPage:="+strNextPage);     
        }else if(subscription){
        	userdata=m_service.verifyUserToken( usertoken);
           isvalidtoken=usertoken!=null && !usertoken.equals("") && userdata!=null;
            if(isvalidtoken) {
            	strNextPage="/src/index.jsp?username="+username+"&usertoken="+usertoken+"&baseurl="+baseurl;      
            }          
        }else if(!tu.isEmptyValue(useraction) &&useraction.equalsIgnoreCase("missingpassword")){
        	
        	strNextPage="/src/password.jsp?token="+regaccess.getToken()+CONST.IPSEPERATOR+remoteaddress+CONST.IPSEPERATOR+reguser+"&baseurl="+baseurl; 
        }else if(!tu.isEmptyValue(useraction) &&useraction.equalsIgnoreCase("demoregistration")){
        	
        	strNextPage="/src/demologin.jsp?token="+regaccess.getToken()+CONST.IPSEPERATOR+remoteaddress+CONST.IPSEPERATOR+reguser+"&baseurl="+baseurl;   
        	
        }else if(!tu.isEmptyValue(adminuser) &&adminuser.equals("sa")){
        	strNextPage="/src/admin.jsp?token="+regaccess.getToken()+CONST.IPSEPERATOR+remoteaddress+CONST.IPSEPERATOR+reguser+"&baseurl="+baseurl; 
        	
        }else if(!tu.isEmptyValue(amount) &&!tu.isEmptyValue(objid)){
        	userdata=m_service.verifyUserToken( usertoken);
        	if(userdata!=null &&!userdata.isEmpty()){
        		strNextPage="/src/java_sim/payment.jsp?email="+username+"&objid="+objid+"&amount="+amount+"&description="+description;  
        		
        	}
        	 
    
        }else if(regaccess!=null){
        	strNextPage="/src/login.jsp?token="+regaccess.getToken()+CONST.IPSEPERATOR+remoteaddress+CONST.IPSEPERATOR+reguser+"&baseurl="+baseurl;    
        }else{
        	strNextPage="/src/error.jsp?token=null"+CONST.IPSEPERATOR+remoteaddress+CONST.IPSEPERATOR+reguser+"&baseurl="+baseurl; 
        }
        
        if(CONST.GENERATE_LOG){
            logger.info ("\n"+date.toString()+":-Mapped Filename : " + strNextPage);
         }
        if (!strNextPage.equals("")) {
              rd = m_autoContext.getRequestDispatcher(strNextPage);
        // Forward the request to the target page
              try {
                  if (rd != null) {

                    rd.forward(request, response);
                  }
              }catch (Exception e) {
            	   
                    logger.info("ControllerServlet.doPost():  error in rd.forward");
                    e.printStackTrace();
                }
              } else {
              // This should be logged.
                logger.info("Next Page is null");
                super.doPost(request,response);
            }
	        
    }


}