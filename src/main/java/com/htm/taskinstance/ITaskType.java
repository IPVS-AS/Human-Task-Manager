package com.htm.taskinstance;

import com.htm.dm.IDataModelElement;
import com.htm.userdirectory.IGroup;

/**
 * Task types represent the different kinds of possible task. It makes it possible to group task with a similar content/behavior.
 * Furthermore it is possible to map task types to groups of users.
 */
public interface ITaskType extends IDataModelElement {

    public String getId();

    public String getTaskTypName();

    public void setTaskTypName(String taskTypName);

    public void addGroup(IGroup goup);

    public void addTask(ITaskInstance task);
}
