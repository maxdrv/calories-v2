package com.home.calories.util;

import java.util.Comparator;

public class Comparators {

    public static Comparator<Long> LONG_ASC = Comparator.naturalOrder();
    public static Comparator<Long> LONG_DESC = Comparator.reverseOrder();
    public static Comparator<String> STRING_ASC = Comparator.naturalOrder();
    public static Comparator<String> STRING_DESC = Comparator.reverseOrder();

}
