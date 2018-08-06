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
