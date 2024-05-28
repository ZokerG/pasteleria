package com.example.pasteleria.response;

import lombok.Data;

@Data
public class LoadUserResponse {
    private long id;
    private String  username;
    private String password;
    private boolean enable;
}
