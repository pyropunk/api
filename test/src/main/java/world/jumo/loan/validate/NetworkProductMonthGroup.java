/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * NetworkProductMonthGroup.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package world.jumo.loan.validate;

import java.time.Month;

/**
 *
 */
public class NetworkProductMonthGroup {

    String network;
    String product;
    Month month;

    public NetworkProductMonthGroup(String network, String product, Month month) {

        super();
        this.network = network;
        this.product = product;
        this.month = month;
    }

    public String getNetwork() {

        return network;
    }

    public void setNetwork(String network) {

        this.network = network;
    }

    public String getProduct() {

        return product;
    }

    public void setProduct(String product) {

        this.product = product;
    }

    public Month getMonth() {

        return month;
    }

    public void setMonth(Month month) {

        this.month = month;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((month == null) ? 0 : month.hashCode());
        result = prime * result + ((network == null) ? 0 : network.hashCode());
        result = prime * result + ((product == null) ? 0 : product.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NetworkProductMonthGroup other = (NetworkProductMonthGroup) obj;
        if (month == null) {
            if (other.month != null)
                return false;
        }
        else if ( !month.equals(other.month))
            return false;
        if (network == null) {
            if (other.network != null)
                return false;
        }
        else if ( !network.equals(other.network))
            return false;
        if (product == null) {
            if (other.product != null)
                return false;
        }
        else if ( !product.equals(other.product))
            return false;
        return true;
    }

}
