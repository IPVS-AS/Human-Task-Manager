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
public class TaskTypeGroupsPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "TASKTYPE_ID")
    private int taskTypeId;

    @Column(name = "GROUP_ID", insertable = false, updatable = false)
    private int groupId;

    public TaskTypeGroupsPK() {
    }

    public int getTaskTypId() {
        return this.taskTypeId;
    }

    public void setTaskTypId(int taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    public int getGroupId() {
        return this.groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TaskTypeGroupsPK)) {
            return false;
        }
        TaskTypeGroupsPK castOther = (TaskTypeGroupsPK) other;
        return
                (this.taskTypeId == castOther.taskTypeId)
                        && (this.groupId == castOther.groupId);

    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.taskTypeId;
        hash = hash * prime + this.groupId;

        return hash;
    }
}
