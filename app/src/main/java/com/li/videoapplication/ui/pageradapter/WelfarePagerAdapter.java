package com.li.videoapplication.ui.pageradapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
/**
 * 适配器：福利
 */
public class WelfarePagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments;

	public WelfarePagerAdapter(FragmentManager manager, List<Fragment> fragments) {
		super(manager);
		this.fragments = fragments;
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}
	
    @Override  
    public Object instantiateItem(ViewGroup arg0, int arg1) {  
        return super.instantiateItem(arg0, arg1);  
    }  
      
    @Override  
    public void destroyItem(ViewGroup container, int position, Object object) { 
        super.destroyItem(container, position, object);  
    }  
}

