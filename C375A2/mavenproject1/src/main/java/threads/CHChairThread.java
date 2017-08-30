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
public class CHChairThread extends ChairThread {
    private ConcurrentHashMap<String, Course> courses;


    public CHChairThread(String name, String department, ConcurrentHashMap courses) {
        setName(name);
        this.department = department;
        this.courses = courses;
    }   
    
    public String getDepartment() {
        return department;
    }
    
    public synchronized void addNewCourse(){
        final String courseCode = department + Course.sp + Course.courseId;
        //System.out.println(getName() + " trying to add " + newCourse);
        Course course = courses.computeIfAbsent(courseCode,
                n -> new Course(department, null, null));
        if(course != null){
            //System.out.println("#CH"+ getName() + " ADDED " + course);
            success++;
        } else {
            //System.out.println("CH"+ getName() + " failed ");
            failure++;
        }
    }
    
    @Override
    public String toString(){
        float successRate = (((float) success) / (success + failure)) * 100;
        return "CH" + getName() + ": Add Success Rate = " + String.format("%2.02f", successRate) + "%";
    }
    
    @Override
    public void run(){
        addNewCourse();
    }
}
