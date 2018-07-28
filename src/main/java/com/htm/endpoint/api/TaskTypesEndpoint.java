package com.htm.endpoint.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/taskTypes")
public class TaskTypesEndpoint {
    @PUT
    public Response createTaskType(String json) {
        String result = "TaskType created";
        return Response.status(200).entity(result).build();
    }

    @GET
    public Response getTaskTypes() {
        String result = "allTaskTypes";
        return Response.status(200).entity(result).build();
    }

    @GET
    @Path("/{taskType}")
    public Response getTask(@PathParam("taskType") String taskType) {
        String result = "TaskType with id returned";
        return Response.status(200).entity(result).build();
    }


    @POST
    @Path("/{taskType}")
    public Response updateRole(@PathParam("taskType") String taskType) {
        String result = "TaskType updated";
        return Response.status(200).entity(result).build();
    }

    @DELETE
    @Path("/{taskType}")
    public Response deleteTaskType(@PathParam("taskType") String taskType) {
        String result = "task type deleted";
        return Response.status(200).entity(result).build();
    }
}
