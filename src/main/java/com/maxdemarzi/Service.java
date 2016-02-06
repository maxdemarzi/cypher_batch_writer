package com.maxdemarzi;

import org.neo4j.graphdb.GraphDatabaseService;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Path("/service")
public class Service {
    private static final BatchWriterServices batchWriterServices = BatchWriterServices.INSTANCE;

    public Service(@Context GraphDatabaseService graphdb){
        batchWriterServices.SetGraphDatabase(graphdb);
    }

    @POST
    @Path("/batch")
    public Response batchWrites(String body, @Context GraphDatabaseService db) throws IOException, InterruptedException {

        // Validate our input or exit right away
        HashMap input = Validators.getValidCypherStatements(body);
        batchWriterServices.put((List)input.get("statements"));

        return Response.ok().build();
    }

}
