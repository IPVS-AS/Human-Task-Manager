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

import com.htm.endpoint.ITasksService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

/**
 * Definition of the REST-API for managing tasks.
 * This REST-API is used by the app and OpenTOSCA.
 */
@Path("/tasks")
@Controller
public class TasksEndpoint {

    @Autowired
    private ITasksService tasksService;

    /**
     * Takes a JSON-String and creates a new task. the following keys are required to describe  the new task:
     * name, taskType, status, presentationDetails, priority
     * Optional keys are: inputParameters, outputParameters
     * @param jsonString
     *          describing the new task
     * @return
     *          Response with the ID of the newly created task
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTask(String jsonString) {
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(jsonString);
            HashMap inputParameter = null;
            HashMap outputParameter = null;
            String[] taskTypeNames = null;
            if (json.get("taskTypes") != null) {
                taskTypeNames = toStringArray((JSONArray)json.get("taskTypes"));
            }
            if (json.get("inputParameters") != null) {
                inputParameter = toHashMap((JSONArray) json.get("inputParameters"));
            }
            if (json.get("outputParameters") != null) {
                outputParameter = toHashMap((JSONArray) json.get("outputParameters"));
            }
           JSONObject presentationDetails =(JSONObject) json.get("presentationDetails");
            String result = tasksService.createTask((String) json.get("name"), taskTypeNames,
                    inputParameter, outputParameter, (String) presentationDetails.get("title"), (String) presentationDetails.get("subject"), (String) presentationDetails.get("description"), (String) json.get("priority"));
            if (result.equals("taken")) {
                return Response.status(409).entity("This task name is taken").build();
            }
            if (result.equals("taskType")) {
                return Response.status(409).entity("Task type is null").build();
            }
            return Response.status(200).entity(result).build();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Response.status(500).build();
    }

    /**
     * Gets all tasks
     * @return
     *          JASON-Array containing all tasks
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTasks() {
        try {
            String result = tasksService.getAllTask();
            if (result == null) {
                return Response.status(404).build();
            }
            return Response.status(200).entity(result).build();
        } catch (Exception e) {
            return Response.status(500).build();
        }
    }

    /**
     * Gets task with the given id
     * @param task
     *          id of the task
     * @return
     *          JSON-String containing the task and its attributes
     */
    @GET
    @Path("/{task}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTask(@PathParam("task") String task) {
        try {
            String result = tasksService.getTask(task);
            if (result == null) {
                return Response.status(404).build();
            }
            return Response.status(200).entity(result).build();
        } catch (Exception e) {
            return Response.status(500).build();
        }
    }

    /**
     * Get all tasks belonging to an user
     * @param user
     *          userId of the user
     * @return
     *          JASON-Array containing all tasks of user
     */
    @GET
    @Path("/user/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTaskUser(@PathParam("user") String user) {
        try {
            String result = tasksService.getAllTaskUser(user);
            if (result == null) {
                return Response.status(404).build();
            }
            return Response.status(200).entity(result).build();
        } catch (Exception e) {
            return Response.status(500).build();
        }
    }

    /**
     * Get all tasks belonging to a task type
     * @param taskType
     * @return
     *          JASON-Array containing all task belonging to the task type
     */
    @GET
    @Path("/taskType/{taskType}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTaskTaskType(@PathParam("taskType") String taskType) {
        try {
            String result = tasksService.getAllTaskTaskType(taskType);
            if (result == null) {
                return Response.status(404).build();
            }
            return Response.status(200).entity(result).build();
        } catch (Exception e) {
            return Response.status(500).build();
        }
    }

    /**
     * Gets all input parameters belonging to the task
     * @param task
     *          id of the task
     * @return
     *          JASON-Array containing all input parameters belonging to the task
     */
    @GET
    @Path("/{task}/input")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInputParameter(@PathParam("task") String task) {
        try {
            String result = tasksService.getInputParameter(task);
            if (result == null) {
                return Response.status(404).build();
            }
            return Response.status(200).entity(result).build();
        } catch (Exception e) {
            return Response.status(500).build();
        }
    }

    /**
     * Gets all output parameters belonging to the task
     * @param task
     *          id of the task
     * @return
     *          JASON-Array containing all output parameters belonging to the task
     */
    @GET
    @Path("/{task}/output")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOutputParameter(@PathParam("task") String task) {
        try {
            String result = tasksService.getOutputParameter(task);
            if (result == null) {
                return Response.status(404).build();
            }
            return Response.status(200).entity(result).build();
        } catch (Exception e) {
            return Response.status(500).build();
        }
    }

