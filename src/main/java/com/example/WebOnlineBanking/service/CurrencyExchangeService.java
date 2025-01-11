package com.example.WebOnlineBanking.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

@Service
public class CurrencyExchangeService {

    private final File apiKeyFile = new File(".\\SecretApiKey");
    private final String apiServiceUrl = "https://api.currencylayer.com/convert";

    public int exchange(String fromCurrency, String toCurrency, int amount) throws IOException {
        String apiKey = getApiKey();
        //set up connection
        HttpURLConnection connection = createApiConnection(apiKey, fromCurrency, toCurrency, amount);
        //get response
        connection.setRequestMethod("GET");
        //read response
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = reader.readLine();
        // Parse JSON response
        JSONObject jsonResponse = new JSONObject(response);
        double result = jsonResponse.getDouble("result");
        return (int) Math.ceil(result);
    }

    private HttpURLConnection createApiConnection(String apiKey,String fromCurrency, String toCurrency, int amount) throws IOException {
        String requestUrl =  apiServiceUrl + "?access_key=" + apiKey + "&from=" + fromCurrency
                + "&to=" + toCurrency + "&amount=" + amount;
        // Create HttpURLConnection object
        return (HttpURLConnection) new URL(requestUrl).openConnection();
    }

    private String getApiKey() throws IOException {
        try(BufferedReader reader = new BufferedReader(new FileReader(apiKeyFile))){
            return reader.readLine();
        }
        catch(FileNotFoundException e){
            System.out.println("Your key for currency exchange api is not set up!\n" +
                    "Make sure to set up in file called `SecretApiKey` in root dir\n"+e);
            throw e;
        }
    }
}
