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
