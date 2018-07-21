package com.htm.endpoint.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/roles")
//TODO: Response Nachrichten für Fehler, wenn etwas Schiefläut
public class RolesEndpoint {

    @PUT
    @Path("/new")
    public Response createRole(String json) {
        String result = "Role created";
        return Response.status(200).entity(result).build();
    }

    @GET
    @Path("/allRoles")
    public Response getRoles() {
        String result = "allRoles";
        return Response.status(200).entity(result).build();
    }

    @GET
    @Path("/{role}")
    public Response getRole(@PathParam("role") String role) {
        String result = "Role with id returned";
        return Response.status(200).entity(result).build();
    }

    @GET
    @Path("/{role}/user")
    public Response getRoleUsers(@PathParam("role") String role) {
        String result = "All users with role";
        return Response.status(200).entity(result).build();
    }

    @POST
    @Path("/{role}")
    public Response updateRole(@PathParam("role") String role) {
        String result = "Role updated";
        return Response.status(200).entity(result).build();
    }

    @DELETE
    @Path("/{role}")
    public Response deleteRole(@PathParam("role") String role) {
        String result = "role deleted";
        return Response.status(200).entity(result).build();
    }

}
