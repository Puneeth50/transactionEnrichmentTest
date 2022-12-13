@Regression
Feature: Transaction Enrichment scenarios

  @Enrichment @HappyPath @Smoke
  Scenario Outline: Enrich a set of transactions happy path
    Given a "POST" call to "/v1/enriched-transactions"
    When performed OAuth HTTP request with body:
      | enrichments  | <enrichments>  |
      | transactions | <transactions> |
    Then response HTTP status is 200
    And validate "EnrichTransactionsResponse"
    Examples:
      | enrichments    | transactions        |
      | categorisation | 1,date,10.0,string  |
      | categorisation | 2,date,100.0,string |

  @Enrichment @Negative @Smoke
  Scenario: Unauthorised Transaction Enrichment
    Given a "POST" call to "/v1/enriched-transactions"
    When performed Not Authorised HTTP request with body:
      | enrichments  | categorisation     |
      | transactions | 1,date,10.0,string |
    Then response HTTP status is 401
    And validate "EnrichTransactionsAuthErrorResponse"

  @Enrichment @Negative @Smoke
  Scenario: Forbidden Transaction Enrichment
    Given a "POST" call to "/v1/enriched-transactions"
    When performed Expired OAuth HTTP request with body:
      | enrichments  | categorisation     |
      | transactions | 1,date,10.0,string |
    Then response HTTP status is 403
    And validate "EnrichTransactionsAuthErrorResponse"


  @Enrichment @Negative @Smoke
  Scenario Outline: Bad Transaction Enrichment requests
    Given a "POST" call to "<endPoint>"
    When performed OAuth HTTP request with body:
      | enrichments  | categorisation        |
      | transactions | id,date,amount,string |
    Then response HTTP status is 400
    And validate "EnrichTransactionsAuthErrorResponse"
    Examples:
    Examples:
      | endPoint                 |
      | /v/enriched-transactions |
      | /v1/enrichedtransactions |
      | /v1/transactions         |
      | /v1                      |

  @Enrichment @Negative @Smoke
  Scenario Outline: Invalid Transaction Enrichment requests
    Given a "POST" call to "/v1/enriched-transactions"
    When performed OAuth HTTP request with body:
      | enrichments  | <enrichments>  |
      | transactions | <transactions> |
    Then response HTTP status is 200
    And validate "EnrichTransactionsAuthErrorResponse"
    Examples:
      | enrichments    | transactions         |
      | categorisation | xyz,date,10.0,string |
      | categorisation | id,xyz,10.0,string   |
      | categorisation | id,date,xyz,string   |
      | categorisation | id,date,10.0,1       |