package com.htm.endpoint;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public interface IRolesService {
    String createRole(String roleName, String[] genericHumanRoles);

    String getRole(String id);

    String getAllRoles();

    String getRoleUsers();

    String updateRole(String json, String id);

    boolean deleteRole(String id);
}
