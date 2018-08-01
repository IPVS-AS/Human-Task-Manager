package com.htm.endpoint.api;

import com.htm.endpoint.ITaskTypesService;
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

@Path("/taskTypes")
@Controller
public class TaskTypesEndpoint {

    @Qualifier("taskTypesService")
    @Autowired
    private ITaskTypesService taskTypesService;
    /**
     * Takes a JSON-String and creates a new task type. the following keys are required to describe  the new task type:
     * typename, associatedRoles
     * @param jsonString
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTaskType(String jsonString) {
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(jsonString);
            String[]roles = toStringArray((JSONArray) json.get("associatedRoles"));
            String result = taskTypesService.createTaskType((String) json.get("typename"), roles);
            if (result.equals("taken")) {
                return Response.status(409).entity("This taskType name is taken").build();
            }
            return Response.status(200).entity(result).build();
        } catch (ParseException e) {

        }

        return Response.status(500).build();
    }

    /**
     * Returns all TaskTypes
     * @return
     *         JSON-Array containing all taskTypes
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTaskTypes() {
        try {
            String result = taskTypesService.getAllTaskTypes();
            return Response.status(200).entity(result).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }

    /**
     * Gets a taskType with all corresponding attributes
     * @param taskType
     *          name of task type
     * @return
     *          JSON-Object containing attributes of taskType
     */
    @GET
    @Path("/{taskType}")
    public Response getTask(@PathParam("taskType") String taskType) {
        try {
            String result = taskTypesService.getTaskType(taskType);
            return Response.status(200).entity(result).build();
        } catch (Exception e) {

        }
        return Response.status(404).build();
    }


    /**
     * Takes a JSON-String and updates a task type. the following keys are required to update a task type:
     * typename, associatedRoles
     * @param taskType
     *          name of  the task type
     * @return
     *          true if update was successful
     */
    @POST
    @Path("/{taskType}")
    public Response updateTaskType(@PathParam("taskType") String taskType, String jsonString) {
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(jsonString);
            String[] roles = toStringArray((JSONArray) json.get("associatedRoles"));
            boolean result = taskTypesService.updateTaskType(taskType, roles);
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
     * Deletes a specific task type
     * @param taskType
     *         name of task type
     * @return
     *         true if deletion was successful
     */
    @DELETE
    @Path("/{taskType}")
    public Response deleteTaskType(@PathParam("taskType") String taskType) {
        boolean result = taskTypesService.deleteTaskType(taskType);
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
