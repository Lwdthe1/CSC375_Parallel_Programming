/*
 * Hashtable class
 */
package hashtable;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author lwdthe1
 */
public class LHashTable {

    private List<String> words;
    public volatile List<LBucket> buckets;
    private volatile int numBuckets = 5;
    private volatile int size;

    public LHashTable() {
        buckets = new LinkedList<LBucket>();

        for (int i = 0; i < 5; i++) {
            LBucket newBucket = new LBucket();
            buckets.add(newBucket);
            //System.out.println("Initiated new  buckets in new hashtable.");
        }
        //System.out.println("Initiated " + buckets.size() + " buckets in new hashtable.");
    }

    public int numBuckets() {
        return buckets.size();
    }

    public int computeHash(String word) {
        int wordHash;
        int totalLettersNoSq = 0;
        int letterNo;
        int letterNoSq;

        int wordLength = word.length();

        for (int i = 0; i < wordLength; i++) {
            String currentLetter = word.substring(i, i + 1).toLowerCase();
            letterNo = "abcdefghijklmnopqrstuvwxyz".indexOf(currentLetter) + 1;
            letterNoSq = letterNo * letterNo;
            totalLettersNoSq += letterNoSq;
        }
        wordHash = (totalLettersNoSq) % numBuckets;
        //System.out.println("Word total letters index squared sum: "+totalLettersNoSq);
        //System.out.println("Word hash: "+wordHash);
        return wordHash;
    }

    /*public boolean addIfAbsent(Course course, Thread thread) {
        int wordHash = computeHash(course.getKey());
        LBucket bucket = buckets.get(wordHash);

        while (bucket.numWriters() > 0) {
            try {
                thread.wait(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(LHashTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            if (bucket.getLock().tryLock()) {
                bucket.incrementWriters();
                bucket.addToBucket(course);
                return true;
               
            } else {
                addIfAbsent(course, thread);
            }
        } finally {
            if (bucket.numWriters() > 0) {
                bucket.decrementWriters();
                bucket.getLock().unlock();
                
            }
        }
        return false;
    }*/
    
    public synchronized boolean addIfAbsent(Course course) {
        int wordHash = computeHash(course.getKey());
        LBucket bucket = buckets.get(wordHash);

        if(!bucket.contains(course)){
            bucket.addToBucket(course);
            size++;
            return true;
        }
        return false;
    }
    
    /*public boolean remove(String key) {
        int wordHash = computeHash(key);
        LBucket bucket = buckets.get(wordHash);
        
        Course course = get(key);
        return bucket.remove(course);
    }
    
    public boolean remove(Course course) {
        return remove(course.getKey());
    }*/

    public String displayAll() {
        String allContents = "";
        int count = 0;
        for (LBucket displayBucket : buckets) {
            System.out.println("\nBucket " + count + " {");
            displayBucket.displayAll();
            System.out.println("}");
            count++;
        }
        return allContents;
    }

    public boolean contains(String key) {
        int wordHash = computeHash(key);
        LBucket bucket = buckets.get(wordHash);
        if (bucket.contains(key)) {
            return true;
        }
        
        return false;
    }

    public boolean contains(Course course) {
        int wordHash = computeHash(course.getKey());
        LBucket bucket = buckets.get(wordHash);
        if (bucket.contains(course.getKey())) {
            return true;
        }
        
        return false;
    }

    public Course get(String key) {
        int wordHash = computeHash(key);
        LBucket bucket = buckets.get(wordHash);
        Course course = bucket.view(key);
        return course;
    }
    
    /*
    public Course get(String key) {
        for (int i = 0; i < buckets.size(); i++) {
            LBucket bucket = buckets.get(i);
            Course course = bucket.view(key);
            if(course != null){
                return course;
            }
        }
        return null;
    }
    */

    public int size() {
        return size;
    }
    
    public static int nextInt(Random rand, int min, int max){
       return rand.nextInt((max - min) + 1) + min;
    }    
}
