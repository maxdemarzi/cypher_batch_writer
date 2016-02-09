# cypher_batch_writer
Let's try batching some cypher queries


1. Build it:

        mvn clean package

2. Copy target/cypher_batch_writer-1.0.jar to the plugins/ directory of your Neo4j server.

3. Download and copy additional jars to the plugins/ directory of your Neo4j server.

        wget http://repo1.maven.org/maven2/joda-time/joda-time/2.9.2/joda-time-2.9.2.jar
        wget http://repo1.maven.org/maven2/com/google/guava/guava/19.0/guava-19.0.jar

3. Configure Neo4j by adding a line to conf/neo4j-server.properties:

        org.neo4j.server.thirdparty_jaxrs_classes=com.maxdemarzi=/v1

4. Start Neo4j server.

5. Send a Cypher statement via :POST to  /v1/service/batch

        {
          "statements" : [ {
            "statement" : "CREATE (n:user {user_id: {user_id} }) ",
            "parameters" : {"user_id": 123}
          } ]
        }
