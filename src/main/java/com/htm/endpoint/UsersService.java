package com.htm.endpoint;

import org.springframework.stereotype.Repository;

@Repository
public interface UsersService {
        String NAME = "usersService";
        //hier soll dann die neue ID zurück gegeben werden
        String createUser(String json);

        String getUser(String id);

        String getAllUsers();

        String updateUser(String json, String id);

        String deleteUser(String id);
}
