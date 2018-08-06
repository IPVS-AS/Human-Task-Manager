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
