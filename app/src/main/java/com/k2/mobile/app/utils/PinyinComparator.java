package com.k2.mobile.app.utils;

import java.util.Comparator;

import com.k2.mobile.app.model.bean.Contacts;

public class PinyinComparator implements Comparator<Contacts> {

	@Override
	public int compare(Contacts o1, Contacts o2) {
		if (o1.getAlphabetical().equals("@") || o2.getAlphabetical().equals("#")) {
			return -1;
		} else if (o1.getAlphabetical().equals("#") || o2.getAlphabetical().equals("@")) {
			return 1;
		} else {
			return o1.getAlphabetical().compareTo(o2.getAlphabetical());
		}
	}

}
