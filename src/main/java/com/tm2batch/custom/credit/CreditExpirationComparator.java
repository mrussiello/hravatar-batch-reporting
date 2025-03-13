/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tm2batch.custom.credit;

import com.tm2batch.entity.purchase.Credit;
import java.util.Comparator;

/**
 *
 * @author Mike
 */
public class CreditExpirationComparator implements Comparator<Credit> {

    @Override
    public int compare(Credit o1, Credit o2) {

        if( o1.getExpireDate()!= null && o2.getExpireDate()!= null )
            return o1.compareTo(o2);

        return 0;
    }

}
