package com.maxdemarzi;

import org.neo4j.graphdb.GraphDatabaseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BatchWriterServices {
    private static final ArrayList<BatchWriterService> writers = new ArrayList<>();
    private Random randomGenerator = new Random();

    public void SetGraphDatabase(GraphDatabaseService graphDb){
        if (writers.isEmpty()) {
            int processors = 4; //Runtime.getRuntime().availableProcessors();
            for (int i = 0; i < (processors); i++) {
                writers.add(new BatchWriterService(graphDb));
            }
        }
    }

    public final static BatchWriterServices INSTANCE = new BatchWriterServices();

    private BatchWriterServices() {
    }

    public void put(HashMap entry) throws InterruptedException {
        writers.get(randomGenerator.nextInt(writers.size())).queue.put(entry);
    }

}
