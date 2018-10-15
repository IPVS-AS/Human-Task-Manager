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

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the INPUTPARAMETER database table.
 */
@Entity
public class InputParameter implements Serializable, WrappableEntity {
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

    public InputParameter() {}

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
