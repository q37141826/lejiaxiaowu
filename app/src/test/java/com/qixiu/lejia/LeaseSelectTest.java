package com.qixiu.lejia;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Long on 2018/4/28 0028
 */
public class LeaseSelectTest {

    @Test
    public void testLeaseSelect() {
        int start = disablePeriods(5);
        Assert.assertEquals(start, 2);
    }

    private int disablePeriods(int lease) {
        if (0 < lease && lease < 3) {
            return 1;
        } else if (3 <= lease && lease < 6) {
            return 2;
        } else if (6 <= lease && lease < 12) {
            return 3;
        } else {
            return -1;
        }
    }


}
