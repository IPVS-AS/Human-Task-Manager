package com.htm.endpoint.api;

import com.htm.endpoint.IRolesService;
import com.htm.endpoint.impl.RolesServiceImpl;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/roles")
@Controller
//TODO: Response Nachrichten für Fehler, wenn etwas Schiefläut
public class RolesEndpoint {

    @Qualifier("rolesService")
    @Autowired
    private IRolesService rolesService;

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRole(String jsonString) {
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(jsonString);
            //String result = rolesService.createRole((String) json.get("rolename"), (String []) json.get("genericHumanRole"));
            String[] genericHumanRoles = {"bla"};
            String result = rolesService.createRole((String) json.get("rolename"), genericHumanRoles);
            if (result.equals("taken")) {
                return Response.status(409).entity("This role name is taken").build();
            }
            return Response.status(200).entity(result).build();
        } catch (ParseException e) {

        }

        return Response.status(500).build();
    }

    @GET
    public Response getRoles() {
        try {
            String result = rolesService.getAllRoles();
            return Response.status(200).entity(result).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }

    }

    @GET
    @Path("/{role}")
    public Response getRole(@PathParam("role") String role) {
        try {
            String result = rolesService.getRole(role);
            return Response.status(200).entity(result).build();
        } catch (Exception e) {

        }
        return Response.status(404).build();
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
        boolean result = rolesService.deleteRole(role);
        if (result) {
            return Response.status(200).entity(result).build();
        } else {
            return Response.status(404).build();
        }
    }

}
