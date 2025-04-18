package Server.CalculCA;

import Common.CalculCA.ICalculCA;
import Server.Utils.DateChecker;
import Server.Utils.JSONReader;
import Server.Utils.PathsClass;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculCAImpl implements ICalculCA {
    /**
     * @param date date a chercher
     * @return le chiffre d'affaires ou Zero si le format n'est pas correct
     */
    @Override
    public float getCA(String date) {
        if (!DateChecker.isDate(date)) {
            return -1;
        }

        String filename = PathsClass.getFacturePath() + PathsClass.getMagasinID() + date + ".json";

        JSONArray jsonArray = JSONReader.getJSONArrayFromFile(filename);

        jsonArray.toList();
        BigDecimal ca = BigDecimal.ZERO;
        for (JSONObject obj : JSONReader.getListFromArray(jsonArray)) {
            System.out.println(obj.getFloat("montant_commande"));
            ca = ca.add(obj.getBigDecimal("montant_commande"));
        }

        return ca.setScale(2, RoundingMode.HALF_UP).floatValue();
    }
}
