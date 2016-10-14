package com.li.videoapplication.tools;


import com.google.gson.Gson;

public class JSONHelper {

    public static final Gson GSON = new Gson();

    public static <T> T parse(String text, Class<T> clazz) {
        return parseGson(text, clazz);
    }

    public static String to(Object o) {
        return toGSON(o);
    }

    private static <T> T parseGson(String text, Class<T> clazz) {
        return GSON.fromJson(text, clazz);
    }

    private static String toGSON(Object o) {
        return GSON.toJson(o);
    }
}
