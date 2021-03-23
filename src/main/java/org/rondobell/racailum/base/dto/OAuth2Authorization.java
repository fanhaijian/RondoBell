package org.rondobell.racailum.base.dto;

public class OAuth2Authorization implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 733263563655588383L;
	
	private String appId;
	private String redirectUri;
	private String scope;
	
	private String openUid;
	private OAuth2Token accessToken;
	private OAuth2Token refreshToken;
	
	public OAuth2Authorization() {
		
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
	public String getOpenUid() {
		return openUid;
	}
	public void setOpenUid(String openUid) {
		this.openUid = openUid;
	}

	public OAuth2Token getAccessToken() {
		return accessToken;
	}


	public void setAccessToken(OAuth2Token accessToken) {
		this.accessToken = accessToken;
	}


	public OAuth2Token getRefreshToken() {
		return refreshToken;
	}


	public void setRefreshToken(OAuth2Token refreshToken) {
		this.refreshToken = refreshToken;
	}

}
