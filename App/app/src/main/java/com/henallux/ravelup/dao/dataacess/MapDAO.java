package com.henallux.ravelup.dao.dataacess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.henallux.ravelup.exeption.CategoryException;
import com.henallux.ravelup.exeption.PinException;
import com.henallux.ravelup.exeption.TokenException;
import com.henallux.ravelup.model.CategoryModel;
import com.henallux.ravelup.model.PinModel;
import com.henallux.ravelup.model.PointOfInterestModel;
import com.henallux.ravelup.model.TokenReceivedModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MapDAO {
    private Gson gsonBuilder = new GsonBuilder()
        .serializeNulls()
        .create();
    public ArrayList<CategoryModel> getAllCategories(TokenReceivedModel token) throws TokenException, CategoryException{
        try {
            URL url = new URL("http://ravelapidb.azurewebsites.net/api/Categorie");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token.getToken());

            BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String stringJSON = "", line;
            while ((line = buffer.readLine()) != null) {
                builder.append(line);
            }
            buffer.close();
            stringJSON = builder.toString();
            return jsonToCategories(stringJSON);
        } catch (IOException e) {
            throw new CategoryException("Les catégories n'ont pas été trouvées");
        } catch (JSONException e) {
            throw new CategoryException("Les catégories n'ont pas été trouvées");
        }
    }

    private ArrayList<CategoryModel>jsonToCategories(String stringJSON) throws JSONException {
        ArrayList<CategoryModel> categories= new ArrayList<>();
        CategoryModel category;
        JSONArray jsonArray=new JSONArray(stringJSON);
        for (int i=0;i<jsonArray.length();i++){
            JSONObject jsonCategory = jsonArray.getJSONObject(i);
            category= new CategoryModel();
            category.setId(jsonCategory.getLong("id"));
            category.setLibelle(jsonCategory.getString("libelle"));
            categories.add(category);
        }
        return categories;
    }

    public ArrayList<PointOfInterestModel> getAllPins(TokenReceivedModel token, PinModel pin) throws TokenException,PinException{
        try {
            URL url = new URL("http://ravelapidb.azurewebsites.net/api/PointInteret/ByCategorie");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token.getToken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream outputStream = connection.getOutputStream();
            OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
            connection.connect();
            streamWriter.write(gsonBuilder.toJson(pin));
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
            return jsonToPointsInterets(jsonString);
        }catch (JSONException e){
            throw new PinException("Les points d'intérêts n'ont pas été trouvés");
        }catch(IOException e){
            throw new PinException("Les points d'intérêts n'ont pas été trouvés");
        }
    }

    private ArrayList<PointOfInterestModel>jsonToPointsInterets(String stringJSON) throws JSONException {
        ArrayList<PointOfInterestModel> pointOfInterest= new ArrayList<>();
        PointOfInterestModel pin;
        JSONArray jsonArray=new JSONArray(stringJSON);
        for (int i=0;i<jsonArray.length();i++){
            JSONObject jsonPin = jsonArray.getJSONObject(i);
            pin= new PointOfInterestModel();
            pin.setId(jsonPin.getLong("id"));
            pin.setNom(jsonPin.getString("nom"));
            pin.setDescription(jsonPin.getString("description"));
            pin.setLongitude(jsonPin.getDouble("longitude"));
            pin.setLatitude(jsonPin.getDouble("latitude"));
            pin.setCategorieId(jsonPin.getLong("categorieId"));
            pointOfInterest.add(pin);
        }
        return pointOfInterest;
    }
}
