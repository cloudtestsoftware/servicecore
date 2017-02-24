package cms.service.test;

import cms.service.util.Base64Util;

public class GenerateToken {

	public GenerateToken() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	  String url="http://sandbox.artitelly.com/testrepo/service?servicekey=";
	  //for regular key without campaign id as 4th element
	  //String val="sjana@cloudtestsoftware.com;srijit96;training";
	  
	  //for campaign as with campaign id
	   String val="sjana@cloudtestsoftware.com;srijit96;campaign";
		try {
			System.out.println(val);
			String  token=new String(Base64Util.encode(val.getBytes()));
			System.out.println(token.toString());
			System.out.println("url="+url+token.toString());
			String decript=new String(Base64Util.decode(token.getBytes()));
			System.out.println(decript.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
