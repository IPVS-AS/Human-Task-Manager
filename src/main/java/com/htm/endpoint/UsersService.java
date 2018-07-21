package com.htm.endpoint;

public interface UsersService {

        //hier soll dann die neue ID zurück gegeben werden
        String createUser(String json);

        String getUser(String id);

        String getAllUsers();

        String updateUser(String json, String id);

        String deleteUser(String id);
}
