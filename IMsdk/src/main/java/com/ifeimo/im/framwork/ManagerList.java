package com.ifeimo.im.framwork;

import com.ifeimo.im.IEmployee;
import com.ifeimo.im.framwork.commander.HandlerMessageLeader;
import com.ifeimo.im.framwork.commander.IMMain;
import com.ifeimo.im.framwork.commander.IManagerList;
import com.ifeimo.im.framwork.commander.ILife;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lpds on 2017/1/17.
 */
final class ManagerList implements IManagerList {

    static ManagerList managerList;

    /**
     * 管理者队列
     */
    private Set<IEmployee> managers;

    /**
     * 处理xml消息的管理者队列
     * 实现此接口的 HandlerMessageLeader 类
     */
    private Set<HandlerMessageLeader> handlerMessageSet;

    static {
        managerList = new ManagerList();
    }

    private ManagerList() {
        managers = new HashSet<>();
        handlerMessageSet = new HashSet<>();
    }

    public static IManagerList getInstances() {
        return managerList;
    }

    @Override
    public void addManager(IEmployee o) {
        if (!managers.contains(o)) {
            managers.add(o);
            if(o instanceof HandlerMessageLeader){
                handlerMessageSet.add((HandlerMessageLeader) o);
            }
        }
    }

    @Override
    public Set<IEmployee> getAllManager() {

        Set<IEmployee> iEmployees = new HashSet<>();
        iEmployees.addAll(managers);

        return iEmployees;

    }

    /**
     * 获取管理者队列中某个类型的集合
     * @param c
     * @param <T>
     * @return
     */
    public <T> Set<T> getManagersByClass(Class<T> c) {
        synchronized (this) {
            Set<T> set = new HashSet<>();
            for (IEmployee iEmployee : managers) {
                if (c.isInstance(iEmployee)) {
                    set.add((T) iEmployee);
                }
            }
            return set;
        }
    }

    /**
     * 获的处理消息的一些管理类
     * @param tClass
     * @param <T>
     * @return
     */
    public <T extends HandlerMessageLeader> Set<T> getAllHandlerMessageLeader(Class<T> tClass){
//        synchronized (this) {
            Set<T> set = new HashSet<>();
            for (HandlerMessageLeader handlerMessageLeader : handlerMessageSet) {
                if (tClass.isInstance(handlerMessageLeader)) {
                    set.add((T) handlerMessageLeader);
                }
            }
            return set;
//        }
    }

    @Override
    public void onCreate(IMMain imWindow) {
        synchronized (this) {
            Set<IEmployee> managers = new HashSet<>(this.managers);
            for (IEmployee iEmployee : managers) {
                if (iEmployee instanceof ILife) {
                    ((ILife) iEmployee).onCreate(imWindow);
                }
            }
        }
    }

    @Override
    public void onResume(IMMain imWindow) {
        synchronized (this) {
            Set<IEmployee> managers = new HashSet<>(this.managers);
            for (IEmployee iEmployee : managers) {
                if (iEmployee instanceof ILife) {
                    ((ILife) iEmployee).onResume(imWindow);
                }
            }
        }
    }

    @Override
    public void onDestroy(IMMain imWindow) {
        synchronized (this) {
            Set<IEmployee> managers = new HashSet<>(this.managers);
            for (IEmployee iEmployee : managers) {
                if (iEmployee instanceof ILife) {
                    ((ILife) iEmployee).onDestroy(imWindow);
                }
            }
        }
    }

    @Override
    public void onPause(IMMain imWindow) {
        synchronized (this) {
            Set<IEmployee> managers = new HashSet<>(this.managers);
            for (IEmployee iEmployee : managers) {
                if (iEmployee instanceof ILife) {
                    ((ILife) iEmployee).onPause(imWindow);
                }
            }
        }
    }

    @Override
    public void onStop(IMMain imWindow) {
        synchronized (this) {
            Set<IEmployee> managers = new HashSet<>(this.managers);
            for (IEmployee iEmployee : managers) {
                if (iEmployee instanceof ILife) {
                    ((ILife) iEmployee).onStop(imWindow);
                }
            }
        }
    }
}
