package org.samanladislav.zonky.service;

import org.samanladislav.zonky.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Date;

/**
 * Periodical job for search new loans.
 */
@Service
public class FetchService {

    private boolean firstCycle = true;
    @Autowired
    private ILoanClient client;

    @Value("${scheduler.period}")
    private int period;

    /**
     * Run method periodically.
     */
    @Scheduled(fixedDelayString = "${scheduler.period}")
    public void run() {
        if (firstCycle) {
            getAllQuotes();
            firstCycle = false;
        } else {
            getNewQuotes();
        }
    }

    private void getAllQuotes() {
        System.out.println("------Start - full list------\n");
        Quote[] quotes = client.getAllLoans();
        for (int i = quotes.length - 1; i >= 0; i--) {
            printQuote(quotes[i]);
        }
        System.out.println("------End - full list------\n");
    }

    private void getNewQuotes() {
        Date actualTimeMinusPeriod = new Date(System.currentTimeMillis() - period);
        Quote[] quotes = client.getAllLoans();
        System.out.println("------Start - updated list------\n");
        for (int i = quotes.length - 1; i >= 0; i--) {
            Date dateFromApi = quotes[i].getDatePublished();
            if (actualTimeMinusPeriod.compareTo(dateFromApi) < 0) {
                printQuote(quotes[i]);
            }
        }
        System.out.println("------End - updated list------\n");
    }

    private void printQuote(Quote quote) {
        System.out.println(quote.getId());
        System.out.println(quote.getName());
        System.out.println(quote.getAmount());
        System.out.println(quote.getDatePublished());
        System.out.println();
    }

}
