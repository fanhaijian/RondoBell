package org.rondobell.racailum.base.dto;

import java.util.Date;

public class OAuth2Token {
	
	private String value;
	Date expiredTime;
	
	public OAuth2Token() {	
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Date getExpiredTime() {
		return expiredTime;
	}
	public void setExpiredTime(Date expiredTime) {
		this.expiredTime = expiredTime;
	}

	
}
