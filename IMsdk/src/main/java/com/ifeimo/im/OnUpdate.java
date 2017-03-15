package com.ifeimo.im;

/**
 * Created by lpds on 2017/1/17.
 */
public interface OnUpdate {

    /**
     * IEmployee的子类被ManagerList.add(IEmployee i)的后，如果IEmployee的子类实现的了此接口 OnUpdate。
     * 则 IM登陆成功后，会调用此方法。
     */
    void update();

}
