/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hashtable;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author lwdthe1
 */
public class LBucket extends LinkedList {
    public volatile LinkedList<Course> bucket;//says that the reader will see what the writers writes with a happens before  
    private final Lock lock = new ReentrantLock();
    private int writers;
    
    public LBucket(){
        bucket = new LinkedList<Course>();
    }
    
    public void addToBucket(Course course){
        bucket.add(course);
    }
    
    public boolean remove(Course course){
        return bucket.remove(course);
    }
    
    public Course view(String key){
        //do this to avoid cocurrent modification exception
        for (int i = 0; i < bucket.size(); i++) {
            Course course = bucket.get(i);
            if(key.equalsIgnoreCase(course.getKey())){
                //System.out.print(" [Found " + course + "] ");
                return course;
            }
        }
        return null;
    }
    
    public void displayAll(){
        for(Course course: bucket){
            System.out.print(course + " | ");
        }
    }
    
    public boolean contains(String key){
        for (Course course: bucket) {
            if(key.equalsIgnoreCase(course.getKey())){
                //System.out.print(" [Found " + course + "] ");
                return true;
            }
        }
        return false;
    }
    
    public boolean contains(Course c){
        for (int i = 0; i < bucket.size(); i++) {
            Course course = bucket.get(i);
            if(c.getKey().equalsIgnoreCase(course.getKey())){
                //System.out.print(" [Found " + course + "] ");
                return true;
            }
        }
        return false;
    }

    Lock getLock() {
       return lock;
    }

    void incrementWriters() {
        writers += 1;
    }
    
    void decrementWriters() {
        writers -= 1;
    }

    int numWriters() {
        return writers;
    }

    List<Course> getBucket() {
        return bucket;
    }
    
}
