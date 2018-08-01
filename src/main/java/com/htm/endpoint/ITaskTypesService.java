package com.htm.endpoint;

import org.springframework.stereotype.Repository;

@Repository
public interface ITaskTypesService {

    String createTaskType(String taskTypeName, String[] roles);

    String getTaskType(String id);

    String getAllTaskTypes();

    boolean updateTaskType(String id, String[] roles);

    boolean deleteTaskType(String id);
}
