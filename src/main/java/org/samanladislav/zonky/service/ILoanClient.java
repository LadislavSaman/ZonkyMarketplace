package org.samanladislav.zonky.service;

import org.samanladislav.zonky.model.Quote;

/**
 * Client for reading REST endpoint.
 */
public interface ILoanClient {
    Quote[] getAllLoans();
}
