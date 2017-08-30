/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import hashtable.Course;
import hashtable.LHashTable;
import java.util.Random;

/**
 *
 * @author lwdthe1
 */
public class LHChairThread extends ChairThread {
    private static final Random rand = new Random();
    private LHashTable courses;
    
    
    public LHChairThread(String name, String department, LHashTable courses) {
        setName(name);
        this.department = department;
        this.courses = courses;
    }   
    
    public String getDepartment() {
        return department;
    }
    
    /*public void deleteExistingCourse(){        
        final int courseId = LHashTable.nextInt(rand, Course.minCourseId, Course.maxCourseId);
        final String courseCode = department + Course.sp + courseId;

        if(courses.remove(courseCode)){
            //System.out.println("#LH"+ getName() + " REMOVED " + courseCode);
            removeSuccess++;
        }
        
    }*/
    
    public void addNewCourse(){
        final Course newCourse = new Course(department, null, null);
        //System.out.println(getName() + " trying to add " + newCourse);
        if(courses.addIfAbsent(newCourse)){
            //System.out.println("#LH"+ getName() + " ADDED " + newCourse);
            success++;
        } else {
            //System.out.println("LH"+ getName() + " failed " + newCourse);
            failure++;
        }
    }
    
    @Override
    public String toString(){
        float successRate = (((float) success) / (success + failure)) * 100;
        return "LH"+ getName() + ": Add Success Rate = " + String.format("%2.02f", successRate) + "%";
    }
    
    @Override
    public void run(){
        addNewCourse();
        /*if(LHashTable.nextInt(rand, 0, 3) == 0){
            deleteExistingCourse();
        }*/
    }
}
