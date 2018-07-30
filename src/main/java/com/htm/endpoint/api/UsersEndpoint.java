package com.htm.endpoint.api;

import com.htm.endpoint.IUsersService;
import com.htm.endpoint.impl.UsersServiceImpl;
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

@Path("/users")
@Controller
public class UsersEndpoint {

    @Qualifier("usersService")
    @Autowired
    private IUsersService usersService;

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

    @GET
    public Response getUsers() {
        try {
            String result = usersService.getAllUsers();
            return Response.status(200).entity(result).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }

    }

    @GET
    @Path("/{user}")
    public Response getUser(@PathParam("user") String user) {
        try {
            String result = usersService.getUser(user);
            return Response.status(200).entity(result).build();
        } catch (Exception e) {

        }
        return Response.status(404).build();


    }

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
