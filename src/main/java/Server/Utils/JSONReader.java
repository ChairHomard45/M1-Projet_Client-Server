package Server.Utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;

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
}
