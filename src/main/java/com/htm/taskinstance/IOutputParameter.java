package com.htm.taskinstance;

import com.htm.dm.IDataModelElement;

/**
 * Output parameters represent the data which the user is supposed to fill in during the
 * execution of a task. A output parameter is filled if value is not null anymore.
 */
public interface IOutputParameter extends IDataModelElement {

    public String getId();

    public String getLabel();

    public void setLabel(String label);

    public String getValue();

    public void setValue(String value);

    public void setTask(ITaskInstance task);

    public ITaskInstance getTask(ITaskInstance task);
}
