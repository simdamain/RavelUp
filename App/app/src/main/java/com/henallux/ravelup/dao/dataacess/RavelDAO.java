package com.henallux.ravelup.dao.dataacess;

import com.google.android.gms.maps.model.PointOfInterest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henallux.ravelup.exeptions.ImageException;
import com.henallux.ravelup.exeptions.PinException;
import com.henallux.ravelup.exeptions.TokenException;
import com.henallux.ravelup.exeptions.TrajetException;
import com.henallux.ravelup.models.ImageModel;
import com.henallux.ravelup.models.PointOfInterestModel;
import com.henallux.ravelup.models.TokenReceivedModel;
import com.henallux.ravelup.models.TrajetModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;


public class RavelDAO {

    private Gson gsonBuilder = new GsonBuilder()
            .serializeNulls()
            .create();

    public PointOfInterestModel getPointInterest(TokenReceivedModel token, Long idPoint)throws PinException,TokenException {
        try {
            URL url = new URL("http://ravelapidb.azurewebsites.net/api/PointInteret/" + "?id=" + idPoint);
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
            return jsonToPointInteret(stringJSON);
        } catch (IOException e) {
            throw new PinException("Le point d'intérêt n'a pas été trouvé");
        } catch (JSONException e) {
            throw new PinException("Le point d'intérêt n'a pas été trouvé");
        }
    }

    private PointOfInterestModel jsonToPointInteret(String stringJSON) throws JSONException {

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

    public ArrayList<TrajetModel> getTrajets(TokenReceivedModel token, ArrayList<Long>idTrajets)throws TrajetException,TokenException{
        try {
            URL url = new URL("http://ravelapidb.azurewebsites.net/api/Trajets/TrajetList");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token.getToken());

            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.connect();

            OutputStream outputStream = connection.getOutputStream();
            OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);

            streamWriter.write(gsonBuilder.toJson(idTrajets));
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

            return jsonToTrajet(jsonString);
        } catch (IOException e) {
            throw new TrajetException("Le trajet n'a pas été trouvé");
        } catch (JSONException e) {
            throw new TrajetException("Le trajet n'a pas été trouvé");
        }
    }

    private ArrayList<TrajetModel>jsonToTrajet(String stringJSON) throws JSONException {
        ArrayList<TrajetModel> trajets= new ArrayList<>();
        TrajetModel trajet;
        JSONArray jsonArray=new JSONArray(stringJSON);
        for (int i=0;i<jsonArray.length();i++){
            JSONObject jsonTrajet = jsonArray.getJSONObject(i);
            trajet= new TrajetModel();
            trajet.setId(jsonTrajet.getLong("id"));
            trajet.setDescription(jsonTrajet.getString("description"));
            trajet.setNbKm(jsonTrajet.getDouble("nbKm"));
            trajet.setTypeDeplacement(jsonTrajet.getInt("typeDeplacement"));
            trajets.add(trajet);
        }
        return trajets;
    }

    public ArrayList<PointOfInterestModel> getPointsInterests(TokenReceivedModel token, ArrayList<Long>idPoints)throws TokenException,PinException {
        try {
            URL url = new URL("http://ravelapidb.azurewebsites.net/api/PointInteret/pointInteretList");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token.getToken());

            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.connect();

            OutputStream outputStream = connection.getOutputStream();
            OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);

            streamWriter.write(gsonBuilder.toJson(idPoints));
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
        } catch (JSONException e) {
            throw new PinException("Les points d'intérêts n'ont pas été trouvés");
        } catch (IOException e) {
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

    public ArrayList<String> getImages(TokenReceivedModel token, Long idPointInteret) throws TokenException,ImageException {
        try {
            URL url = new URL("http://ravelapidb.azurewebsites.net/api/Images/"+idPointInteret);
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
            return jsonToImages(stringJSON);
        } catch (JSONException e) {
            throw new ImageException("Les images n'ont pas été trouvées (Url)");
        } catch (IOException e) {
            throw new ImageException("Les images n'ont pas été trouvées (Url)");
        }
    }

        private ArrayList<String>jsonToImages(String stringJSON) throws JSONException {
            ArrayList<String> urlImages= new ArrayList<>();
            JSONArray jsonArray=new JSONArray(stringJSON);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonImage = jsonArray.getJSONObject(i);
                urlImages.add(jsonImage.getString("url"));
            }
            return urlImages;

    }

}
