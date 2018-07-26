package com.htm.endpoint.api;

import com.htm.endpoint.UsersService;
import com.htm.endpoint.impl.UsersServiceImpl;
import com.htm.userdirectory.IUser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Controller
public class UsersEndpoint {

   // private UsersService usersService = new UsersServiceImpl();
    //private UsersServiceImpl usersService;
    @Qualifier("usersService")
    @Autowired
    private UsersServiceImpl usersService;

    @PUT
    //@Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String jsonString) {
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(jsonString);
            String result = usersService.createUser((String) json.get("userId"), (String) json.get("firstname"), (String) json.get("lastname"));
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
        String result = "allUsers";
        return Response.status(200).entity(result).build();
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
    public Response updateUser(@PathParam("user") String user) {
        String result = "User updated";
        return Response.status(200).entity(result).build();
    }

    @DELETE
    @Path("/{user}")
    public Response deleteRole(@PathParam("user") String user) {
        String result = "user deleted";
        return Response.status(200).entity(result).build();
    }

   // @Autowired
//   public void setUsersService(UsersServiceImpl usersService) {
//    this.usersService = usersService;
//  }

  public UsersService getUsersService() {

        return usersService;
    }
}
