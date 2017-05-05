package com.ifeimo.im.framwork;

import com.ifeimo.im.IEmployee;
import com.ifeimo.im.framwork.interface_im.IMMain;
import com.ifeimo.im.framwork.interface_im.IManagerList;
import com.ifeimo.im.framwork.interface_im.ILife;
import com.ifeimo.im.framwork.interface_im.IMWindow;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lpds on 2017/1/17.
 */
final class ManagerList implements IManagerList {

    static ManagerList managerList;

    HashSet<IEmployee> managers;

    static {
        managerList = new ManagerList();
    }

    private ManagerList() {
        managers = new HashSet<>();
    }

    public static IManagerList getInstances() {
        return managerList;
    }

    @Override
    public void addManager(IEmployee o) {
        if (!managers.contains(o)) {
            managers.add(o);
        }
    }

    @Override
    public Set<IEmployee> getAllManager() {

        Set<IEmployee> iEmployees = new HashSet<>();
        iEmployees.addAll(managers);

        return iEmployees;

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
