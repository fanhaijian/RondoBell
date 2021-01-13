package org.rondobell.akkalas;

import java.util.List;

public class RadioReqMsg extends CommonReqMsg {

	private List<Long> radioIdList;

	public List<Long> getRadioIdList() {
		return radioIdList;
	}

	public void setRadioIdList(List<Long> radioIdList) {
		this.radioIdList = radioIdList;
	}

	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("{msgId=").append(getMsgId());
		sb.append(",radioIdList=").append(radioIdList);
		sb.append("}");
		return sb.toString();
	}
}
