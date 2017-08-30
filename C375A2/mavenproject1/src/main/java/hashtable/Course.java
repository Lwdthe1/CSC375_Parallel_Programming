/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hashtable;

/**
 *
 * @author lwdthe1
 */
public class Course {
    public static volatile int courseId = 100;
    public static final int minCourseId = 100;
    public static final int maxCourseId = 150;
    public static String sp = "_";
    
    private String key;
    private volatile Object val;
    private int hash;

    public Course(String department, String key, String val) {
        if(key == null){
            int currentId = ++courseId;
            if(currentId > maxCourseId) courseId = minCourseId;
            this.key = department + sp + currentId;
            this.val = department + sp + currentId;
        } else {
            this.key = key;
            this.val = val;
        }
    }
    
    @Override
    public String toString(){
        String s = "";
        if(key != null) s += key.toString() + " : ";
        if(val != null) s += val.toString();
        else s += "null val";
        return s;
    }
    
    public static int getId() {
        return courseId;
    }

    public String getKey() {
        return key;
    }

    public Object getVal() {
        return val;
    }

    public int getHash() {
        return hash;
    }
    
    public void setHash(int hash) {
        this.hash = hash;
    }
}
