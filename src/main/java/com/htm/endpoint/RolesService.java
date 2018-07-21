package com.htm.endpoint;

public interface RolesService {
    String createRole(String json);

    String getRole(String id);

    String getAllRoles();

    String getRoleUsers();

    String updateRole(String json, String id);

    String deleteRole(String id);
}
