package com.henallux.ravelup.dao.dataacess;

import com.henallux.ravelup.model.CategoryModel;
import com.henallux.ravelup.model.TokenReceived;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MapDAO {
    public ArrayList<CategoryModel> getAllCategories(TokenReceived token)throws Exception{

        URL url = new URL("http://ravelapidb.azurewebsites.net/api/Categorie");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-type","application/json");
        connection.setRequestProperty("Authorization","Bearer" +token.getToken());
        BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String stringJSON = "", line;
        while ((line = buffer.readLine()) != null) {
            builder.append(line);
        }
        buffer.close();
        stringJSON = builder.toString();
        return jsonToPersons(stringJSON);
    }

    private ArrayList<CategoryModel>jsonToPersons(String stringJSON) throws Exception
    {
        ArrayList<CategoryModel> categories= new ArrayList<>();
        CategoryModel category;
        JSONArray jsonArray=new JSONArray(stringJSON);
        for (int i=0;i<jsonArray.length();i++){
            JSONObject jsonPerson = jsonArray.getJSONObject(i);
            category= new CategoryModel(jsonPerson.getString("Libelle"));
            categories.add(category);
        }
        return categories;
    }
}
