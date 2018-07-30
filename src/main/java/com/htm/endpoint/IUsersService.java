package com.htm.endpoint;

import org.springframework.stereotype.Repository;

@Repository
public interface IUsersService {
        String NAME = "usersService";

        String createUser(String userId, String firstname, String lastname, String[] groups);

        String getUser(String id);

        String getAllUsers();

        boolean updateUser(String firstname, String lastname, String[] groups, String id);

        boolean deleteUser(String id);
}
