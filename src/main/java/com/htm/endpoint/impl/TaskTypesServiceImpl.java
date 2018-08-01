package com.htm.endpoint.impl;

import com.htm.db.IDataAccessProvider;
import com.htm.endpoint.ITaskTypesService;
import com.htm.exceptions.DatabaseException;
import com.htm.exceptions.HumanTaskManagerException;
import com.htm.taskinstance.ITaskType;
import com.htm.userdirectory.IGroup;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;


public class TaskTypesServiceImpl implements ITaskTypesService {

    /**
     * Autowired data access provider with methods implemented for use of OpenTOSCA
     */
    protected IDataAccessProvider dataAccessTosca;

    /**
     * Creates a new task type
     * @param taskTypeName
     *         name of the new task type
     * @param roles
     *         associated roles/groups of the task type
     * @return
     *         id of the newly crated task type as JSON-String
     */
    @Override
    public String createTaskType(String taskTypeName, String[] roles) {
        ITaskType taskType = null;
        JSONObject response = new JSONObject();
        try {
            taskType = dataAccessTosca.getTaskType(taskTypeName);
            // if taskType is null, then taskTypename is not taken by another task type
            if (taskType == null) {
                taskType = dataAccessTosca.createTaskType(taskTypeName);
                response.put("id", taskType.getId());
                if (roles != null) {
                    for (int i = 0; i < roles.length; i++) {
                        dataAccessTosca.addGroupToTaskType(taskType.getTaskTypName(), roles[i]);
                    }
                }
                return response.toString();
            }
            return "taken";
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return  null;
    }

    /**
     * Gets the task type with the given id
     * @param id
     *         name of the task type
     * @return
     *         JSON-String containing the attributes if id
     */
    @Override
    public String getTaskType(String id) {
        JSONObject response = new JSONObject();
        try {
            ITaskType taskType = dataAccessTosca.getTaskType(id);
            // if group is null, then no roles/groups with group name id  were found
            if (taskType != null) {
                response.put("id", taskType.getId());
                response.put("typename", taskType.getTaskTypName());
                JSONArray roles = new JSONArray();
                roles.addAll(dataAccessTosca.getTaskTypeAllGroups(id));
                response.put("associatedRoles", roles);
                return response.toString();
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets all task types
     * @return
     *         JSON-Array as String with all task types and their attributes
     */
    @Override
    public String getAllTaskTypes() {
        try {
            Set<ITaskType> allTaskTypes = dataAccessTosca.getAllTaskTypes();
            JSONArray response = new JSONArray();
            // if allTaskTypes is null, then no task types were found
            if (allTaskTypes != null) {
                // create a JSON-Object for every task type and add it to response
                for (ITaskType taskType : allTaskTypes) {
                    JSONObject j = new JSONObject();
                    j.put("id", taskType.getId());
                    j.put("typename", taskType.getTaskTypName());
                    JSONArray roles = new JSONArray();
                    roles.addAll(dataAccessTosca.getTaskTypeAllGroups(taskType.getTaskTypName()));
                    j.put("associatedRoles", roles);
                    response.add(j);
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
     * Updates task type with the given values
     * @param id
     *          name of task type to be updated
     * @param roles
     *          new roles/groups mapped to id
     * @return
     *         true if deletion was successful
     */
    @Override
    public boolean updateTaskType(String id, String[] roles) {
        boolean response = false;
        try {
            response = dataAccessTosca.updateTaskType(id, roles);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * Deletes a task type with the given task type name
     * @param id
     *         name of task type
     * @return
     *         true if deletion was successful
     */
    @Override
    public boolean deleteTaskType(String id) {
        boolean deleted = false;
        try {
            deleted = dataAccessTosca.deleteTaskType(id);
        } catch (HumanTaskManagerException e) {
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
