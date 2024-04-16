package com.leron.api.service.dolar;

import com.leron.api.model.DTO.dolar.DollarResponse;
import com.leron.api.responses.DataResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class DollarService {
    public static DataResponse<DollarResponse> getDolar() {
        DataResponse<DollarResponse> dataResponse = new DataResponse<>();
        DollarResponse dollarResponse = new DollarResponse();

        try {
            URL url = new URL("https://economia.awesomeapi.com.br/last/USD-BRL");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            String jsonResponse = response.toString();
            dollarResponse.setValue(extract(jsonResponse));

            connection.disconnect();

            dataResponse.setSeverity("success");
            dataResponse.setMessage("success");

        } catch (Exception e) {
            dataResponse.setSeverity("error");
            dataResponse.setMessage("DOLLAR_NOT_FOUND");
        }
        dataResponse.setData(dollarResponse);

        return dataResponse;
    }

    public static double extract(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONObject usdToBrlObject = jsonObject.getJSONObject("USDBRL");

        String value = usdToBrlObject.getString("ask");
        return Double.parseDouble(value);
    }
}
