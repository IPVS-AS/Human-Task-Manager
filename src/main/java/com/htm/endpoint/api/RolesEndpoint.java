package com.htm.endpoint.api;

import com.htm.endpoint.IRolesService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Definition of the REST-API for managing roles/groups.
 * This REST-API is used by the app and OpenTOSCA.
 */
@Path("/roles")
@Controller
public class RolesEndpoint {

    @Qualifier("rolesService")
    @Autowired
    private IRolesService rolesService;

    /**
     * Takes a JSON-String and creates a new role/group. the following keys are required to describe  the new role/group:
     * rolename, genericHumanRoles
     *
     * @param jsonString
     *          describing the new role/group
     * @return Response with the ID of the newly created role/group
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRole(String jsonString) {
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(jsonString);
            String[] genericHumanRoles = toStringArray((JSONArray) json.get("genericHumanRoles"));
            String result = rolesService.createRole((String) json.get("rolename"), genericHumanRoles);
            if (result.equals("taken")) {
                return Response.status(409).entity("This role name is taken").build();
            }
            return Response.status(200).entity(result).build();
        } catch (ParseException e) {

        }

        return Response.status(500).build();
    }

    /**
     * Returns all roles/groups
     * @return
     *         JASON-Array containing all roles/groups
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoles() {
        try {
            String result = rolesService.getAllRoles();
            return Response.status(200).entity(result).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }

    }

    /**
     * Gets a role/group with all corresponding attributes
     * @param role
     *          name of role/group
     * @return
     *          JSON-Object containing attributes of role
     *
     */
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

    /**
     * Gets all users belonging to a specific role/group
     * @param role
     *         name of role
     * @return
     *         JSON-Array containing all users belonging to role with their attributes
     */
    @GET
    @Path("/{role}/user")
    public Response getRoleUsers(@PathParam("role") String role) {
        String result = rolesService.getRoleUsers(role);
        if (result == null) {
            return Response.status(404).build();
        }
        return Response.status(200).entity(result).build();
    }

    /**
     * Takes a JSON-String and creates a new role/group. the following keys are required to describe  the new role/group:
     * rolename, genericHumanRoles
     * @param role
     *         name of role
     * @param jsonString
     *         JSON-Object describing the new attributes of role
     * @return
     */
    @POST
    @Path("/{role}")
    public Response updateRole(@PathParam("role") String role, String jsonString) {
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(jsonString);
            String[] genericHumanRoles = toStringArray((JSONArray) json.get("genericHumanRoles"));
            boolean result = rolesService.updateRole(role, genericHumanRoles);
            if (result) {
                return Response.status(200).entity(result).build();
            } else {
                return Response.status(404).build();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Response.status(500).build();
    }

    /**
     * Deletes a specific role/group
     * @param role
     *         name of role
     * @return
     *         Response containing true
     */
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

    /**
     * Takes a JSON-Array and converts it to a String-Array
     * @param jsonArray
     *         JSON-Array to be converted
     * @return
     *         String-Array containing values of jasonArray
     */
    private String[] toStringArray (JSONArray jsonArray) {
        if (jsonArray != null) {
            String [] array = new String[jsonArray.size()];
            for (int i = 0; i < array.length; i++) {
                array[i] = jsonArray.get(i).toString();
            }
            return array;
        }
        return null;
    }

}
