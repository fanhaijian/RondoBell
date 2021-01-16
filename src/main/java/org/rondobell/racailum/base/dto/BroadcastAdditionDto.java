package org.rondobell.racailum.base.dto;

import java.io.Serializable;

public class BroadcastAdditionDto implements Serializable{

	private static final long serialVersionUID = 8396277718390610493L;
	
	private Long id;
	private Long likedNum;
	
	public BroadcastAdditionDto() {
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setLikedNum(Long likedNum) {
		this.likedNum = likedNum;
	}
	
	public Long getLikedNum() {
		return likedNum;
	}
	
	

}
