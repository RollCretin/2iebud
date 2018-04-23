package com.hxjf.dubei.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

import org.json.JSONArray;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

public class SPUtils {
	private static final String FILENAME = "SPUtils";

	public static void putString(Context context,String key,String value){
		SharedPreferences sharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		Editor edit = sharedPreferences.edit();
		edit.putString(key, value);
		edit.commit();
	}

	public static String getString(Context context,String key,String defValue){
		if (context == null){
			return "";
		}
		SharedPreferences sharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		return sharedPreferences.getString(key, defValue);
	}

	public static void putBoolean(Context context,String key,boolean value){
		SharedPreferences sharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		Editor edit = sharedPreferences.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}

	public static int getInt(Context context,String key,int defValue){
		SharedPreferences sharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(key, defValue);
	}

	public static void putInt(Context context,String key,int value){
		SharedPreferences sharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		Editor edit = sharedPreferences.edit();
		edit.putInt(key, value);

		edit.commit();
	}

	public static boolean getBoolean(Context context,String key,boolean defValue){
		SharedPreferences sharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(key, defValue);
	}
	public static void putFloat(Context context,String key,float value){
		SharedPreferences sharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		Editor edit = sharedPreferences.edit();
		edit.putFloat(key, value);
		edit.commit();
	}

	public static float getFloat(Context context,String key,float defValue){
		SharedPreferences sharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		return sharedPreferences.getFloat(key, defValue);
	}

	public static SharedPreferences getSharedPreferences(Context context) {
		return context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
	}

	//存储bean
	public static void putBean(Context context, String key, Object obj) {
		if (obj instanceof Serializable) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(obj);
				String string64 = new String(Base64.encode(baos.toByteArray(),
						0));
				Editor editor = getSharedPreferences(context).edit();
				editor.putString(key, string64);
				editor.commit();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			throw new IllegalArgumentException(
					"the obj must implement Serializble");
		}

	}

	public static Object getBean(Context context, String key) {
		Object obj = null;
		try {
			String base64 = getSharedPreferences(context).getString(key, "");
			if (base64.equals("")) {
				return null;
			}
			byte[] base64Bytes = Base64.decode(base64.getBytes(), 1);
			ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			obj = ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	//存储int数组
	public static void putIntArray(Context context,String key,int[] array){
		SharedPreferences sharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		Editor edit = sharedPreferences.edit();
		JSONArray jsonArray = new JSONArray();
		for (int i :
				array) {
			jsonArray.put(i);

		}
		edit.putString(key,jsonArray.toString());
		edit.commit();
	}

	//读取int数组
	public static int[] getIntArray(Context context,String key,int arrayLength){
		SharedPreferences sharedPreferences = context.getSharedPreferences(FILENAME,Context.MODE_PRIVATE);
		int[] array = new int[arrayLength];
		Arrays.fill(array, -1);
		try {
			JSONArray jsonArray = new JSONArray(sharedPreferences.getString(key, "[]"));
			for (int i = 0; i < jsonArray.length(); i++) {
				array[i] = jsonArray.getInt(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return array;
	}

	public static boolean remove(Context context, String key){
		SharedPreferences sp = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		return edit.remove(key).commit();
	}
}
