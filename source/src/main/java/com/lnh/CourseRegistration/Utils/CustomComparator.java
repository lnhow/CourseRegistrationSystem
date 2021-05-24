package com.lnh.CourseRegistration.Utils;

import java.time.LocalDateTime;
import java.util.Comparator;

public class CustomComparator {
    public static class ComparatorString implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            if (o1.length() > o2.length()) {
                return 1;
            } else if (o1.length() < o2.length()) {
                return -1;
            } else {    //2 string lengths are equals
                return o1.compareTo(o2);
            }
        }
    }

    public static class ComparatorInt implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    }

    public static class ComparatorFloat implements Comparator<Float> {
        @Override
        public int compare(Float o1, Float o2) {
            return o1.compareTo(o2);
        }
    }

    public static class ComparatorLong implements Comparator<Long> {
        @Override
        public int compare(Long o1, Long o2) {
            return o1.compareTo(o2);
        }
    }

    public static class ComparatorLocalDateTime implements Comparator<LocalDateTime> {
        @Override
        public int compare(LocalDateTime o1, LocalDateTime o2) {
            return o1.compareTo(o2);
        }
    }
}
