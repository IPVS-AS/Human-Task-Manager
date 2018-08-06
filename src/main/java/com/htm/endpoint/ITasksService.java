package com.htm.endpoint;


import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public interface ITasksService {

    String createTask(String name, String[] taskTypeNames, HashMap inputParameter,
                      HashMap outputParameter, String title, String subject, String description, String priority);

    String getTask(String id);

    String getAllTask();

    String getAllTaskUser(String userId);

    String getAllTaskTaskType(String typeId);

    String getInputParameter(String task);

    String getOutputParameter(String task);

    String getPresentationDetails(String task);

    boolean updateOutputParameter(String task, HashMap outputParameters);

    boolean updateTask(String id,String name, String[] taskTypeNames, String state, HashMap inputParameter,
    HashMap outputParameter, String title, String subject, String description, String priority, String claimed);

    boolean updateState(String id, String state);

    boolean claimTask(String id, String userId);

    boolean deleteTask(String id);


}
