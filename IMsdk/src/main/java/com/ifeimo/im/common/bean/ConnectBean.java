package com.ifeimo.im.common.bean;

/**
 * Created by lpds on 2017/1/11.
 * 连接参数
 */
public class ConnectBean {

    private String host;
    private int port;
    private String serviceName;

    public ConnectBean(String host, int port, String serviceName) {
        this.host = host;
        this.port = port;
        this.serviceName = serviceName;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
