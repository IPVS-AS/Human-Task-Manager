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
package com.htm.taskinstance.jpa;

import com.htm.dm.IPersistenceVisitor;
import com.htm.entities.WrappableEntity;
import com.htm.entities.jpa.Group;
import com.htm.entities.jpa.Humantaskinstance;
import com.htm.entities.jpa.TaskType;
import com.htm.taskinstance.ITaskInstance;
import com.htm.taskinstance.ITaskType;
import com.htm.userdirectory.IGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * wrapper for Task type instances
 */
public class TaskTypeWrapper implements ITaskType {

    protected TaskType taskTypeEntity;

    public TaskTypeWrapper(TaskType adaptee) {this.taskTypeEntity = adaptee;}

    public TaskTypeWrapper() {this.taskTypeEntity = new TaskType();}

    public TaskTypeWrapper(String name) {
        taskTypeEntity = new TaskType();
        setTaskTypName(name);
    }

    @Override
    public String getId() {
        return Integer.toString(taskTypeEntity.getId());
    }

    @Override
    public void accept(IPersistenceVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public WrappableEntity getAdaptee() {
        return taskTypeEntity;
    }

    @Override
    public String getTaskTypName() {
        return taskTypeEntity.getTaskTypeName();
    }


    @Override
    public void setTaskTypName(String taskTypName) {
        taskTypeEntity.setTaskTypeName(taskTypName);

    }


    @Override
    public void addGroup(IGroup group) {
        List<Group> groupEntities = taskTypeEntity.getGroups();
        // if the first group is added to the list of groups is null
        if (groupEntities == null) {
            groupEntities = new ArrayList<Group>();
            taskTypeEntity.setGroups(groupEntities);
        }
        groupEntities.add((Group) group.getAdaptee());
    }

    @Override
    public void addTask(ITaskInstance task) {
        List<Humantaskinstance> taskEntities = taskTypeEntity.getTasks();
        // if the first task is added to the list of task is null
        if (taskEntities == null) {
            taskEntities = new ArrayList<Humantaskinstance>();
            taskTypeEntity.setTasks(taskEntities);
        }
        taskEntities.add((Humantaskinstance) task.getAdaptee());

    }
}
