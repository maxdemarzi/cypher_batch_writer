package com.maxdemarzi;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class ServiceTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static GraphDatabaseService graphDatabaseService;
    private static Service service;

    @Before
    public void setUp() {
        graphDatabaseService = new TestGraphDatabaseFactory().newImpermanentDatabase();
        service = new Service(graphDatabaseService);
    }

    @After
    public void tearDown() throws Exception {
        graphDatabaseService.shutdown();
    }

    @Test
    public void shouldExecuteCypherBatched() throws IOException, InterruptedException {
        Random rn = new Random();
        for(int i = 0; i < 5_000; i++){
            ArrayList<HashMap<String,Object>> statements =  new ArrayList<>();
            HashMap<String,Object> entry = new HashMap<>();
            entry.put("statement", "CREATE (u:User {user_id: {user_id} })");
            HashMap<String,Object> parameter = new HashMap<>();
            parameter.put("user_id", rn.nextInt()  );
            entry.put("parameters", parameter );
            statements.add(entry);
            HashMap cypher = new HashMap();
            cypher.put("statements", statements);

            Response response = service.batchWrites(objectMapper.writeValueAsString(cypher), graphDatabaseService);
            assertEquals(200, response.getStatus());
        }
        Thread.sleep(2000);
    }

    @Test
    public void shouldExecuteMoreThanOneCypherStatement() throws IOException, InterruptedException {
        Random rn = new Random();
        ArrayList<HashMap<String,Object>> statements =  new ArrayList<>();
        HashMap<String,Object> entry = new HashMap<>();
        entry.put("statement", "CREATE (u:User {user_id: {user_id} })");
        HashMap<String,Object> parameter = new HashMap<>();
        parameter.put("user_id", rn.nextInt()  );
        entry.put("parameters", parameter );
        statements.add(entry);
        statements.add(entry);
        HashMap cypher = new HashMap();
        cypher.put("statements", statements);

        Response response = service.batchWrites(objectMapper.writeValueAsString(cypher), graphDatabaseService);
        assertEquals(200, response.getStatus());

        Thread.sleep(2000);
    }

    @Test
    public void shouldNotExecuteBadCypherStatement() throws IOException, InterruptedException {
        Random rn = new Random();
        ArrayList<HashMap<String,Object>> statements =  new ArrayList<>();
        HashMap<String,Object> entry = new HashMap<>();
        entry.put("statement", "ETAERC (u:User {user_id: {user_id} })");
        HashMap<String,Object> parameter = new HashMap<>();
        parameter.put("user_id", rn.nextInt()  );
        entry.put("parameters", parameter );
        statements.add(entry);
        HashMap cypher = new HashMap();
        cypher.put("statements", statements);

        Response response = service.batchWrites(objectMapper.writeValueAsString(cypher), graphDatabaseService);
        assertEquals(200, response.getStatus());

        Thread.sleep(2000);
    }

    @Test
    public void shouldExecuteGoodAndSkipBadCypherStatement() throws IOException, InterruptedException {
        Random rn = new Random();
        ArrayList<HashMap<String,Object>> statements =  new ArrayList<>();
        HashMap<String,Object> entry = new HashMap<>();
        entry.put("statement", "ETAERC (u:User {user_id: {user_id} })");
        HashMap<String,Object> parameter = new HashMap<>();
        parameter.put("user_id", rn.nextInt()  );
        entry.put("parameters", parameter );
        statements.add(entry);

        HashMap<String,Object> entry2 = new HashMap<>();
        entry2.put("statement", "CREATE (u:User {user_id: {user_id} })");
        HashMap<String,Object> parameter2 = new HashMap<>();
        parameter2.put("user_id", rn.nextInt()  );
        entry2.put("parameters", parameter2 );
        statements.add(entry2);

        HashMap cypher = new HashMap();
        cypher.put("statements", statements);

        Response response = service.batchWrites(objectMapper.writeValueAsString(cypher), graphDatabaseService);
        assertEquals(200, response.getStatus());

        Thread.sleep(2000);
    }
}
