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
