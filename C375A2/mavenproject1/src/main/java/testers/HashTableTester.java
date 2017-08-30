/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testers;

import threads.LHStudentThread;
import threads.CHStudentThread;
import threads.ChairThread;
import threads.LHChairThread;
import threads.CHChairThread;
import hashtable.LHashTable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import threads.*;

/**
 *
 * @author lwdthe1
 */
public class HashTableTester {

    public static LHashTable mLHCourses;
    public static ConcurrentHashMap mCHCourses;
    private static List<LHChairThread> mLHChairThreads;
    private static List<CHChairThread> mCHChairThreads;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        mLHCourses = new LHashTable();
        mCHCourses = new ConcurrentHashMap();
        mLHChairThreads = new ArrayList<>();
        mCHChairThreads = new ArrayList<>();
        
        mLHChairThreads.add(new LHChairThread("Doug Lea", ChairThread.DEP_CS, mLHCourses));
        mLHChairThreads.add(new LHChairThread("Rachid Manseur", ChairThread.DEP_ECE, mLHCourses));
        mCHChairThreads.add(new CHChairThread("Scott Preston", ChairThread.DEP_MAT, mCHCourses));
        mCHChairThreads.add(new CHChairThread("Shashi Kanbur", ChairThread.DEP_PHY, mCHCourses));
        ExecutorService lhAdminExecutor = Executors.newFixedThreadPool(2);
        ExecutorService chAdminExecutor = Executors.newFixedThreadPool(4);
        ExecutorService studentExecutor = Executors.newFixedThreadPool(2);
        
        Random rand = new Random();
        runJobs(lhAdminExecutor, chAdminExecutor, studentExecutor, rand);

        lhAdminExecutor.shutdown();
        studentExecutor.shutdown();
        lhAdminExecutor.awaitTermination(1, TimeUnit.DAYS);
        studentExecutor.awaitTermination(1, TimeUnit.DAYS);
        System.out.println("\n\n\nThe day is over. Here are your results: \n\n");
        for(ChairThread admin: mLHChairThreads){
            System.out.println(admin);
        }
        System.out.println(LHStudentThread.stats());
        System.out.println(CHStudentThread.stats());
    }

    private static void runJobs(ExecutorService lhAdminExecutor, ExecutorService chAdminExecutor, ExecutorService studentExecutor, Random rand) {
        for(int i = 0; i < 200; i ++){
            final int lhNextInt = LHashTable.nextInt(rand, 0, mLHChairThreads.size() - 1);
            final int chNextInt = LHashTable.nextInt(rand, 0, mCHChairThreads.size() - 1);
            lhAdminExecutor.execute(mLHChairThreads.get(lhNextInt));
            //chAdminExecutor.execute(mCHChairThreads.get(chNextInt));

            final int nextInt = LHashTable.nextInt(rand, 0, (mLHChairThreads.size())  - 1);
            studentExecutor.execute(new LHStudentThread("LHStudent " + nextInt, mLHCourses));
            //studentExecutor.execute(new CHStudentThread("CHStudent " + nextInt, mCHCourses));


        }
    }
}
