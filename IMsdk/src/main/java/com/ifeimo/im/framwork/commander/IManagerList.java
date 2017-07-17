package com.ifeimo.im.framwork.commander;

import com.ifeimo.im.IEmployee;

import java.util.Set;

/**
 * 管理者所有员工的管理者
 * Created by lpds on 2017/2/13.
 */
public interface IManagerList extends ILife{

    void addManager(IEmployee o);

    /**
     * versioncode 1.4 之后 不建议使用
     * @return
     */
    @Deprecated
    Set<IEmployee> getAllManager();

    <T> Set<T> getManagersByClass(Class<T> c);

}
