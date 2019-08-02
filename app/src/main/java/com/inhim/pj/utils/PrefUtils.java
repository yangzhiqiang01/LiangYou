package com.inhim.pj.utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.inhim.pj.app.MyApplication;


/**
 * SharedPreferences工具类
 * @author axxui
 *
 */
public class PrefUtils {

	public static void putBoolean(String key, boolean value){
		SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
		
	}
	
	public static boolean getBoolean(String key, boolean defvalue){
		SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getBoolean(key, defvalue);
	}
	public static void putLong(String key, long value){
		SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putLong(key, value).commit();

	}

	public static long getLong(String key, long defvalue){
		SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getLong(key, defvalue);
	}


	public static void putString(String key, String value){
		SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
		
	}
	
	public static String getString(String key, String defvalue){
		SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getString(key, defvalue);
	}
	
	public static void putInt(String key, int value){
		SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();
		
	}
	
	public static int getInt(String key, int defvalue){
		SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getInt(key, defvalue);
	}

	public static void remove(String key){
		SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().remove(key).commit();
	}
}
