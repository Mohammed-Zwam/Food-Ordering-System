package com.pattern.food_ordering_system.utils;

import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Locations {
    public static final Map<String, Map<String, Double>> deliveryTimeCache = new HashMap<>();
    public static final List<String> ZONES = new ArrayList<>();

    static {
        ZONES.add("El Marg");
        ZONES.add("Tagammu");
        ZONES.add("Abbasiya");
        ZONES.add("Ain Shams");
        ZONES.add("Ezbet El Nakhl");
        ZONES.add("Helmeyat El Zaytoun");
        ZONES.add("Matareya");
        ZONES.add("Gesr El Suez");
        ZONES.add("Nasr City");
        ZONES.add("Masaken Sheraton");
        ZONES.add("Heliopolis");
        ZONES.add("Nozha");
        ZONES.add("New Nozha");
        ZONES.add("Rehab");
        ZONES.add("Tagammu 1");
        ZONES.add("Tagammu 3");
        ZONES.add("Maadi");
        ZONES.add("Zahraa Maadi");
        ZONES.add("Dar El Salam");
        ZONES.add("El Basatin");
        ZONES.add("Shubra");
        ZONES.add("Rod El Farag");
        ZONES.add("Bulaq");
        ZONES.add("Zamalek");
        ZONES.add("Dokki");
        ZONES.add("Mohandessin");
        ZONES.add("Agouza");
        ZONES.add("Haram");
        ZONES.add("Faisal");
        ZONES.add("6th October");
        ZONES.add("Sheikh Zayed");
        ZONES.add("Imbaba");
    }

    static {
        try {
            String content = new String(Files.readAllBytes(Paths.get("src/main/resources/delivery_times.json")));
            JSONObject obj = new JSONObject(content);

            for (String from : obj.keySet()) {
                Map<String, Double> toMap = new HashMap<>();
                JSONObject inner = obj.getJSONObject(from);
                for (String to : inner.keySet()) {
                    toMap.put(to, inner.getDouble(to));
                }
                deliveryTimeCache.put(from, toMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}