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
import com.htm.entities.jpa.OutputParameter;
import com.htm.taskinstance.IOutputParameter;
import com.htm.taskinstance.ITaskInstance;
import com.htm.utils.Utilities;

import java.io.Serializable;

public class OutputParameterWrapper implements IOutputParameter, Serializable {
    private static final long serialVersionUID = 1L;

    protected OutputParameter outputParameterEntity;

    public OutputParameterWrapper(OutputParameter adaptee) {
        this.outputParameterEntity = adaptee;
    }

    public OutputParameterWrapper(String label) {
        this.outputParameterEntity = new OutputParameter();
        this.setLabel(label);
    }

    @Override
    public String getId() {
        return Integer.toString(outputParameterEntity.getId());
    }

    @Override
    public void accept(IPersistenceVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public WrappableEntity getAdaptee() {
        return outputParameterEntity;
    }

    @Override
    public String getLabel() {
        return outputParameterEntity.getLabel();
    }

    @Override
    public void setLabel(String label) {
        outputParameterEntity.setLabel(label);
    }

    @Override
    public String getValue() {
        return outputParameterEntity.getValue();
    }

    @Override
    public void setValue(String value) {
        outputParameterEntity.setValue(value);
    }

    @Override
    public void setTask(ITaskInstance task) {
        Humantaskinstance taskInstanceEntity = (Humantaskinstance) task.getAdaptee();
        outputParameterEntity.setTask(taskInstanceEntity);
    }

    public OutputParameterWrapper() {
    }

    @Override
    public ITaskInstance getTask(ITaskInstance task) {

        return Utilities.createTaskInstanceFromEntity(outputParameterEntity.getTask());
    }
}
