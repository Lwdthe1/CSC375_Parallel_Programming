package com.lincolnwdaniel.ltrain.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Lwdthe1 on 6/28/2015.
 * An object for storing key value pairs
 */
public class TouchObject implements Parcelable {
    public static final String KEY_ID = "object_id";
    private HashMap<String, Object> vars;
    protected int id;

    public TouchObject(){
        vars = new HashMap<>();
    }

    public TouchObject set(String key, Object val){ vars.put(key, val); return this;}
    public Object get(String key){ return vars.get(key); }

    public int getId() {
        return id;
    }

    public static String filterIntsOnly(String s) {
        return s.replaceAll("\\D+","");
    }

    protected TouchObject(Parcel in) {
        vars = (HashMap) in.readValue(HashMap.class.getClassLoader());
        id = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(vars);
        dest.writeInt(id);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TouchObject> CREATOR = new Parcelable.Creator<TouchObject>() {
        @Override
        public TouchObject createFromParcel(Parcel in) {
            return new TouchObject(in);
        }

        @Override
        public TouchObject[] newArray(int size) {
            return new TouchObject[size];
        }
    };

    public HashMap<String, Object> getVars() {
        return vars;
    }

    public Set<String> getKeys(){
        return getVars().keySet();
    }

    public Collection<Object> getValues(){
        return getVars().values();
    }

    public boolean containsKey(String key){
        return getVars().keySet().contains(key);
    }

    public boolean containsValue(String value){
        return getVars().values().contains(value);
    }
}