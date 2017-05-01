package com.herce.pf_moviles.Objects;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Herce on 30/04/2017.
 */

public class Categories {
    Map<Integer,String> categoryList = new HashMap<Integer, String>();

    private static final Categories holder = new Categories();

    public static Categories getInstance() {
        return holder;
    }
}
