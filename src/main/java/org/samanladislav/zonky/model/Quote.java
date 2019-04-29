package org.samanladislav.zonky.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Marketplace loan. It contains important attributes from loan.
 */
public class Quote {
    /**
     * Loan id.
     */
    private int id;
    /**
     * Loan name.
     */
    private String name;
    /**
     * Loan amount.
     */
    private BigDecimal amount;
    /**
     * Loan published date.
     */
    private Date datePublished;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
    }
}
