package com.htm.endpoint;

public interface TasksService {
    //hier soll dann die neue ID zurück gegeben werden
    String createTask(String json);

    String getTask(String id);

    String getAllTask();

    String getAllTaskUser(String userId);

    String getAllTaskTaskType(String typeId);

    String updateTask(String json, String id);

    String updateStatus(String id, String status);

    String claimTask(String id, String json);

    String deleteTask(String id);


}
