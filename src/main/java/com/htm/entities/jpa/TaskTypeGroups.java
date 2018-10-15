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
 * The persistent class for the TASKTYPEGROUPS database table
 */
@Entity
@Table(name = "TASKTYPEGROUPS")
public class TaskTypeGroups implements Serializable, WrappableEntity {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private TaskTypeGroupsPK id;

    //bi-diractional many-to-one association to TaskType
    @ManyToOne
    private Group group;

    public TaskTypeGroups() {}

    public TaskTypeGroupsPK getId() {
        return id;
    }

    public void setId(TaskTypeGroupsPK id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
