package com.gdev.resources;

import com.codahale.metrics.annotation.Timed;
import com.gdev.api.TestResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;

@Path("/")
public class TestResource {
    public TestResource() { }

    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Timed
    public TestResponse sayHello() {
        return new TestResponse();
    }
}
