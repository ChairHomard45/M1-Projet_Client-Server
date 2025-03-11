package Server.CalculCA;

import Common.CalculCA.ICalculCA;
import Server.Utils.JSONReader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculCA implements ICalculCA {
    /**
     * @param date date a chercher
     * @return le chiffre d'affaires
     */
    public float getCA(String date) {
        String filename = "./src/main/java/Server/FacturesJson/AB-" + date + ".json";
        JSONArray jsonArray = JSONReader.getJSONArrayFromFile(filename);
        BigDecimal ca = BigDecimal.ZERO;
        for (JSONObject obj : JSONReader.getListFromArray(jsonArray)) {
            System.out.println(obj.getFloat("montantCommande"));
            ca = ca.add(obj.getBigDecimal("montantCommande"));
        }

        return ca.setScale(2, RoundingMode.HALF_UP).floatValue();
    }
}
