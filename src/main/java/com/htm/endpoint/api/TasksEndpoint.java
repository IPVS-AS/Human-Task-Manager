package com.htm.endpoint.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tasks")
public class TasksEndpoint {
    @PUT
    public Response createTask(String json) {
        String result = "Task created";
        return Response.status(200).entity(result).build();
    }

    @GET
    public Response getTasks() {
        String result = "allTasks";
        return Response.status(200).entity(result).build();
    }

    @GET
    @Path("/{task}")
    public Response getTask(@PathParam("task") String task) {
        String result = "Task with id returned";
        return Response.status(200).entity(result).build();
    }

    /*@GET
    @Path("/{user}")
    public Response getAllTaskUser(@PathParam("user") int user) {
        String result = "Task for User ";
        return  Response.status(200).entity(result).build();
    }*/

    /*@GET
    @Path("/{taskType}")
    public Response getAllTaskTaskType(@PathParam("taskType") String user) {
        String result = "Task for User ";
        return  Response.status(200).entity(result).build();
    }*/

    @POST
    @Path("/{task}")
    public Response updateTask(@PathParam("task") String task) {
        String result = "Task updated";
        return Response.status(200).entity(result).build();
    }

    @POST
    @Path("/{task}/claim")
    public Response claimTask(@PathParam("task") String task, String json) {
        String result = "Task claimed";
        return Response.status(200).entity(result).build();
    }

    @POST
    @Path("/{task}/{status}")
    public Response updateStatus(@PathParam("task") String task, @PathParam("status") String status) {
        String result = "Task claimed";
        return Response.status(200).entity(result).build();
    }

    @DELETE
    @Path("/{task}")
    public Response deleteTask(@PathParam("task") String task) {
        String result = "task deleted";
        return Response.status(200).entity(result).build();
    }
}
