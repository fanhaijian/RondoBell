package org.rondobell.akkalas;

import java.io.Serializable;

/**
 * Created by zhuwp on 18/1/30.
 */
public class CommonReqMsg implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -3169263166233919467L;
	private String from;
	private String msgId;
	private AkkaBizEvent event;

	public CommonReqMsg(){

	}

    public CommonReqMsg(String from, String msgId, AkkaBizEvent event) {
    	this.event = event;
    	this.from = from;
        if (event == null) {
            this.msgId = this.from+"_"+this.getClass().getSimpleName() + msgId;
        } else {
            this.msgId = this.from+"_"+event.getValue() + msgId;
        }
    }

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public AkkaBizEvent getEvent() {
		return event;
	}

	public void setEvent(AkkaBizEvent event) {
		this.event = event;
	}

    
}
