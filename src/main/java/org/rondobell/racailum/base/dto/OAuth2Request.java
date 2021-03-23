package org.rondobell.racailum.base.dto;

public class OAuth2Request implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3244295999939774165L;

	
	private String appId;
	private String redirectUri;
	private String scope;	
	private String openUid;
	
	//第三方附加信息
	private String state;
	private String code;

	
	public OAuth2Request() {
		
	}
	
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getRedirectUri() {
		return redirectUri;
	}
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getOpenUid() {
		return openUid;
	}
	public void setOpenUid(String openUid) {
		this.openUid = openUid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
