package Server.Facture;

import Common.Facture.IFacture;
import Common.Objects.ObjectFacture;
import Server.Utils.DateChecker;
import Common.Utils.JSONReader;
import Server.Utils.PathsClass;
import org.json.JSONArray;
import org.json.JSONObject;

public class FactureImpl implements IFacture {

    /**
     * @param refCommande Numéro de commande/facture
     * @return ObjectFacture contenant toutes les données.
     */
    @Override
    public ObjectFacture consulterFacture(int refCommande, String date) {
        if (!DateChecker.isDate(date)) {
            return null;
        }
        String filename = PathsClass.getFacturePath() + PathsClass.getMagasinID() + date + ".json";

        JSONArray jsonArray = JSONReader.getJSONArrayFromFile(filename);

        for (JSONObject obj : JSONReader.getListFromArray(jsonArray)) {
            if (obj.getInt("reference_commande") == refCommande) {
                return new ObjectFacture(obj);
            }
        }
        return null;
    }
}
