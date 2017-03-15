package com.ifeimo.im.common.bean;

import com.zhy.http.okhttp.request.RequestCall;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by lpds on 2017/2/13.
 */
public class RequestTag {

    private Map<String,RequestCall> map;
    private String tag;

    public RequestTag(String tag){
        this.tag = tag;
        map  = new HashMap<>();
    }

    public void put(String url ,RequestCall call){
        remove(url);
        map.put(url,call);
    }

    public void remove(String url){
        if(map.containsKey(url)){
            map.get(url).cancel();
        }
    }

    public Set<String> keySet(){
        return map.keySet();
    }

}
