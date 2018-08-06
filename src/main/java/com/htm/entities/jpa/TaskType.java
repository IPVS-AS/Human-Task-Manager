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
