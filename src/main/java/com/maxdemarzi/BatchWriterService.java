package com.maxdemarzi;

import com.google.common.util.concurrent.AbstractScheduledService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class BatchWriterService extends AbstractScheduledService {
    private static final Logger logger = Logger.getLogger(BatchWriterService.class.getName());
    private GraphDatabaseService graphDb;
    public LinkedBlockingQueue<HashMap<String, Object>> queue = new LinkedBlockingQueue<>();

    public BatchWriterService(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
        if (!this.isRunning()){
            logger.info("Starting BatchWriterService");
            this.startAsync();
            this.awaitRunning();
            logger.info("Started BatchWriterService");
        }
    }

    @Override
    protected void runOneIteration() throws Exception {
        long startTime = System.nanoTime();
        long transactionTime = System.nanoTime();
        Collection<HashMap<String, Object>> writes = new ArrayList<>();

        queue.drainTo(writes);

        if(!writes.isEmpty()){
            int i = 0;
            Transaction tx = graphDb.beginTx();
            try {
                for( HashMap write : writes){
                    try {
                        i++;
                         graphDb.execute((String)write.get("statement"), (Map)write.getOrDefault("parameters", new HashMap<>()));
                    } catch (Exception exception) {
                        logger.severe("Error Executing Cypher Statement: " + write);
                    }

                    if(i % 1_000 == 0){
                        tx.success();
                        tx.close();
                        //DateTime currently = new DateTime();
                        //System.out.printf("Performed a transaction of 1,000 writes in  %d [msec] @ %s \n", (System.nanoTime() - transactionTime) / 1000000, currently.toDateTimeISO());
                        //transactionTime = System.nanoTime();
                        tx = graphDb.beginTx();
                    }
                }

                tx.success();
            } finally {
                tx.close();
                //DateTime currently = new DateTime();
                //System.out.printf("Performed a set of transactions with %d writes in  %d [msec] @ %s \n", writes.size(), (System.nanoTime() - startTime) / 1000000, currently.toDateTimeISO());
            }
        }

    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedRateSchedule(0, 1, TimeUnit.SECONDS);
    }

}
