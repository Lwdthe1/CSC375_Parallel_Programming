/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import hashtable.Course;
import hashtable.LHashTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author lwdthe1
 */
public class ChairThread implements Runnable {
    public static final Random rand = new Random();

    public static final String DEP_CS = "CSC";
    public static final String DEP_MAT = "MAT";
    public static final String DEP_PHY = "PHY";
    public static final String DEP_ECE = "ECE";
    public static final List<String> departments = new ArrayList<>(Arrays.asList(
            DEP_CS, DEP_ECE/*, DEP_MAT, DEP_PHY*/));

    protected String name;

    
    protected String department;
    protected LHashTable courses;
    protected int success = 0, failure = 0;
    protected int removeSuccess = 0;


    public ChairThread() {
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    
    @Override
    public void run() {
        
    }


}
