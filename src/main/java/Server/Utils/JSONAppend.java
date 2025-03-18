package Server.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class JSONAppend {

    /**
     * Insertion d'un nouvel Objet dans le fichier.
     * @param obj Object Json à insérer
     * @param Filename Path du fichier
     * @return True si terminer ou False si error
     * */
    public static boolean insertJSONObject(JSONObject obj, String Filename){
        try {
            File file = new File(Filename);
            FileWriter fileWriter;

            JSONArray jsonArray = new JSONArray();
            if (!file.createNewFile()) {
                jsonArray = JSONReader.getJSONArrayFromFile(Filename);
            }
            jsonArray.put(obj);

            fileWriter = new FileWriter(file,false);
            fileWriter.write(jsonArray.toString());
            fileWriter.close();
            return true;
        } catch (IOException err){
            System.err.println("An error occurred : " + err.getMessage() );
            return false;
        }
    }

    /**
     * Insertion d'un nouveau Array dans le fichier.
     * @param jsonArray Array Json à insérer
     * @param Filename Path du fichier
     * @return True si terminer ou False si error
     * */
    public static boolean insertJSONArray(JSONArray jsonArray, String Filename){
        try {
            File file = new File(Filename);
            FileWriter fileWriter;

            JSONArray fileArray = new JSONArray();
            if (!file.createNewFile()) {
                fileArray = JSONReader.getJSONArrayFromFile(Filename);
            }
            fileArray.putAll(jsonArray);

            fileWriter = new FileWriter(file,false);
            fileWriter.write(fileArray.toString());
            fileWriter.close();
            return true;
        } catch (IOException err){
            System.err.println("An error occurred : " + err.getMessage() );
            return false;
        }
    }

}
