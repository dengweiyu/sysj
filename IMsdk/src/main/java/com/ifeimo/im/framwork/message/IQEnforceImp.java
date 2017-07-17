package com.ifeimo.im.framwork.message;

import com.ifeimo.im.framwork.commander.IQObserver;

import org.jivesoftware.smack.packet.IQ;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lpds on 2017/6/22.
 */
final class IQEnforceImp implements IQEnforce {


    Set<IQAffirm> affirmSet;

    IQEnforceImp(){
        affirmSet = new HashSet<>();
    }


    @Override
    public Set<IQAffirm> getAllAffirms() {
        return null;
    }

    public void onMessage(IQ stanza) {

    }

    void send(IQ iq,IQAffirm iqAffirm){

    }



}
