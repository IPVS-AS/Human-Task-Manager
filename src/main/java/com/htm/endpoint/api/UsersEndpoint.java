/*
 * Copyright 2018 OpenTOSCA and
 * University of Stuttgart (Institute of Architecture of Application Systems, Institute for Parallel and Distributed Systems)
 * All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.htm.endpoint.api;

import com.htm.endpoint.IUsersService;
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
 * Definition of the REST-API for managing users.
 * This REST-API is used by the app and OpenTOSCA.
 */
@Path("/users")
@Controller
public class UsersEndpoint {

    @Qualifier("usersService")
    @Autowired
    private IUsersService usersService;

    /**
     * Takes a JSON-String and creates a new user. the following keys are required to describe  the new user:
     * userId, firstname, lastname, roles
     * @param jsonString
     *          describing the new user
     * @return
     *          Response with the ID of the newly created user
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String jsonString) {
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(jsonString);
            String[] groups = toStringArray((JSONArray) json.get("roles"));
            String result = usersService.createUser((String) json.get("userId"), (String) json.get("firstname"), (String) json.get("lastname"), groups);
            if(result.equals("taken")){
                return Response.status(409).entity("This UserId is taken").build();
            }
            return Response.status(200).entity(result).build();
        } catch (ParseException e) {

        }

        return Response.status(500).build();

    }

    /**
     * Returns all users
     * @return
     *         JASON-Array containing all users
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        try {
            String result = usersService.getAllUsers();
            if (result == null) {
                return Response.status(404).build();
            }
            return Response.status(200).entity(result).build();
        } catch (Exception e) {
            return Response.status(500).build();
        }

    }

    /**
     * Gets an user with all corresponding attributes
     * @param user
     *         userId of user to be returned
     * @return
     *         JSON-Object containing attributes of user
     */
    @GET
    @Path("/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("user") String user) {
        try {
            String result = usersService.getUser(user);
            if (result == null) {
                return Response.status(404).build();
            }
            return Response.status(200).entity(result).build();
        } catch (Exception e) {
            return Response.status(500).build();
        }


    }

    /**
     * Takes a JSON-String and updates an user. The following keys are required to update an user:
     * @param user
     *         userId of user to be updated
     * @param jsonString
     *         JSON-Object describing the new attributes of role
     * @return
     *          true if update was successful
     */
    @POST
    @Path("/{user}")
    public Response updateUser(@PathParam("user") String user, String jsonString) {
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(jsonString);
            String[] groups = toStringArray((JSONArray) json.get("roles"));
            boolean result = usersService.updateUser((String) json.get("firstname"),
                    (String) json.get("lastname"),groups,(String) json.get("userId"));
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
     * Deletes a specific user
     * @param user
     *          userId of user to be deleted
     * @return
     *         response containing true
     */
    @DELETE
    @Path("/{user}")
    public Response deleteUser(@PathParam("user") String user) {

        boolean result = usersService.deleteUser(user);
        if (result) {
            return Response.status(200).entity(result).build();
        } else {
            return Response.status(404).build();
        }
    }


  public IUsersService getUsersService() {

        return usersService;
    }

    /**
     * Takes a JSON-Array and converts it to a String-Array
     * @param jsonArray
     *         JSON-Array to be converted
     * @return
     *         String-Array containing values of jasonArray
     */
    public String[] toStringArray (JSONArray jsonArray) {
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
