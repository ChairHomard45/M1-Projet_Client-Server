package Server.Utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JSONReader {
    /**
     * @param fileName Nom du fichier à partir duquel on veut récupérer les données
     *                 à partir de la racine
     * @return Objet JSONObject
     * */
    public static JSONObject getJSONFromFile(String fileName) {
        try {
            JSONTokener tokener = new JSONTokener(new FileInputStream(fileName));
            return new JSONObject(tokener);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return new JSONObject();
    }

    /**
     * @param fileName Nom du fichier à partir duquel on veut récupérer les données
     *                 à partir de la racine
     * @return Objet JSONArray
     * */
    public static JSONArray getJSONArrayFromFile(String fileName) {
        try {
            JSONTokener tokener = new JSONTokener(new FileInputStream(fileName));
            return new JSONArray(tokener);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return new JSONArray();
    }

    /**
     * Sert à convertir un JSONArray en une list de JSONObject
     * @param array Object JSONArray à convertir
     * @return List JSONObject
     * */
    public static List<JSONObject> getListFromArray(JSONArray array) {
        List<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getJSONObject(i));
        }
        return list;
    }
}
