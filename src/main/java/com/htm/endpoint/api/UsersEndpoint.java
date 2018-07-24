package com.htm.endpoint.api;

import com.htm.endpoint.UsersService;
import com.htm.endpoint.impl.UsersServiceImpl;
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

    private UsersServiceImpl usersService;
//    @Qualifier("usersService")
//    @Autowired
//    private UsersServiceImpl usersService;

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String json) {
        String result = usersService.createUser("test");
        return Response.status(200).entity(result).build();
    }

    @GET
    public Response getUsers() {
        String result = "allUsers";
        return Response.status(200).entity(result).build();
    }

    @GET
    @Path("/{user}")
    public Response getUser(@PathParam("user") String user) {

        String result = usersService.getUser("Dean79");
        return Response.status(200).entity(result).build();
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

    @Autowired
   public void setUsersService(UsersServiceImpl usersService) {
    this.usersService = usersService;
  }

  public UsersService getUsersService() {

        return usersService;
    }
}
