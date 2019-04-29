package org.samanladislav.zonky.service;

import org.samanladislav.zonky.model.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Implementation of client for reading REST endpoint.
 */
@Service
public class LoanClient implements ILoanClient {

    private static final Logger LOG = LoggerFactory.getLogger(LoanClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${endpoint.uri}")
    private String zonkyUrl;

    @Value("${uri.set.date.number}")
    private int minusDate;

    @Value("${limit.page.loans}")
    private int pageLimit;

    @Override
    public Quote[] getAllLoans() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Size", getNumberOfLoans().toString());

        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<Quote[]> response = restTemplate.exchange(getUrlWithDate(), HttpMethod.GET, entity, Quote[].class);

        return response.getBody();
    }

    private Integer getNumberOfLoans() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Size", "1");

        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<Quote[]> response = restTemplate.exchange(getUrlWithDate(), HttpMethod.GET, entity, Quote[].class);

        List<String> totalLoansList = response.getHeaders().get("X-Total");
        if ((totalLoansList != null) && (totalLoansList.size() == 1)) {
            int totalLoans = Integer.parseInt(totalLoansList.get(0));
            if (totalLoans > pageLimit) {
                totalLoans = pageLimit;
                LOG.warn("Number of loans on page exceeds limit - {}", totalLoans);
            }
            return totalLoans;
        } else {
            throw new IllegalArgumentException("Invalid parametr X-Total " + totalLoansList);
        }
    }

    private String addUrlDateTime() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, -minusDate);
        Date modifiedDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String urlDate = sdf.format(modifiedDate).toString();
        return urlDate;
    }

    private String getUrlWithDate() {
        return zonkyUrl + addUrlDateTime();
    }
}
