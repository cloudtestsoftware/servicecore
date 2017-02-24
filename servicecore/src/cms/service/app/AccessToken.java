package cms.service.app;

public class AccessToken {
	
	private String groupuser;
	private String loginusers;
	private String remoteips;
	private String token;
	private String modules;
	private String firstname;
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	private long tokenexpiry;

	
	public String getGroupuser() {
		return groupuser;
	}
	public void setGroupuser(String groupuser) {
		this.groupuser = groupuser;
	}
	public String getLoginusers() {
		return loginusers;
	}
	public void setLoginusers(String loginusers) {
		this.loginusers = loginusers;
	}
	public String getRemoteips() {
		return remoteips;
	}
	public void setRemoteips(String remoteips) {
		this.remoteips = remoteips;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public long getTokenexpiry() {
		return tokenexpiry;
	}
	public void setTokenexpiry(long tokenexpiry) {
		this.tokenexpiry = tokenexpiry;
	}
	public String getModules() {
		return modules;
	}
	public void setModules(String modules) {
		this.modules = modules;
	}
		
	
	

}
