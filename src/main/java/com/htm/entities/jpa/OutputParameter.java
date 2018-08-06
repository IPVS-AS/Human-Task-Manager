package com.htm.entities.jpa;

import com.htm.entities.WrappableEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the OUTPUTPARAMETER database table.
 */
@Entity
public class OutputParameter implements Serializable, WrappableEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String label;

    private String value;

    //bi-directional many-to-one association to Humantaskinstance
    //TODO: Bi-directional auch bei Humantaskinstance rein
    @ManyToOne
    @JoinColumn(name = "TIID")
    private Humantaskinstance task;

    public OutputParameter() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Humantaskinstance getTask() {
        return task;
    }

    public void setTask(Humantaskinstance task) {
        this.task = task;
    }
}
