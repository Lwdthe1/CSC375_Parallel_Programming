package com.lincolnwdaniel.ltrain.utils;

/**
 * Created by Lwdthe1 on 4/14/2015.
 */
public class LString {
    /**
     * Formats the provided strings
     * @param msg
     * @param args
     * @return formatted version of provided string and its arguments
     */
    public static String fS(String msg, Object... args) {
        return String.format(msg, args);
    }

    public static String capitalize(String string) {
        if(string.length()<1) return "";
        else if(string.length()>1) return string.substring(0,1).toUpperCase()+string.substring(1);
        else return string.toUpperCase();
    }
}
