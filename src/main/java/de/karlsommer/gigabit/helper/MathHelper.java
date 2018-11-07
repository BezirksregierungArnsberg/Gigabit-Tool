package de.karlsommer.gigabit.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathHelper {


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd;
        try {
            bd = new BigDecimal(value);
        }catch (Exception e)
        {
            return 0;
        }
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
