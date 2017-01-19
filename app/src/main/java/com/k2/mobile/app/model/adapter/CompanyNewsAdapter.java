package com.k2.mobile.app.model.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @Title CompanyNewsAdapter.java
 * @Package com.oppo.mo.model.adapter
 * @Description 适配公司新闻
 * @Company  K2
 * 
 * @author liangzy
 * @date 2015-03-18 20:43:00
 * @version V1.0
 */
public class CompanyNewsAdapter extends FragmentPagerAdapter {
	private List<Fragment> fragmentList;

	public CompanyNewsAdapter(FragmentManager fm, List<Fragment> fragmentList) {
		super(fm);
		this.fragmentList = fragmentList;
	}

	/**
	 * 得到每个页面
	 */
	@Override
	public Fragment getItem(int position) {
		return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(position);
	}

	/**
	 * 每个页面的title
	 */
	@Override
	public CharSequence getPageTitle(int position) {
		return null;
	}

	/**
	 * 页面的总个数
	 */
	@Override
	public int getCount() {
		return fragmentList == null ? 0 : fragmentList.size();
	}
}
