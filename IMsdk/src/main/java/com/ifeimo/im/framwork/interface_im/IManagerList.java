package com.ifeimo.im.framwork.interface_im;

import com.ifeimo.im.IEmployee;
import com.ifeimo.im.framwork.interface_im.ILife;

import java.util.Set;

/**
 * 管理者所有员工的管理者
 * Created by lpds on 2017/2/13.
 */
public interface IManagerList extends ILife{

    void addManager(IEmployee o);

    Set<IEmployee> getAllManager();
}
