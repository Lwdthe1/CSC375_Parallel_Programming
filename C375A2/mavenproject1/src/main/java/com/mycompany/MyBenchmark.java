/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.mycompany;

import hashtable.LHashTable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import static testers.HashTableTester.mCHCourses;
import static testers.HashTableTester.mLHCourses;
import threads.CHChairThread;
import threads.CHStudentThread;
import threads.ChairThread;
import threads.LHChairThread;
import threads.LHStudentThread;

public class MyBenchmark {
    
    public static LHashTable mLHCourses;
    public static ConcurrentHashMap mCHCourses;
    private static List<LHChairThread> mLHChairThreads;
    private static List<CHChairThread> mCHChairThreads;

    @Benchmark
    public void testMethod() throws InterruptedException {
        // This is a demo/sample template for building your JMH benchmarks. Edit as needed.
        // Put your benchmark code here.
        mLHCourses = new LHashTable();
        mCHCourses = new ConcurrentHashMap();
        mLHChairThreads = new ArrayList<>();
        mCHChairThreads = new ArrayList<>();
        
        mLHChairThreads.add(new LHChairThread("Doug Lea", ChairThread.DEP_CS, mLHCourses));
        mLHChairThreads.add(new LHChairThread("Rachid Manseur", ChairThread.DEP_ECE, mLHCourses));
        mCHChairThreads.add(new CHChairThread("Scott Preston", ChairThread.DEP_MAT, mCHCourses));
        mCHChairThreads.add(new CHChairThread("Shashi Kanbur", ChairThread.DEP_PHY, mCHCourses));
        ExecutorService lhAdminExecutor = Executors.newFixedThreadPool(2);
        ExecutorService studentExecutor = Executors.newFixedThreadPool(2);
        
        Random rand = new Random();
        lhRunJobs(lhAdminExecutor, studentExecutor, rand);

        lhAdminExecutor.shutdown();
        studentExecutor.shutdown();
        lhAdminExecutor.awaitTermination(1, TimeUnit.DAYS);
        studentExecutor.awaitTermination(1, TimeUnit.DAYS);
        //System.out.println("\n\n\nThe day is over. Here are your results: \n\n");
        /*for(ChairThread admin: mLHChairThreads){
            System.out.println(admin);
        }*/
        
    }
    
    private static void lhRunJobs(ExecutorService lhAdminExecutor, ExecutorService studentExecutor, Random rand) throws InterruptedException {
        for(int i = 0; i < 5; i ++){
            final int lhNextInt = LHashTable.nextInt(rand, 0, mLHChairThreads.size() - 1);
            lhAdminExecutor.execute(mLHChairThreads.get(lhNextInt));
        }
        for(int i = 0; i < 10; i++){
            final int nextInt = LHashTable.nextInt(rand, 0, (mCHChairThreads.size())  - 1);
            studentExecutor.execute(new LHStudentThread("LHStudent " + nextInt, mLHCourses));
        }
    }
    
    
    /*@Benchmark
    public void testMethod() throws InterruptedException {
        // This is a demo/sample template for building your JMH benchmarks. Edit as needed.
        // Put your benchmark code here.
        mLHCourses = new LHashTable();
        mCHCourses = new ConcurrentHashMap();
        mLHChairThreads = new ArrayList<>();
        mCHChairThreads = new ArrayList<>();
        
        mLHChairThreads.add(new LHChairThread("Doug Lea", ChairThread.DEP_CS, mLHCourses));
        mLHChairThreads.add(new LHChairThread("Rachid Manseur", ChairThread.DEP_ECE, mLHCourses));
        mCHChairThreads.add(new CHChairThread("Doug Lea", ChairThread.DEP_CS, mCHCourses));
        mCHChairThreads.add(new CHChairThread("Rachid Manseur", ChairThread.DEP_ECE, mCHCourses));

        ExecutorService chAdminExecutor = Executors.newFixedThreadPool(4, (Runnable r) -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        });
        
        ExecutorService chStudentExecutor = Executors.newFixedThreadPool(10, (Runnable r) -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        });
        
        Random rand = new Random();
        //lhRunJobs(lhAdminExecutor, lhStudentExecutor, rand);
        chRunJobs(chAdminExecutor, chStudentExecutor, rand);

        chAdminExecutor.shutdown();
        chStudentExecutor.shutdown();
        chAdminExecutor.awaitTermination(1, TimeUnit.DAYS);
        chStudentExecutor.awaitTermination(1, TimeUnit.DAYS);
        //System.out.println("\n\n\nThe day is over. Here are your results: \n\n");
        /*for(ChairThread admin: mLHChairThreads){
            System.out.println(admin);
        }
        System.out.println(LHStudentThread.stats());
        System.out.println(CHStudentThread.stats());*/
    /*}
    
    
    private static void chRunJobs(ExecutorService adminExecutor, ExecutorService studentExecutor, Random rand) throws InterruptedException {
        for(int i = 0; i < 5; i ++){
            final int chNextInt = LHashTable.nextInt(rand, 0, mCHChairThreads.size() - 1);
            final CHChairThread chairThread = mCHChairThreads.get(chNextInt);
            
            adminExecutor.execute(() -> chairThread.addNewCourse());
        }
        
        for(int i = 0; i < 10; i ++){
            final int nextInt = LHashTable.nextInt(rand, 0, (mCHChairThreads.size())  - 1);
            final CHStudentThread chStudentThread = new CHStudentThread("CHStudent " + nextInt, mCHCourses);
            chStudentThread.setDaemon(true);
            
            studentExecutor.execute(chStudentThread);
        }
    }*/
    
}
