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
package com.htm.entities.jpa;

import com.htm.entities.WrappableEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * The persistent class for the TASKTYPE database table
 */
@Entity
@Table(name = "TASKTYPE")
public class TaskType implements Serializable, WrappableEntity {
    private static final long serialVersonUID = 1L;
    @Id
    @GeneratedValue
    private int id;

    private String taskTypeName;



    //bi-directional many-to-many association to Group
    @ManyToMany
    @JoinTable(
            name = "TaskTypeGroups",
            joinColumns = {
            @JoinColumn(name = "TASKTYPE_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "GROUP_ID")
            }
    )

    private List<Group> groups;

    //bi-directional many-to-many association to Task
    @ManyToMany
    @JoinTable(
            name = "TaskTypeTasks",
            joinColumns = {
                    @JoinColumn(name = "TASKTYPE_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "TASK_ID")
            }
    )
    private List<Humantaskinstance> tasks;

    public TaskType() {

    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskTypeName() {
        return this.taskTypeName;
    }

    public void setTaskTypeName(String taskTypeName) {
        this.taskTypeName = taskTypeName;
    }

    public List<Group> getGroups() {
        return this.groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Humantaskinstance> getTasks() {
        return tasks;
    }

    public void setTasks(List<Humantaskinstance> tasks) {
        this.tasks = tasks;
    }

}
