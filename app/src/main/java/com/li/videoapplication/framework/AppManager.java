package com.li.videoapplication.framework;

import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.li.videoapplication.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

/**
 * 
 * 功能：全局管理Application/Activiy/Service的实例
 * 用法：1.在BaseActivity中添加和移除实例  2.在BaseApplication中添加实例
 * 
 */
public class AppManager {

    protected final String action = this.getClass().getName();
    protected final String tag = this.getClass().getSimpleName();

	private AppManager() {
		super();
        if (null == activities) {
            activities = new Stack<>();
        }
        if (null == services) {
            services = new Vector<>();
        }
        if (null == views) {
            views = new HashSet<>();
        }
	}

	private static AppManager instance;

	/*单一实例*/
	public static AppManager getInstance() {
		if (null == instance) {
			instance = new AppManager();
		}
		return instance;
	}
	
	/*############ 应用程序 ##############*/
	
	private BaseApplication application;
	
	private Context context;

	public void setApplication(BaseApplication application) {
		this.application = application;
		setContext(application.getApplicationContext());
	}

	public BaseApplication getApplication() {
		return application;
	}
	
	public Context getContext() {
		return context;
	}

	private void setContext(Context context) {
		this.context = context;
	}

    public Resources getResources() {
        return context.getResources();
    }
	
	/*################ 主页面 #############*/

    private MainActivity mainActivity;

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void removeMainActivity() {
        mainActivity = null;
    }

    /*################ 活动 #############*/

	private Stack<FragmentActivity> activities;
	
	/**
	 * 功能：添加Activity到堆
	 */
	public void addActivity(final FragmentActivity activity) {
		synchronized (instance) {
			Log.i(tag, "[addActivity]" + activity);
			activities.push(activity);
			printAllActivity();
		}
	}

	/**
	 * 功能：获取当前Activity，堆栈中后一个压入的
	 */
	public FragmentActivity currentActivity() {
		synchronized (instance) {
            FragmentActivity activity = null;
            if (!activities.isEmpty())
			    activity = activities.peek();
			Log.i(tag, "[currentActivity]" + activity);
			return activity;
		}
	}

	/**
	 * 功能：结束当前Activity，堆栈中后一个压入的
	 */
	public void removeCurrentActivity() {
		synchronized (instance) {
            FragmentActivity activity = null;
            if (!activities.isEmpty())
                activity = activities.pop();
			Log.i(tag, "[finishActivity]" + activity);
			removeActivity(activity);
			printAllActivity();
		}
	}

	/**
	 * 功能：结束指定的Activity
	 */
	public void removeActivity(FragmentActivity activity) {
		synchronized (instance) {
			Log.i(tag, "[finishActivity]" + activity);
			if (activity != null) {
				activity.finish();
				activities.remove(activity);
			}
			printAllActivity();
		}
	}

	/**
	 * 功能：结束指定类名的Activity
	 */
	public void removeActivity(final Class<?> cls) {
		synchronized (instance) {
			Log.i(tag, "[finishActivity]" + cls.getSimpleName());
			for (FragmentActivity activity : activities) {
				if (activity.getClass().equals(cls)) {
					removeActivity(activity);
				}
			}
			printAllActivity();
		}
	}

	/**
	 * 功能：结束指定类名的Activity[]
	 */
	public void removeActivity(final Class<?>[] clss) {
		synchronized (instance) {
			for (int i = 0; i < clss.length; i++) {
				Log.i(tag, "[finishActivity]" + clss[i].getSimpleName());
			}
			for (int i = 0; i < clss.length; i++) {
				Class<?> cls = clss[i];
				for (FragmentActivity activity : activities) {
					if (activity.getClass().equals(cls)) {
						removeActivity(activity);
					}
				}
			}
			printAllActivity();
		}
	}

