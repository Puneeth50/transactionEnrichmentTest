@Regression
Feature: Authentication Scenarios

  @Authentication @HappyPath @Smoke
  Scenario Outline: Get a successful authentication token for the platform
    Given a "POST" call to "/v1/oauth/token"
    When performed HTTP request with body:
      | client_id     | <clientId>     |
      | client_secret | <clientSecret> |
    Then response HTTP status is 200
    And validate "OAuthResponse"
    Examples:
      | clientId       | clientSecret |
      | clientID123456 | 1234567890   |

  @Authentication @HappyPath @Smoke
  Scenario Outline: unsuccessful authentication due to invalid endpoint <endPoint>
    Given a "POST" call to "<endPoint>"
    When performed HTTP request with body:
      | client_id     | clientID123456 |
      | client_secret | 1234567890     |
    Then response HTTP status is 400
    And validate "OAuthErrorResponse"
    Examples:
      | endPoint        |
      | /v1/token       |
      | /v2/oauth/token |
      | /oauth/token    |

  @Authentication @Negative @Smoke
  Scenario Outline: unsuccessful authentication due to invalid body
    Given a "POST" call to "/v1/oauth/token"
    When performed HTTP request with body:
      | client_id     | <clientId>     |
      | client_secret | <clientSecret> |
    Then response HTTP status is 500
    And validate "OAuthErrorResponse"
    Examples:
      | clientId | clientSecret   |
      | invalid  | invalid secret |
      |          | valid secret   |
      | valid    |                |
      | £$%%     | ^&&*           |
