package com.maxdemarzi;

import org.neo4j.graphdb.GraphDatabaseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BatchWriterServices {
    private static final ArrayList<BatchWriterService> writers = new ArrayList<>();
    private Random randomGenerator = new Random();

    public void SetGraphDatabase(GraphDatabaseService graphDb){
        if (writers.isEmpty()) {
            int processors = Runtime.getRuntime().availableProcessors();
            for (int i = 0; i < processors; i++) {
                writers.add(new BatchWriterService(graphDb));
            }
        }
    }

    public final static BatchWriterServices INSTANCE = new BatchWriterServices();

    private BatchWriterServices() {
    }

    public void put(List entries) throws InterruptedException {
        int queueId = randomGenerator.nextInt(writers.size());
        for (Object entry : entries) {
            writers.get(queueId).queue.put((HashMap)entry);
        }

    }

}
