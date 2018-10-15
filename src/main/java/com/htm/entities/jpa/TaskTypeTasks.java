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

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The persistent class for the TASKTYPETasks database table
 */
@Entity
@Table(name = "TASKTYPETASKS")
public class TaskTypeTasks implements Serializable, WrappableEntity {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private TaskTypeTasksPK id;

    //bi-diractional many-to-one association to TaskType
    @ManyToOne
    private Humantaskinstance task;

    public TaskTypeTasks() {}

    public TaskTypeTasksPK getId() {
        return id;
    }

    public void setId(TaskTypeTasksPK id) {
        this.id = id;
    }

    public Humantaskinstance getTask() {
        return task;
    }

    public void setTask(Humantaskinstance task) {
        this.task = task;
    }
}
