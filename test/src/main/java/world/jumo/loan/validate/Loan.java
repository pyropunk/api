/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Loan.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package world.jumo.loan.validate;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 */
public class Loan implements Product<BigDecimal> {

    String msisdn;
    String network;
    LocalDate date;
    String product;
    BigDecimal amount;

    public Loan(String msisdn, String network, LocalDate date, String product, BigDecimal amount) {

        super();
        this.msisdn = msisdn;
        this.network = network;
        this.date = date;
        this.product = product;
        this.amount = amount;
    }

    public String getMsisdn() {

        return msisdn;
    }

    public void setMsisdn(String msisdn) {

        this.msisdn = msisdn;
    }

    public String getNetwork() {

        return network;
    }

    public void setNetwork(String network) {

        this.network = network;
    }

    public LocalDate getDate() {

        return date;
    }

    public void setDate(LocalDate date) {

        this.date = date;
    }

    public String getProduct() {

        return product;
    }

    public void setProduct(String product) {

        this.product = product;
    }

    public BigDecimal getAmount() {

        return amount;
    }

    public void setAmount(BigDecimal amount) {

        this.amount = amount;
    }

}