	/**
	 * 功能：判断当前的activity是否存在
	 */
	public boolean isActivityAliive(final Class<?> cls) {
		synchronized (instance) {
			Log.i(tag, "[isActivityAliive]" + cls.getSimpleName());
			for (FragmentActivity activity : activities) {
				if (activity.getClass().equals(cls)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * 功能：获得对应的activity
	 */
	public FragmentActivity getActivity(final Class<?> cls) {
		synchronized (instance) {
			Log.i(tag, "[getActivity]" + cls.getSimpleName());
			for (FragmentActivity activity : activities) {
				if (activity.getClass().equals(cls)) {
					return activity;
				}
			}
			return null;
		}
	}

	/**
	 * 功能：结束所有Activity，底部跳转的时候每次点击都会把前面的activity全部清除
	 */
	public void removeAllActivity() {
		synchronized (instance) {
			int size = activities.size();
			Log.i(tag, "[finishAllActivity]" + size);
			for (int i = 0; i < size; i++) {
				if (null != activities.get(i)) {
					activities.get(i).finish();
				}
			}
			activities.clear();
			printAllActivity();
		}
	}

	/**
	 * 功能：打印所有Activity
	 */
	public void printAllActivity() {
		if (activities != null && activities.size() > 0) {
			Log.i(tag, "[printAllActivity]" + "------------------------------");
			for (int i = 0; i < activities.size(); i++) {
				Log.i(tag, "[printAllActivity]" + activities.get(i));
			}
			Log.i(tag, "[printAllActivity]" + "------------------------------");
		}
	}

	/**
     * 功能：应用程序退出
     */
    public void exit() {
    	synchronized (instance) {
            try {
                removeAllActivity();
                Runtime.getRuntime().exit(0);
            } catch (Exception e) {
                Runtime.getRuntime().exit(-1);
            }
		}
    }

	/*################ 服务 #############*/

    private Vector<BaseService> services;

    /**
     * 功能：添加Service到集合
     */
    public void addService(final BaseService service) {
        synchronized (instance) {
            if (service != null) {
                Log.i(tag, "[addService]" + service.getClass().getSimpleName());
                services.add(service);
                printAllService();
            }
        }
    }

    /**
     * 功能：结束指定的Service
     */
    public void removeService(final BaseService service) {
        synchronized (instance) {
            if (service != null) {
                Log.i(tag, "[removeService]" + service.getClass().getSimpleName());
                services.remove(service);
            }
            printAllService();
        }
    }

    /**
     * 功能：结束指定类名的Service
     */
    public void removeService(final Class<?> cls) {
        synchronized (instance) {
            if (cls != null) {
                Log.i(tag, "[removeService]" + cls.getSimpleName());
                for (BaseService service : services) {
                    if (service.getClass().equals(cls)) {
                        removeService(service);
                    }
                }
                printAllService();
            }
        }
    }

    /**
     * 功能：获得对应的Service
     */
    public Service getService(final Class<?> cls) {
        synchronized (instance) {
            if (cls != null) {
                Log.i(tag, "[getService]" + cls.getSimpleName());
                for (BaseService service : services) {
                    if (service.getClass().equals(cls)) {
                        return service;
                    }
                }
            }
            return null;
        }
    }

    /**
     * 功能：打印所有Service
     */
    public void printAllService() {
        if (services != null && services.size() > 0) {
            Log.d(tag, "[printAllService]" + "------------------------------");
            for (int i = 0; i < services.size(); i++) {
                Log.i(tag, "[printAllService]" + services.get(i));
            }
            Log.i(tag, "[printAllService]" + "------------------------------");
        }
    }

	/*################ 视图 #############*/

    private Set<View> views;

    /**
     * 功能：添加View到集合
     */
    public void addView(final View view) {
        synchronized (instance) {
            if (view != null) {
                Log.i(tag, "[addView]" + view.getClass().getSimpleName());
                views.add(view);
                printAllView();
            }
        }
    }

    /**
     * 功能：结束指定的View
     */
    public void removeView(final View view) {
        synchronized (instance) {
            if (view != null) {
                Log.i(tag, "[removeView]" + view.getClass().getSimpleName());
                views.remove(view);
            }
            printAllView();
        }
    }

    /**
     * 功能：结束指定类名的View
     */
    public void removeView(final Class<?> cls) {
        synchronized (instance) {
            if (cls != null) {
                Log.i(tag, "[removeView]" + cls.getSimpleName());
                for (View view : views) {
                    if (view.getClass().equals(cls)) {
                        removeView(view);
                    }
                }
                printAllView();
            }
        }
    }

    /**
     * 功能：获得对应的View
     */
    public View getView(final Class<?> cls) {
        synchronized (instance) {
            if (cls != null) {
                Log.i(tag, "[getView]" + cls.getSimpleName());
                for (View view : views) {
                    if (view.getClass().equals(cls)) {
                        return view;
                    }
                }
            }
            return null;
        }
    }

    /**
     * 功能：获得对应的View
     */
    public List<View> getViews(final Class<?> cls) {
        synchronized (instance) {
            if (cls != null) {
                Log.i(tag, "[getViews]" + cls.getSimpleName());
                List<View> views = new ArrayList<View>();
                for (View view : this.views) {
                    if (view.getClass().equals(cls)) {
                        views.add(view);
                    }
                }
                return views;
            }
            return null;
        }
    }

    /**
     * 功能：打印所有Views
     */
    public void printAllView() {
        if (views != null && views.size() > 0) {
            Log.i(tag, "[printAllView]" + "------------------------------");
            for (View view: views) {
                Log.i(tag, "[printAllView]" + view);
            }
            Log.d(tag, "[printAllView]" + "------------------------------");
        }
    }
}