package com.maxdemarzi;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Validators {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static HashMap getValidCypherStatements(String body) throws IOException {
        HashMap input;

        // Parse the input
        try {
            input = objectMapper.readValue(body, HashMap.class);
        } catch (Exceptions e) {
            throw Exceptions.invalidInput;
        }

        if (!input.containsKey("statements")) {
            throw Exceptions.missingStatementsParameter;
        } else {
            Object statements = input.get("statements");
            if (statements instanceof List<?>) {
                if (((List) statements).isEmpty()) {
                    throw Exceptions.emptyStatementsParameter;
                } else {
                    if (((List) statements).get(0) instanceof Map) {
                        Map statement = (Map) ((List) statements).get(0);
                        if (!statement.containsKey("statement")) {
                            throw Exceptions.missingStatement;
                        }
                    }
                }
            } else {
                throw Exceptions.invalidStatementsParameter;
            }
        }
        return input;
    }
}
