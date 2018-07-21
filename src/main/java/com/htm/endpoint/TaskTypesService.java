package com.htm.endpoint;

public interface TaskTypesService {
    //hier soll dann die neue ID zurück gegeben werden
    String createTaskType(String json);

    String getTaskType(String id);

    String getAllTaskTypes();

    String updateTaskType(String json, String id);

    String deleteTaskType(String id);
}
