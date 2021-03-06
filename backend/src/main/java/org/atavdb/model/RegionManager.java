package org.atavdb.model;

import java.util.Arrays;
import java.util.HashSet;

/**
 *
 * @author nick
 */
public class RegionManager {

    private static HashSet<String> chrSet = new HashSet<>();
    public static final int MAX_SEARCH_LIMIT = 1000;

    private static final String[] ALL_CHR = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "X", "Y", "MT"};

    static {
        chrSet.addAll(Arrays.asList(ALL_CHR));
    }

    public static boolean isChrValid(String chr) {
        return chrSet.contains(chr);
    }
}
