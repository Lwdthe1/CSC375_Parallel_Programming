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
public class LHStudentThread extends Thread {
    private static final Random rand = new Random();
    private static int success = 0, failure = 0;
    private LHashTable courses;    
    
    public LHStudentThread(String name, LHashTable courses) {
        setName(name);
        this.courses = courses;
    }

    private void viewACourse() {
        final int deptRInt = LHashTable.nextInt(rand, 0, ChairThread.departments.size() - 1);
        final String courseDepartment = ChairThread.departments.get(deptRInt);
        final int courseId = LHashTable.nextInt(rand, 100, Course.courseId);
        
        final String courseCode = courseDepartment + Course.sp + courseId;
        
        if(courses.size() != 0){
            Course course = courses.get(courseCode);
            if(course != null){
                //System.out.println("\n#" + getName() + " VIEWED " + course + "\n");
                success++;
            } else {
                //System.out.println(getName() + " failed " + courseCode);
                failure++;
            }
        }
    }
    
    @Override
    public void run(){
        viewACourse();
    }
    
    
    public static String stats(){
        float successRate = (((float) success) / (success + failure)) * 100;
        return "LHStudents : Success Rate = " + String.format("%2.02f", successRate) + "%";
    }
    
}
