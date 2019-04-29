package org.samanladislav.zonky.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.samanladislav.zonky.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Basic tests for the service.
 */
@RunWith(SpringRunner.class)
@RestClientTest(ApplicationConfig.class)
public class LoanClientTest {

    @Autowired
    private LoanClient loanClient;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${endpoint.uri}")
    private String apiUrl;

    private MockRestServiceServer restServer;

    @Before
    public void setUp() {
        restServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void getLoans() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Total", "20");

        String count = "20";
        restServer.expect(requestTo(containsString(apiUrl)))
                .andExpect(method(GET))
                .andExpect(header("X-Size", "1"))
                .andRespond(withSuccess(getClassPathResource("/loans-response-single.json"), APPLICATION_JSON_UTF8).headers(headers));

        restServer.expect(requestTo(containsString(apiUrl)))
                .andExpect(method(GET))
                .andExpect(header("X-Size", "20"))
                .andRespond(withSuccess(getClassPathResource("/loans-response.json"), APPLICATION_JSON_UTF8).headers(headers));


        Quote[] quotes = loanClient.getAllLoans();

        Assert.assertEquals(20, quotes.length);
        Assert.assertEquals("Nový sporák", quotes[0].getName());
        Assert.assertEquals(457161, quotes[0].getId());
    }

    @Test
    public void getLoansEmptyResult() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Total", "20");

        restServer.expect(requestTo(containsString(apiUrl)))
                .andExpect(method(GET))
                .andExpect(header("X-Size", "1"))
                .andRespond(withSuccess().headers(headers));

        restServer.expect(requestTo(containsString(apiUrl)))
                .andExpect(method(GET))
                .andExpect(header("X-Size", "20"))
                .andRespond(withSuccess().headers(headers));

        Quote[] quotes = loanClient.getAllLoans();
    }

    @Test(expected = RestClientException.class)
    public void getLoansInvalidJsonResult() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Total", "20");

        restServer.expect(requestTo(containsString(apiUrl)))
                .andExpect(method(GET))
                .andExpect(header("X-Size", "1"))
                .andRespond(withSuccess().headers(headers));

        restServer.expect(requestTo(containsString(apiUrl)))
                .andExpect(method(GET))
                .andExpect(header("X-Size", "20"))
                .andRespond(withSuccess("[", APPLICATION_JSON_UTF8).headers(headers));

        Quote[] quotes = loanClient.getAllLoans();
    }

    @Test(expected = IllegalArgumentException.class)
    public void getLoansNoTotalHeader() {
        restServer.expect(requestTo(containsString(apiUrl)))
                .andExpect(method(GET))
                .andExpect(header("X-Size", "1"))
                .andRespond(withSuccess());

        Quote[] quotes = loanClient.getAllLoans();
    }

    @Test(expected = HttpServerErrorException.class)
    public void getLoansInvalidResponse() {
        restServer.expect(requestTo(containsString(apiUrl)))
                .andExpect(method(GET))
                .andExpect(header("X-Size", "1"))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        Quote[] quotes = loanClient.getAllLoans();
    }

    private ClassPathResource getClassPathResource(String pathUrl) {
        return new ClassPathResource(pathUrl, getClass());
    }
}