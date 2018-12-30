package com.henallux.ravelup.dao.dataacess;

import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henallux.ravelup.model.PointOfInterestModel;
import com.henallux.ravelup.model.TokenReceived;
import com.henallux.ravelup.model.TrajetModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class RavelDAO {

    private Gson gsonBuilder = new GsonBuilder()
            .serializeNulls()
            .create();

    public PointOfInterestModel getPointInterest(TokenReceived token, Long idPoint)throws Exception{
        //Gson gsonBuilder = new GsonBuilder().serializeNulls().create();


        URL url = new URL("http://ravelapidb.azurewebsites.net/api/PointInteret/"+"?id="+idPoint);
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

//      Gson gson = new Gson();
//      return gson.fromJson(stringJSON, PointOfInterestModel.class);
        return jsonToPointInteret(stringJSON);
    }

    private PointOfInterestModel jsonToPointInteret(String stringJSON) throws Exception {
        // probleme ?
        JSONArray jsonArray=new JSONArray(stringJSON);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        PointOfInterestModel point= new PointOfInterestModel();
            point.setId(jsonObject.getLong("id"));
            point.setNom(jsonObject.getString("nom"));
            point.setDescription(jsonObject.getString("description"));
            point.setLongitude(jsonObject.getDouble("longitude"));
            point.setLatitude(jsonObject.getDouble("latitude"));
            point.setCategorieId(jsonObject.getLong("categorieId"));
        return point;
    }

    public ArrayList<TrajetModel> getTrajets(TokenReceived token, ArrayList<Long>idTrajets)throws Exception{
        URL url = new URL("http://ravelapidb.azurewebsites.net/api/Trajets/TrajetList");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-type","application/json");
        connection.setRequestProperty("Authorization","Bearer " +token.getToken());

        connection.setDoOutput(true);
        connection.setDoInput(true);

        connection.connect();

        OutputStream outputStream = connection.getOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);

        streamWriter.write(gsonBuilder.toJson(idTrajets));
        streamWriter.flush();
        streamWriter.close();

        BufferedReader buffer =new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String jsonString="",line;
        while((line=buffer.readLine())!=null){
            builder.append(line);
        }
        buffer.close();
        jsonString=builder.toString();

        outputStream.close();
        connection.disconnect();

        return jsonToTrajet(jsonString);
    }

    private ArrayList<TrajetModel>jsonToTrajet(String stringJSON) throws Exception {
        ArrayList<TrajetModel> trajets= new ArrayList<>();
        TrajetModel trajet;
        JSONArray jsonArray=new JSONArray(stringJSON);
        for (int i=0;i<jsonArray.length();i++){
            JSONObject jsonTrajet = jsonArray.getJSONObject(i);
            trajet= new TrajetModel();
            trajet.setId(jsonTrajet.getLong("id"));
            trajet.setDescription(jsonTrajet.getString("description"));
            trajet.setNbKm(jsonTrajet.getDouble("nbKm"));
            trajet.setTypeDeplacement(jsonTrajet.getLong("typeDeplacement"));
            trajets.add(trajet);
        }
        return trajets;
    }

    public ArrayList<PointOfInterestModel> getPointsInterests(TokenReceived token, ArrayList<Long>idPoints)throws Exception{
        URL url = new URL("http://ravelapidb.azurewebsites.net/api/PointInteret/pointInteretList");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-type","application/json");
        connection.setRequestProperty("Authorization","Bearer " +token.getToken());

        connection.setDoOutput(true);
        connection.setDoInput(true);

        connection.connect();

        OutputStream outputStream = connection.getOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);

        streamWriter.write(gsonBuilder.toJson(idPoints));
        streamWriter.flush();
        streamWriter.close();

        BufferedReader buffer =new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String jsonString="",line;
        while((line=buffer.readLine())!=null){
            builder.append(line);
        }
        buffer.close();
        jsonString=builder.toString();

        outputStream.close();
        connection.disconnect();

        return jsonToPointsInterets(jsonString);
    }

    private ArrayList<PointOfInterestModel>jsonToPointsInterets(String stringJSON) throws Exception {
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
