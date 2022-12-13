package stepIntegration;


import com.fasterxml.jackson.core.JsonProcessingException;
import config.Request;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import model.EnrichTransactionsRequest;
import model.OAuthTokenRequest;
import model.TransactionForEnrichment;
import model.World;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.module.jsv.JsonSchemaValidatorSettings.settings;

@CucumberContextConfiguration
public class StepDefs {

    private final World world = new World();
    private final Request request = new Request();

    @Given("a {string} call to {string}")
    public void aCallTo(String httpMethod, String endPoint) {
        world.setMethod(httpMethod);
        world.setEndPointUrl(endPoint);
    }

    @When("performed HTTP request with body:")
    public void performedHTTPRequestWithBody(DataTable dataTable) throws JsonProcessingException {
        world.setResponse(request.processHttpRequest(world.getMethod(), world.getEndPointUrl(), new OAuthTokenRequest(dataTable.row(0).get(1), dataTable.row(1).get(1))));
    }

    @When("performed OAuth HTTP request with body:")
    public void performedAuthorisedHTTPRequestWithBody(DataTable dataTable) throws JsonProcessingException {
        List<String> items= Stream.of(dataTable.row(1).get(1).split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        world.setResponse(request.processHttpRequestWithAuth(world.getMethod(), world.getEndPointUrl(), new EnrichTransactionsRequest(Collections.singletonList(dataTable.row(0).get(1)), new TransactionForEnrichment(items.get(0),items.get(1),items.get(2),items.get(3))),"auth"));
    }

    @When("performed Not Authorised HTTP request with body:")
    public void performedNotAuthorisedHTTPRequestWithBody(DataTable dataTable) throws JsonProcessingException {
        List<String> items= Stream.of(dataTable.row(1).get(1).split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        world.setResponse(request.processHttpRequestWithAuth(world.getMethod(), world.getEndPointUrl(), new EnrichTransactionsRequest(Collections.singletonList(dataTable.row(0).get(1)), new TransactionForEnrichment(items.get(0),items.get(1),items.get(2),items.get(2))),"unauth"));
    }

    @When("performed Expired OAuth HTTP request with body:")
    public void performedExpiredHTTPRequestWithBody(DataTable dataTable) throws JsonProcessingException {
        List<String> items= Stream.of(dataTable.row(1).get(1).split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        world.setResponse(request.processHttpRequestWithAuth(world.getMethod(), world.getEndPointUrl(), new EnrichTransactionsRequest(Collections.singletonList(dataTable.row(0).get(1)), new TransactionForEnrichment(items.get(0),items.get(1),items.get(2),items.get(2))),"expired"));
    }


    @Then("response HTTP status is {int}")
    public void responseHTTPStatusIs(int responseCode) {
        request.verifyResponseStatusValue(world.getResponse(), responseCode);
    }

    @And("response body contains {string} error")
    public void responseBodyContainsError(String error) {
        request.verfiyErrorMessage(world.getResponse(), error);
    }

    @When("performed HTTP request with invalid body:")
    public void performedHTTPRequestWithInvalidBody(DataTable table){
        Map<String,String> tempMap = table.asMap(String.class,String.class);
        JSONObject jsonObject = new JSONObject();
        for(Map.Entry<String,String> entry:tempMap.entrySet()){
            jsonObject.put(entry.getKey(),entry.getValue());
        }
        world.setResponse(request.requestWithInvalidFieldNames(world.getEndPointUrl(),jsonObject.toString()));
    }

    @And("validate {string}")
    public void validate(String expected) {
        world.getResponse().then().body(matchesJsonSchemaInClasspath("/jsonSchemas/"+expected+"Schema.json").using(settings().with().checkedValidation(true)));
    }
}