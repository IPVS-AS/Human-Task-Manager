package com.htm.endpoint;

import org.springframework.stereotype.Repository;

@Repository
public interface IRolesService {
    String createRole(String roleName, String[] genericHumanRoles);

    String getRole(String id);

    String getAllRoles();

    String getRoleUsers(String role);

    boolean updateRole(String id, String[] genericHumanRoles);

    boolean deleteRole(String id);
}
