package model;

import lombok.Data;

@Data
public class OAuthTokenResponse {

    private String access_token;

    private String expires_in;
}