    /**
     * Gets the presentation details belonging to a task
     * @param task
     *          id of the task
     * @return
     *          JSON-Object containing the presentation details belonging to task
     */
    @GET
    @Path("/{task}/presentation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPresentationDetails(@PathParam("task") String task) {
        try {
            String result = tasksService.getPresentationDetails(task);
            if (result == null) {
                return Response.status(404).build();
            }
            return Response.status(200).entity(result).build();
        } catch (Exception e) {
            return Response.status(500).build();
        }
    }

    /**
     * Takes a JSON-String and updates the output parameters of a task. The following keys are required to update output parameters:
     * label, key
     * labels have to stay the same as they were before
     * @param task
     *          id of the task
     * @param jsonString
     *          JSON-String with the new values and the corresponding labels of the output parameters
     * @return
     *          true if update was successful
     */
    @POST
    @Path("/{task}/output")
    public Response updateOutputParameter(@PathParam("task") String task, String jsonString) {
        JSONParser parser = new JSONParser();
        JSONArray json = null;
        try {
            json = (JSONArray) parser.parse(jsonString);
            HashMap outputParameters = toHashMap(json);
            boolean result = tasksService.updateOutputParameter(task, outputParameters);
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
     * Takes a JSON-String and updates a task. The following keys are required to update a task:
     * name, taskTypes, status, presentationDetails, priority
     * Optional keys are: inputParameters, outputParameters
     * @param task
     *          id of the task to be updated
     * @param jsonString
     *         describing the task
     * @return
     *          true if update was successful
     */
    @POST
    @Path("/{task}")
    public Response updateTask(@PathParam("task") String task, String jsonString) {
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(jsonString);
            HashMap inputParameter = null;
            HashMap outputParameter = null;
            String claimed = null;
            String[] taskTypeNames = null;
            if (json.get("taskTypes") != null) {
                taskTypeNames = toStringArray((JSONArray)json.get("taskTypes"));
            }
            if (json.get("inputParameters") != null) {
                inputParameter = toHashMap((JSONArray) json.get("inputParameters"));
            }
            if (json.get("outputParameters") != null) {
                outputParameter = toHashMap((JSONArray) json.get("outputParameters"));
            }
            if (json.get("claimedBy") != null) {
                claimed = (String) json.get("claimedBy");
            }
            JSONObject presentationDetails =(JSONObject) json.get("presentationDetails");
            boolean result = tasksService.updateTask(task,(String) json.get("name"), taskTypeNames, (String) json.get("status"),
                    inputParameter, outputParameter, (String) presentationDetails.get("title"), (String) presentationDetails.get("subject"), (String) presentationDetails.get("description"), (String) json.get("priority"), claimed);
            if (result) {
                return Response.status(200).entity(result).build();
            }
            return Response.status(404).build();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Response.status(500).build();
    }

    /**
     * Claims a task for a user with the given userId
     * @param task
     *          id of task to be claimed
     * @param jsonString
     *          containing userId of user wanting to claim the task
     * @return
     *          true if task was successfully claimed
     */
    @POST
    @Path("/{task}/claim")
    public Response claimTask(@PathParam("task") String task, String jsonString) {
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(jsonString);
            boolean result = tasksService.claimTask(task, (String) json.get("userId"));
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
     * Updates the status of the task with the given id
     * @param task
     *          id of task to be updated
     * @param status
     *          new status of the task
     * @return
     *          true if status update was successful
     */
    @POST
    @Path("/{task}/{status}")
    public Response updateStatus(@PathParam("task") String task, @PathParam("status") String status) {
        boolean result = tasksService.updateState(task, status);
        if (result) {
            return Response.status(200).entity(result).build();
        } else {
            return Response.status(404).build();
        }
    }

    /**
     * Deletes a specific task with the given id
     * @param task
     *          id of the task to be deleted
     * @return
     *          true if deletion was successful
     */
    @DELETE
    @Path("/{task}")
    public Response deleteTask(@PathParam("task") String task) {
        boolean result = tasksService.deleteTask(task);
        if (result) {
            return Response.status(200).entity(result).build();
        } else {
            return Response.status(404).build();
        }
    }

    /**
     * converts an JSONArray to a hashMap
     * @param jsonArray
     *          JSON-Array to be converted
     * @return
     */
    private HashMap toHashMap(JSONArray jsonArray) {
        if (jsonArray != null) {
            HashMap hashMap = new HashMap();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                hashMap.put(jsonObject.get("label"), jsonObject.get("value"));
            }
            return hashMap;
        }
        return null;
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
