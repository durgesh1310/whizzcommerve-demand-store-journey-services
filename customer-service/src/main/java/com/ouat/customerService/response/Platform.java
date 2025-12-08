package com.ouat.customerService.response;

import java.util.EnumSet;

public enum Platform {
	
	ANDROID, IOS, WEB, MWEB, MOBILE;
	
	public static boolean contains(String value) {
		try {
			return EnumSet.allOf(Platform.class).contains(Enum.valueOf(Platform.class, value));
		} catch (Exception e) {
			return false;
		}
	}

}
