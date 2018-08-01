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
