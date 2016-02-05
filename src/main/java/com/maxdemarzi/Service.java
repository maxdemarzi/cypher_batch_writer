package com.maxdemarzi;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.GraphDatabaseService;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Path("/service")
public class Service {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final BatchWriterService batchWriterService = BatchWriterService.INSTANCE;

    public Service(@Context GraphDatabaseService graphdb){
        batchWriterService.SetGraphDatabase(graphdb);
    }

    public Response asyncWrites(String body, @Context GraphDatabaseService db) throws IOException, InterruptedException {

        // Validate our input or exit right away
        HashMap input = Validators.getValidCypherStatements(body);
        for (Object entry : (List)input.get("statements")) {
            batchWriterService.queue.put((HashMap)entry);
        }

        return Response.ok().build();
    }

}
