package com.maxdemarzi;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Exceptions extends WebApplicationException {
    public Exceptions(int code, String error)  {
        super(new Throwable(error), Response.status(code)
                .entity("{\"error\":\"" + error + "\"}")
                .type(MediaType.APPLICATION_JSON)
                .build());

    }

    public static Exceptions invalidInput = new Exceptions(400, "Invalid Input");

    public static Exceptions missingStatementsParameter = new Exceptions(400, "Missing statements Parameter.");
    public static Exceptions invalidStatementsParameter = new Exceptions(400, "Invalid statements Parameter.");
    public static Exceptions emptyStatementsParameter = new Exceptions(400, "Empty statements Parameter.");
    public static Exceptions missingStatement = new Exceptions(400, "Missing statement.");

}
