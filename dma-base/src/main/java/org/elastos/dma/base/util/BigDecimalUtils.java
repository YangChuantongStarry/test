package org.elastos.dma.base.util;

import java.math.BigDecimal;

/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2019/1/10 14:48
 */
public class BigDecimalUtils {


    //加法
    public static BigDecimal add(Object val1, Object val2) {
        BigDecimal bigDecimal1 = new BigDecimal(val1.toString());
        BigDecimal bigDecimal2 = new BigDecimal(val2.toString());
        return bigDecimal1.add(bigDecimal2);
    }

    //减法
    public static BigDecimal subtract(Object val1, Object val2) {
        BigDecimal bigDecimal1 = new BigDecimal(val1.toString());
        BigDecimal bigDecimal2 = new BigDecimal(val2.toString());
        return bigDecimal1.subtract(bigDecimal2);
    }

    //乘法
    public static BigDecimal multiply(Object val1, Object val2) {
        BigDecimal bigDecimal1 = new BigDecimal(val1.toString());
        BigDecimal bigDecimal2 = new BigDecimal(val2.toString());
        return bigDecimal1.multiply(bigDecimal2);
    }

    //除法
    //scale 保留小数位
    public static BigDecimal divide(Object val1, Object val2, int scale) {
        BigDecimal bigDecimal1 = new BigDecimal(val1.toString());
        BigDecimal bigDecimal2 = new BigDecimal(val2.toString());
        return bigDecimal1.divide(bigDecimal2, scale, BigDecimal.ROUND_HALF_EVEN);
    }

    //比较大小
    // -1,表示bigdemical小于bigdemical2；
    // 0表示bigdemical等于bigdemical2；
    // 1 表示bigdemical大于bigdemical2；
    public static int compareTo(String val1, String val2) {
        BigDecimal bigDecimal1 = new BigDecimal(val1);
        BigDecimal bigDecimal2 = new BigDecimal(val2);
        return bigDecimal1.compareTo(bigDecimal2);

    }


}
