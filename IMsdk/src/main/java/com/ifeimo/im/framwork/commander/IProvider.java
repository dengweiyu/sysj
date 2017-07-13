package com.ifeimo.im.framwork.commander;

import com.ifeimo.im.IEmployee;
import com.ifeimo.im.OnOutIM;
import com.ifeimo.im.OnUpdate;
import com.ifeimo.im.framwork.connect.MemberInfoObserver;

/**
 * Created by lpds on 2017/4/26.
 */
public interface IProvider extends IEmployee,OnOutIM,OnUpdate,MemberInfoObserver{}
