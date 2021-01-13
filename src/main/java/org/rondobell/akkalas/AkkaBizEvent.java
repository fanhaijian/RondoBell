package org.rondobell.akkalas;

/**
 * Created by zhuwp on 18/3/18.
 */
public enum AkkaBizEvent {
    AUDIO_ONLINE("audio_online"),//mz审核  
    AUDIO_OFFLINE("audio_offline"),//mz下线	
    AUDIO_DELETE("audio_delete"),//itings删除	
    AUDIO_ADD("audio_add"),
    
    ALBUM_ONLINE("album_online"),
    ALBUM_OFFLINE("album_offline"),
    ALBUM_DELETE("album_delete"),
    
    USER_BIND("user_bind"),
    USER_UNBIND("user_unbind"),
    
    SUBSCRIBE("subscribe"),//插入uid-audios 
    UN_SUBSCRIBE("un_subscribe"),//uid-audios
    
    KRADIO_SUBSCRIBE("kradio_subscribe"),
    KRADIO_UN_SUBSCRIBE("kradio_un_subscribe");
	
	private String value;
	
	AkkaBizEvent(String value){
		this.value=value;
	}
	

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static void main(String[] args) {
		AkkaBizEvent event = AkkaBizEvent.SUBSCRIBE;
		switch (event) {
		case AUDIO_ADD:
		    System.out.println("select " + "add");  
		    break;
		case SUBSCRIBE:
		    System.out.println("select " + "sub");  
		    break;
		case AUDIO_ONLINE:
		    System.out.println("select " + "on");  
		    break;
		default:  
		    System.out.println("select " + "unknow!!");  
		    break; 
		}
	}
}
