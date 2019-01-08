package com.henallux.ravelup.dao.dataacess;

import com.henallux.ravelup.exeption.CityException;
import com.henallux.ravelup.exeption.LoginExecption;
import com.henallux.ravelup.exeption.SignUpException;
import com.henallux.ravelup.model.CityModel;
import com.henallux.ravelup.model.LoginModel;
import com.henallux.ravelup.model.TokenReceivedModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.*;
import com.henallux.ravelup.model.UserModel;

import java.io.BufferedReader;
import java.io.IOException;
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

    public TokenReceivedModel checkLogin(LoginModel user) throws LoginExecption {
        try {
            TokenReceivedModel tokenReceived = new TokenReceivedModel();

            URL url = new URL("http://ravelapidb.azurewebsites.net/api/Jwt");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            connection.setDoOutput(true);
            connection.setDoInput(true);

            OutputStream outputStream = connection.getOutputStream();
            OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);

            connection.connect();

            streamWriter.write(gsonBuilder.toJson(user));
            streamWriter.flush();
            streamWriter.close();

            BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String jsonString = "", line;
            while ((line = buffer.readLine()) != null) {
                builder.append(line);
            }
            buffer.close();
            jsonString = builder.toString();

            outputStream.close();
            connection.disconnect();

            JSONObject tokenReceivedJSON = new JSONObject(jsonString);
            tokenReceived.setToken(tokenReceivedJSON.getString("acces_token"));

            if (!tokenReceived.getToken().equals("")) {
                tokenReceived.setCode(connection.getResponseCode());
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.FRENCH);
                Date currentDate = new Date();
                Long durationToken = Long.parseLong(tokenReceivedJSON.getString("expires_in"));
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(currentDate.getTime() + durationToken);
                Date expirationDate = cal.getTime();
                dateFormat.format(expirationDate);
                tokenReceived.setExpirationDate(expirationDate);
            }
            return tokenReceived;
        }
        catch (JSONException e) {
            throw new LoginExecption("Les informations sont incorrects");
        }catch (IOException e){
            throw  new LoginExecption("L'utilisateur est introuvable");
        }

    }

    public void signUp(UserModel user) throws SignUpException  {

        try {
            URL url = new URL("http://ravelapidb.azurewebsites.net/api/User");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

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
        } catch (IOException e) {
            throw  new SignUpException("Les informations sont incorrects ou déjà existante");
        }

    }

    public ArrayList<CityModel> getAllCities() throws CityException{

        try {
            URL url = new URL("http://ravelapidb.azurewebsites.net/api/Ville/villes");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "application/json");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String stringJSON = "", line;
            while ((line = buffer.readLine()) != null) {
                builder.append(line);
            }
            buffer.close();
            stringJSON = builder.toString();
            return jsonToCities(stringJSON);
        } catch (IOException e) {
            throw  new CityException("Erreur de connexion à la base de données");
        } catch (JSONException e) {
            throw  new CityException("Les villes n'ont pas été trouvées");
        }
    }

    private ArrayList<CityModel>jsonToCities(String stringJSON) throws  JSONException {
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
