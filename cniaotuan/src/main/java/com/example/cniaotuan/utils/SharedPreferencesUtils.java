package com.example.cniaotuan.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 临时存储工具类
 */
public class SharedPreferencesUtils {
    private static Context mContext;
    private static SharedPreferences mUser;
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_date";
    public static void init(Context context){
        mContext = context;
        mUser = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
    }


//    public static  String getString(String key){
//        return mUser.getString(key, "");
//    }
//
//    public static void setString(String key,String value){
//        mUser.edit().putString(key, value).commit();
//    }
//    /** 通过JsonArray 追加数据到Shared */
//    public static void insertStringArray(String key,String value){
//        List<String> data = getStringArray(key);
//        for (int i = 0; i < data.size(); i++) {
//            if(value.equals(data.get(i))){
//                return;
//            }
//        }
//        data.add(value);
//        Collections.reverse(data);
//    }
//
//    /** 通过JsonArrray 读取数据*/
//    public static List<String> getStringArray(String key)
//    {
//        ArrayList<String> result = new ArrayList<String>();
//        JSONArray jsonArray;
//        try {
//            jsonArray = new JSONArray(mUser.getString(key, "[]"));
//            for (int i = 0; i < jsonArray.length(); i++){
//                result.add(jsonArray.getString(i));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    /* 保存当前登录帐号密码信息,用于自动登录 */
//    public static void setCurrentPerson(MyUser person){
//        ObjectOutputStream oos = null;
//        try {
//            oos = new ObjectOutputStream(mContext.openFileOutput("person", Context.MODE_PRIVATE));
//            oos.writeObject(person);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if(oos!=null){
//                try {
//                    oos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//    /* 读取当前登录帐号信息,用户自动登录 */
//    public static MyUser getCurrentPerson(){
//        MyUser person = null;
//        ObjectInputStream ois = null;
//        try {
//            person = (MyUser) new ObjectInputStream(mContext.openFileInput("person")).readObject();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return person;
//    }
//    /* 删除当前登录帐号信息 */
//    public static void clearCurrentPerson(){
//        mContext.deleteFile("person");
//    }


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context , String key, Object object){

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if("String".equals(type)){
            editor.putString(key, (String)object);
        }
        else if("Integer".equals(type)){
            editor.putInt(key, (Integer)object);
        }
        else if("Boolean".equals(type)){
            editor.putBoolean(key, (Boolean)object);
        }
        else if("Float".equals(type)){
            editor.putFloat(key, (Float)object);
        }
        else if("Long".equals(type)){
            editor.putLong(key, (Long)object);
        }

        editor.commit();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context , String key, Object defaultObject){
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if("String".equals(type)){
            return sp.getString(key, (String)defaultObject);
        }
        else if("Integer".equals(type)){
            return sp.getInt(key, (Integer)defaultObject);
        }
        else if("Boolean".equals(type)){
            return sp.getBoolean(key, (Boolean)defaultObject);
        }
        else if("Float".equals(type)){
            return sp.getFloat(key, (Float)defaultObject);
        }
        else if("Long".equals(type)){
            return sp.getLong(key, (Long)defaultObject);
        }

        return null;
    }


}
