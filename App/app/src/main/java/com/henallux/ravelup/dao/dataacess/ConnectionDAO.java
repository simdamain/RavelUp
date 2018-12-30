package com.henallux.ravelup.dao.dataacess;

import com.henallux.ravelup.model.CityModel;
import com.henallux.ravelup.model.LoginModel;
import com.henallux.ravelup.model.TokenReceived;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.*;
import com.henallux.ravelup.model.UserModel;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ConnectionDAO{
    private Gson gsonBuilder = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .serializeNulls()
            .create();

    public TokenReceived checkLogin(LoginModel user){

        TokenReceived tokenReceivedCode = new TokenReceived();

        try {

            URL url = new URL("http://ravelapidb.azurewebsites.net/api/Jwt");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type","application/json");

            connection.setDoOutput(true);
            connection.setDoInput(true);

            OutputStream outputStream = connection.getOutputStream();
            OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);

            connection.connect();

            streamWriter.write(gsonBuilder.toJson(user));
            streamWriter.flush();
            streamWriter.close();

            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            java.util.Scanner scanner = new java.util.Scanner(inputStream).useDelimiter("\\A");
            String token = scanner.hasNext() ? scanner.next() : "";

            outputStream.close();
            connection.disconnect();

            JSONObject tokenReceived = new JSONObject(token);
            tokenReceivedCode.setToken(tokenReceived.getString("acces_token"));

            if (!tokenReceivedCode.getToken().equals("")) {
                tokenReceivedCode.setCode(connection.getResponseCode());
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.FRENCH);
                Date currentDate = new Date();
                Long durationToken = Long.parseLong(tokenReceived.getString("expires_in"));
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(currentDate.getTime() + durationToken);
                Date expirationDate = cal.getTime();
                dateFormat.format(expirationDate);
                tokenReceivedCode.setExpirationDate(expirationDate);
            }
        }
        catch(IOException e){
            // TODO: 17/12/2018  a modifier et refaire les exceptions
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tokenReceivedCode;
    }

    public void signUp(UserModel user){

        try {

            //Todo change url
            URL url = new URL("http://ravelapidb.azurewebsites.net/api/User");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type","application/json");

            connection.setDoInput(true);

            OutputStream outputStream = connection.getOutputStream();
            OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);

            connection.connect();

            streamWriter.write(gsonBuilder.toJson(user));
            streamWriter.flush();
            streamWriter.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String test = br.readLine();
            outputStream.close();
            connection.disconnect();

        }
        catch(IOException e){
            // TODO: 17/12/2018  a modifier et refaire les exceptions
            e.printStackTrace();
        }

    }

    public ArrayList<CityModel> getAllCities()throws Exception{

        URL url = new URL("http://ravelapidb.azurewebsites.net/api/Ville/villes");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-type","application/json");
        BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String stringJSON = "", line;
        while ((line = buffer.readLine()) != null) {
            builder.append(line);
        }
        buffer.close();
        stringJSON = builder.toString();
        return jsonToCities(stringJSON);
    }

    private ArrayList<CityModel>jsonToCities(String stringJSON) throws Exception {
        ArrayList<CityModel> cities= new ArrayList<>();
        CityModel city;
        JSONArray jsonArray=new JSONArray(stringJSON);
        for (int i=0;i<jsonArray.length();i++){
            JSONObject jsonCity = jsonArray.getJSONObject(i);
            city= new CityModel(jsonCity.getInt("id"),jsonCity.getString("libelle"),jsonCity.getInt("codePostal"));
            cities.add(city);
        }
        return cities;
    }
}
