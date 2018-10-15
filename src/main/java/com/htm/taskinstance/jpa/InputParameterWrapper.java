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
package com.htm.taskinstance.jpa;

import com.htm.dm.IPersistenceVisitor;
import com.htm.entities.WrappableEntity;
import com.htm.entities.jpa.Humantaskinstance;
import com.htm.entities.jpa.InputParameter;
import com.htm.taskinstance.IInputParameter;
import com.htm.taskinstance.ITaskInstance;
import com.htm.utils.Utilities;

import java.io.Serializable;

public class InputParameterWrapper implements IInputParameter,Serializable {

    private static final long serialVersionUID = 1L;

    protected InputParameter inputParameterEntity;

    public InputParameterWrapper() {
    }

    public InputParameterWrapper(InputParameter adaptee) {
        this.inputParameterEntity = adaptee;
    }

    public InputParameterWrapper(String label) {
        this.inputParameterEntity = new InputParameter();
        this.setLabel(label);
    }

    @Override
    public String getId() {
        return Integer.toString(inputParameterEntity.getId());
    }

    @Override
    public void accept(IPersistenceVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public WrappableEntity getAdaptee() {
        return inputParameterEntity;
    }

    @Override
    public String getLabel() {
        return inputParameterEntity.getLabel();
    }

    @Override
    public void setLabel(String label) {
        inputParameterEntity.setLabel(label);
    }

    @Override
    public String getValue() {
        return inputParameterEntity.getValue();
    }

    @Override
    public void setValue(String value) {
        inputParameterEntity.setValue(value);
    }

    @Override
    public void setTask(ITaskInstance task) {
        Humantaskinstance taskInstanceEnity = (Humantaskinstance) task.getAdaptee();
        inputParameterEntity.setTask(taskInstanceEnity);
    }

    @Override
    public ITaskInstance getTask(ITaskInstance task) {
        return Utilities.createTaskInstanceFromEntity(inputParameterEntity.getTask());

    }
}
