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
package com.htm.taskinstance;

import com.htm.dm.IDataModelElement;
import com.htm.userdirectory.IGroup;

/**
 * Task types represent the different kinds of possible task. It makes it possible to group task with a similar content/behavior.
 * Furthermore it is possible to map task types to groups of users.
 */
public interface ITaskType extends IDataModelElement {

    public String getId();

    public String getTaskTypName();

    public void setTaskTypName(String taskTypName);

    public void addGroup(IGroup goup);

    public void addTask(ITaskInstance task);
}
