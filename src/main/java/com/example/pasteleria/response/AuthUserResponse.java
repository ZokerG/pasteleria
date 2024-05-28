package com.example.pasteleria.response;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class AuthUserResponse {

    private String accessToken;
    private String provider;
    private boolean accountCreated;
    private UserInfoResponse userInfo;

}
