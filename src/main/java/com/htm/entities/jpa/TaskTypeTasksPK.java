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

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Primary key class for TASKTYPEGROUPS database table
 */
@Embeddable
public class TaskTypeTasksPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "TASKTYPE_ID")
    private int taskTypeId;

    @Column(name = "TASK_ID", insertable = false, updatable = false)
    private int taskId;

    public TaskTypeTasksPK() {}

    public int getTaskTypeId() {
        return taskTypeId;
    }

    public void setTaskTypeId(int taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TaskTypeTasksPK)) {
            return false;
        }
        TaskTypeTasksPK castOther = (TaskTypeTasksPK) other;
        return
                (this.taskTypeId == castOther.taskTypeId)
                        && (this.taskId == castOther.taskId);

    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.taskTypeId;
        hash = hash * prime + this.taskId;

        return hash;
    }
}
