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
