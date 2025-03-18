package Server.Facture;

import Common.Facture.IFacture;
import Common.Objects.ObjectFacture;
import Server.Utils.JSONReader;
import org.json.JSONArray;
import org.json.JSONObject;

public class FactureImpl implements IFacture {

    /**
     * @param refCommande Numéro de commande/facture
     * @return ObjectFacture contenant toutes les données.
     */
    @Override
    public ObjectFacture consulterFacture(int refCommande, String date) {
        JSONArray jsonArray = JSONReader.getJSONArrayFromFile(date);

        for (JSONObject obj : JSONReader.getListFromArray(jsonArray)) {
            if (obj.getInt("referenceCommande") == refCommande) {
                return new ObjectFacture(obj);
            }
        }
        return null;
    }
}
