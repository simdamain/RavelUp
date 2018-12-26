package com.henallux.ravelup.dao.dataacess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henallux.ravelup.model.CategoryModel;
import com.henallux.ravelup.model.PinModel;
import com.henallux.ravelup.model.PointOfInterestModel;
import com.henallux.ravelup.model.TokenReceived;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MapDAO {
    private Gson gsonBuilder = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .serializeNulls()
            .create();
    public ArrayList<CategoryModel> getAllCategories(TokenReceived token)throws Exception{

        URL url = new URL("http://ravelapidb.azurewebsites.net/api/Categorie");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-type","application/json");
        connection.setRequestProperty("Authorization","Bearer " +token.getToken());
        BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String stringJSON = "", line;
        while ((line = buffer.readLine()) != null) {
            builder.append(line);
        }
        buffer.close();
        stringJSON = builder.toString();
        return jsonToCategories(stringJSON);
    }

    private ArrayList<CategoryModel>jsonToCategories(String stringJSON) throws Exception
    {
        ArrayList<CategoryModel> categories= new ArrayList<>();
        CategoryModel category;
        JSONArray jsonArray=new JSONArray(stringJSON);
        for (int i=0;i<jsonArray.length();i++){
            JSONObject jsonCategory = jsonArray.getJSONObject(i);
            category= new CategoryModel(jsonCategory.getLong("id"),jsonCategory.getString("libelle"));
            categories.add(category);
        }
        return categories;
    }

    public ArrayList<PointOfInterestModel> getAllPins(TokenReceived token, PinModel pin)throws Exception{
        URL url = new URL("http://ravelapidb.azurewebsites.net/api/PointInteret/ByCategorie");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-type","application/json");
        connection.setDoInput(true);
        OutputStream outputStream = connection.getOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
        connection.connect();
        streamWriter.write(gsonBuilder.toJson(token));
        streamWriter.write(gsonBuilder.toJson(pin));
        streamWriter.flush();
        streamWriter.close();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String test = br.readLine();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String stringJSON = "", line;
        while ((line = buffer.readLine()) != null) {
            builder.append(line);
        }
        buffer.close();
        stringJSON = builder.toString();
        outputStream.close();
        connection.disconnect();
        return jsonToPointInteret(stringJSON);
    }


    private ArrayList<PointOfInterestModel>jsonToPointInteret(String stringJSON) throws Exception
    {
        ArrayList<PointOfInterestModel> pointOfInterest= new ArrayList<>();
        PointOfInterestModel pin;
        JSONArray jsonArray=new JSONArray(stringJSON);
        for (int i=0;i<jsonArray.length();i++){
            JSONObject jsonPin = jsonArray.getJSONObject(i);
            pin= new PointOfInterestModel();
            pin.setId(jsonPin.getLong("Id"));
            pin.setNom(jsonPin.getString("Nom"));
            pin.setDescription(jsonPin.getString("Description"));
            pin.setLongitude(jsonPin.getDouble("Longitude"));
            pin.setLatitude(jsonPin.getDouble("Latitude"));
            pin.setCategorieId(jsonPin.getLong("CategorieId "));
        }
        return pointOfInterest;
    }
}
