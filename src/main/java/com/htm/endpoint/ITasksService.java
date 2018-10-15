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
