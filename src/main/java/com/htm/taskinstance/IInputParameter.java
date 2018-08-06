package com.htm.taskinstance;

import com.htm.dm.IDataModelElement;

/**
 * Input parameters represent data which is needed by the user to fulfill the task.
 * For example a specific modelnumber of a component.
 */
public interface IInputParameter extends IDataModelElement {

    public String getId();

    public String getLabel();

    public void setLabel(String label);

    public String getValue();

    public void setValue(String value);

    public void setTask(ITaskInstance task);

    public ITaskInstance getTask(ITaskInstance task);

}
