package com.htm.endpoint.impl;

import com.htm.db.IDataAccessProvider;
import com.htm.endpoint.ITasksService;
import com.htm.exceptions.DatabaseException;
import com.htm.exceptions.HumanTaskManagerException;
import com.htm.taskinstance.IInputParameter;
import com.htm.taskinstance.IOutputParameter;
import com.htm.taskinstance.ITaskInstance;
import com.htm.taskinstance.IWorkItem;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class TasksServiceImpl implements ITasksService {

    /**
     * Autowired data access provider with methods implemented for use of OpenTOSCA
     */
    protected IDataAccessProvider dataAccessTosca;


    /**
     * Creates a new task
     * @param name
     *          name of the task
     * @param taskTypeNames
     *          name of the task type of the task
     * @param state
     *          state of the task
     * @param inputParameter
     *          input parameters of the task
     * @param outputParameter
     *          output parameters of the task
     * @param title
     *          title of the task shown to the user
     * @param subject
     *          subject of the task shown to the user
     * @param description
     *          description of the task shown to the user
     * @param priority
     *          priority of the task
     * @return
     *           id of the newly crated task as JSON-String
     */
    @Override
    public String createTask(String name, String[] taskTypeNames,  HashMap inputParameter,
                             HashMap outputParameter, String title, String subject, String description, String priority) {
        ITaskInstance task = null;
        JSONObject response = new JSONObject();
        // Check if taskType is there
        if (taskTypeNames == null) {
            return "taskType";
        }
        // Create taskInstance
        try {
            task = dataAccessTosca.createTask(name,  title, subject, description, priority, taskTypeNames);
            if (task == null) {
                return "taken";
            }
            response.put("id", task.getId());

        // map task to task type
          /*  for (int i = 0; i < taskTypeNames.length; i++) {
                dataAccessTosca.addTaskToTaskType(task.getId(), taskTypeNames[i]);
            }*/
        // insert inputParameters, if not null
        if (inputParameter != null) {
            Set<String> entries = inputParameter.keySet();
            for (String entry: entries) {
                boolean insert = false;
                try {
                    insert = dataAccessTosca.createInputParameter(entry, (String) inputParameter.get(entry), task.getId());
                } catch (DatabaseException e) {
                    e.printStackTrace();
                }
                if (!insert) {
                    return null;
                }
            }
        }
        // insert outputParameters, if not null
        if (outputParameter != null) {
            Set<String> entries = outputParameter.keySet();
            for (String entry: entries) {
                boolean insert = false;
                try {
                    insert = dataAccessTosca.createOutputParameter(entry, (String) outputParameter.get(entry), task.getId());
                } catch (DatabaseException e) {
                    e.printStackTrace();
                }
                if (!insert) {
                    return null;
                }
            }
        }
        } catch (DatabaseException e) {
            e.printStackTrace();


        }

        return response.toString();
    }

    /**
     * Gets the task with the given id
     * @param id
     *          tiid of the task
     * @return
     *          JSON-String containing the attributes of id
     */
    @Override
    public String getTask(String id) {
        JSONObject response = new JSONObject();
        try {
            ITaskInstance task = dataAccessTosca.getTaskInstance(id);
            if (task != null) {
                //get the belonging work item
                List<IWorkItem> workItem = dataAccessTosca.getWorkItems(id);
                //get the presentationDetails
                JSONObject presentation = new JSONObject();
                presentation.put("title", task.getPresentationName());
                presentation.put("subject", task.getPresentationSubject());
                presentation.put("description", task.getPresentationDescription());
                //get inputParameters
                Set<IInputParameter> inputParameters = dataAccessTosca.getInputParametersByTask(task.getId());

                //get outputParameters
                Set<IOutputParameter> outputParameters = dataAccessTosca.getOutputParametersByTask(task.getId());
                //put information in response
                response.put("id", task.getId());
                response.put("name", task.getName());
                JSONArray taskTypes = new JSONArray();
                taskTypes.addAll(dataAccessTosca.getTaskTypeByTask(task.getId()));
                response.put("taskType", taskTypes);
                response.put("priority", task.getPriority());
                response.put("state", task.getStatus());
                if (workItem.get(0).getAssignee() != null) {
                    response.put("claimedBy", workItem.get(0).getAssignee().getUserId());
                }
                response.put("presentationDetails",presentation);
                if (inputParameters != null) {
                    JSONArray inputs = new JSONArray();
                    for (IInputParameter input : inputParameters) {
                        JSONObject i = new JSONObject();
                        i.put("label", input.getLabel());
                        i.put("value", input.getValue());
                        inputs.add(i);
                    }
                    response.put("inputParameters", inputs);
                }
                if (outputParameters != null) {
                    JSONArray outputs = new JSONArray();
                    for (IOutputParameter output : outputParameters) {
                        JSONObject i = new JSONObject();
                        i.put("label", output.getLabel());
                        i.put("value", output.getValue());
                        outputs.add(i);
                    }
                    response.put("outputParameters",outputs);
                }

                return response.toString();
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets all tasks
     * @return
     *          JSON-Array as String with all Tasks and their attributes
     */
    @Override
    public String getAllTask() {
        try {
            Set<ITaskInstance> allTasks = dataAccessTosca.getAllTasks();
            JSONArray fullResponse = new JSONArray();
            // if allTasks is null, then no tasks were found
            if (allTasks != null) {
                // create a JSON-Object for every task and add it to fullresponse
                for (ITaskInstance task : allTasks) {
                    JSONObject response = new JSONObject();
                    //get the belonging work item
                    List<IWorkItem> workItem = dataAccessTosca.getWorkItems(task.getId());
                    //get the presentationDetails
                    JSONObject presentation = new JSONObject();
                    presentation.put("title", task.getPresentationName());
                    presentation.put("subject", task.getPresentationSubject());
                    presentation.put("description", task.getPresentationDescription());
                    //get inputParameters
                    Set<IInputParameter> inputParameters = dataAccessTosca.getInputParametersByTask(task.getId());

                    //get outputParameters
                    Set<IOutputParameter> outputParameters = dataAccessTosca.getOutputParametersByTask(task.getId());
                    //put information in response
                    response.put("id", task.getId());
                    response.put("name", task.getName());
                    JSONArray taskTypes = new JSONArray();
                    taskTypes.addAll(dataAccessTosca.getTaskTypeByTask(task.getId()));
                    response.put("taskType", taskTypes);
                    response.put("taskType", "Noch einfügen");
                    response.put("priority", task.getPriority());
                    response.put("state", task.getStatus());
                    if (workItem.get(0).getAssignee() != null) {
                        response.put("claimedBy", workItem.get(0).getAssignee().getUserId());
                    }
                    response.put("presentationDetails",presentation);
                    if (inputParameters != null) {
                        JSONArray inputs = new JSONArray();
                        for (IInputParameter input : inputParameters) {
                            JSONObject i = new JSONObject();
                            i.put("label", input.getLabel());
                            i.put("value", input.getValue());
                            inputs.add(i);
                        }
                        response.put("inputParameters", inputs);
                    }
                    if (outputParameters != null) {
                        JSONArray outputs = new JSONArray();
                        for (IOutputParameter output : outputParameters) {
                            JSONObject i = new JSONObject();
                            i.put("label", output.getLabel());
                            i.put("value", output.getValue());
                            outputs.add(i);
                        }
                        response.put("outputParameters",outputs);
                    }
                    fullResponse.add(response);
                }
                return fullResponse.toString();
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets all tasks belonging to a user
     * @param userId
     *          userId of user
     * @return
     */
    @Override
    public String getAllTaskUser(String userId) {
        try {
            Set<ITaskInstance> allTasks = dataAccessTosca.getTasksByUser(userId);
            JSONArray fullResponse = new JSONArray();
            //if allTasks is null, then no tasks for this user was found
            if (allTasks != null) {
                // create a JSON-Object for every task and add it to fullresponse
                for (ITaskInstance task : allTasks) {
                    JSONObject response = new JSONObject();
                    //get the belonging work item
                    List<IWorkItem> workItem = dataAccessTosca.getWorkItems(task.getId());
                    //get the presentationDetails
                    JSONObject presentation = new JSONObject();
                    presentation.put("title", task.getPresentationName());
                    presentation.put("subject", task.getPresentationSubject());
                    presentation.put("description", task.getPresentationDescription());
                    //get inputParameters
                    Set<IInputParameter> inputParameters = dataAccessTosca.getInputParametersByTask(task.getId());

                    //get outputParameters
                    Set<IOutputParameter> outputParameters = dataAccessTosca.getOutputParametersByTask(task.getId());
                    //put information in response
                    response.put("id", task.getId());
                    response.put("name", task.getName());
                    JSONArray taskTypes = new JSONArray();
                    taskTypes.addAll(dataAccessTosca.getTaskTypeByTask(task.getId()));
                    response.put("taskType", taskTypes);
                    response.put("taskType", "Noch einfügen");
                    response.put("priority", task.getPriority());
                    response.put("state", task.getStatus());
                    if (workItem.get(0).getAssignee() != null) {
                        response.put("claimedBy", workItem.get(0).getAssignee().getUserId());
                    }
                    response.put("presentationDetails",presentation);
                    if (inputParameters != null) {
                        JSONArray inputs = new JSONArray();
                        for (IInputParameter input : inputParameters) {
                            JSONObject i = new JSONObject();
                            i.put("label", input.getLabel());
                            i.put("value", input.getValue());
                            inputs.add(i);
                        }
                        response.put("inputParameters", inputs);
                    }
                    if (outputParameters != null) {
                        JSONArray outputs = new JSONArray();
                        for (IOutputParameter output : outputParameters) {
                            JSONObject i = new JSONObject();
                            i.put("label", output.getLabel());
                            i.put("value", output.getValue());
                            outputs.add(i);
                        }
                        response.put("outputParameters",outputs);
                    }
                    fullResponse.add(response);
                }
                return fullResponse.toString();
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets all tasks belonging to a task type
     * @param typeId
     *          name of the task type
     * @return
     */
    @Override
    public String getAllTaskTaskType(String typeId) {
        try {
            Set<ITaskInstance> allTasks = dataAccessTosca.getTasksByTaskType(typeId);
            JSONArray fullResponse = new JSONArray();
            //if allTasks is null, then no tasks for this user was found
            if (allTasks != null) {
                // create a JSON-Object for every task and add it to fullresponse
                for (ITaskInstance task : allTasks) {
                    JSONObject response = new JSONObject();
                    //get the belonging work item
                    List<IWorkItem> workItem = dataAccessTosca.getWorkItems(task.getId());
                    //get the presentationDetails
                    JSONObject presentation = new JSONObject();
                    presentation.put("title", task.getPresentationName());
                    presentation.put("subject", task.getPresentationSubject());
                    presentation.put("description", task.getPresentationDescription());
                    //get inputParameters
                    Set<IInputParameter> inputParameters = dataAccessTosca.getInputParametersByTask(task.getId());

                    //get outputParameters
                    Set<IOutputParameter> outputParameters = dataAccessTosca.getOutputParametersByTask(task.getId());
                    //put information in response
                    response.put("id", task.getId());
                    response.put("name", task.getName());
                    JSONArray taskTypes = new JSONArray();
                    taskTypes.addAll(dataAccessTosca.getTaskTypeByTask(task.getId()));
                    response.put("taskType", taskTypes);
                    response.put("priority", task.getPriority());
                    response.put("state", task.getStatus());
                    if (workItem.get(0).getAssignee() != null) {
                        response.put("claimedBy", workItem.get(0).getAssignee().getUserId());
                    }
                    response.put("presentationDetails",presentation);
                    if (inputParameters != null) {
                        JSONArray inputs = new JSONArray();
                        for (IInputParameter input : inputParameters) {
                            JSONObject i = new JSONObject();
                            i.put("label", input.getLabel());
                            i.put("value", input.getValue());
                            inputs.add(i);
                        }
                        response.put("inputParameters", inputs);
                    }
                    if (outputParameters != null) {
                        JSONArray outputs = new JSONArray();
                        for (IOutputParameter output : outputParameters) {
                            JSONObject i = new JSONObject();
                            i.put("label", output.getLabel());
                            i.put("value", output.getValue());
                            outputs.add(i);
                        }
                        response.put("outputParameters",outputs);
                    }
                    fullResponse.add(response);
                }
                return fullResponse.toString();
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets all input parameters belonging to task
     * @param task
     *          id of task which input parameters should be returned
     * @return
     *          all input parameters belonging to task
     */
    @Override
    public String getInputParameter(String task) {
        try {
            Set<IInputParameter> inputParameters = dataAccessTosca.getInputParametersByTask(task);
            JSONArray response = new JSONArray();
            if (inputParameters != null) {
                for (IInputParameter input : inputParameters) {
                    JSONObject i = new JSONObject();
                    i.put("id", input.getId());
                    i.put("label", input.getLabel());
                    i.put("value", input.getValue());
                    i.put("tiid", task);
                    response.add(i);
                }
                return response.toString();
            }
            return null;

        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets all output parameters belonging to task
     * @param task
     *          id of task which input parameters should be returned
     * @return
     *          all output parameters belonging to task
     */
    @Override
    public String getOutputParameter(String task) {
        try {
            Set<IOutputParameter> outputParameters = dataAccessTosca.getOutputParametersByTask(task);
            JSONArray response = new JSONArray();
            if (outputParameters != null) {
                for (IOutputParameter output : outputParameters) {
                    JSONObject i = new JSONObject();
                    i.put("id",output.getId());
                    i.put("label", output.getLabel());
                    i.put("value", output.getValue());
                    i.put("tiid", task);
                    response.add(i);
                }
                return response.toString();
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the presentationDetails of the task with the given id
     * @param task
     *          tiid of the task
     * @return
     *          JSON-String containing presentation details of task
     */
    @Override
    public String getPresentationDetails(String task) {
        try {
            ITaskInstance taskInstance = dataAccessTosca.getTaskInstance(task);
            if (taskInstance != null) {
                JSONObject presentation = new JSONObject();
                presentation.put("title", taskInstance.getPresentationName());
                presentation.put("subject", taskInstance.getPresentationSubject());
                presentation.put("description", taskInstance.getPresentationDescription());
                return presentation.toString();
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates output parameters of a task with the given values
     * @param task
     *          id of the task
     * @param outputParameters
     *          labels and new values of the output parameters of the task
     * @return
     */
    @Override
    public boolean updateOutputParameter(String task, HashMap outputParameters) {
        boolean response = false;
        try {
            response = dataAccessTosca.updateOutputParameter(task, outputParameters);
            return response;
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update task with given values
     * @param id
     *          id of the task to be updated
     * @param name
     *          name of the task
     * @param taskTypeNames
     *          name of the task type of the task
     * @param state
     *          state of the task
     * @param inputParameter
     *          input parameters of the task
     * @param outputParameter
     *          output parameters of the task
     * @param title
     *          title of the task shown to the user
     * @param subject
     *          subject of the task shown to the user
     * @param description
     *          description of the task shown to the user
     * @param priority
     *          priority of the task
     * @return
     *          true if update was successful
     */
    @Override
    public boolean updateTask(String id,String name, String[] taskTypeNames, String state, HashMap inputParameter, HashMap outputParameter, String title, String subject, String description, String priority, String claimed) {
        boolean response = false;
        try {
            response = dataAccessTosca.updateTask(id, name, taskTypeNames, state, inputParameter ,outputParameter, title, subject, description, priority, claimed);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public boolean updateState(String id, String state) {
        boolean response = false;
        try {
            response = dataAccessTosca.updateState(id, state);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public boolean claimTask(String id, String userId) {
        boolean response = false;
        try {
            response = dataAccessTosca.claimTask(id, userId);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Delets a task with the given id
     * @param id
     *          id of the task to be deleted
     * @return
     *          true if deletion was successful
     */
    @Override
    public boolean deleteTask(String id) {
        boolean deleted = false;
        try {
            deleted = dataAccessTosca.deleteHumanTaskInstance(id);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return deleted;
    }

    @Autowired
    public void setIDataAccessProvider(IDataAccessProvider dataAccessTosca) {
        this.dataAccessTosca = dataAccessTosca;
    }

    public IDataAccessProvider getIDataAccessProvider() {return dataAccessTosca;}
}
