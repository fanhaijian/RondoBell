package org.rondobell.racailum.base.dto;

public class BroadcastStream {
    Integer id;
    Long broadcast_id;
    String stream;
    Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getBroadcast_id() {
        return broadcast_id;
    }

    public void setBroadcast_id(Long broadcast_id) {
        this.broadcast_id = broadcast_id;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
