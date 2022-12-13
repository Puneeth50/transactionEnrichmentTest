package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthTokenRequest {

    public String client_id;

    public String client_secret;

    public OAuthTokenRequest(String client_id, String client_secret) {
        this.client_id = client_id;
        this.client_secret = client_secret;
    }
    public OAuthTokenRequest() {
    }
}
