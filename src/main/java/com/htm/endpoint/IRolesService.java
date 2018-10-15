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
package com.htm.endpoint;

import org.springframework.stereotype.Repository;

@Repository
public interface IRolesService {
    String createRole(String roleName, String[] genericHumanRoles);

    String getRole(String id);

    String getAllRoles();

    String getRoleUsers(String role);

    String getRoleTaskTypes(String role);

    boolean updateRole(String id, String[] genericHumanRoles);

    boolean deleteRole(String id);
}
