package com.pattern.food_ordering_system.service.user;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class DeliveryTimeService {
    public static double getDeliveryTimeInMinutes(String originZone, String destinationZone) {
        double[] o = CairoLocations.COORDS.get(originZone);
        double[] d = CairoLocations.COORDS.get(destinationZone);

        if (o == null || d == null) {
            return 999;
        }

        try {
            String urlStr = String.format(
                    Locale.US,
                    "https://router.project-osrm.org/route/v1/driving/%.6f,%.6f;%.6f,%.6f",
                    o[1], o[0], d[1], d[0]
            );

          //  System.out.println("URL = " + urlStr);

            HttpURLConnection con = (HttpURLConnection) new URL(urlStr).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "FoodOrderingApp/1.0");

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) response.append(line);


            JSONObject obj = new JSONObject(response.toString());
            JSONObject route = obj.getJSONArray("routes").getJSONObject(0);

            double durationSeconds = route.getDouble("duration");
            double minutes = durationSeconds / 60.0;


            return minutes;

        } catch (Exception e) {
            e.printStackTrace();
            return 999;
        }
    }
}