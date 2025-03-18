package Common.Objects;

import org.json.JSONObject;

import java.util.List;

public class ObjectFacture {
    private final int reference_commande;
    private final float montant_commande;
    private final String date_facture;
    private final String mode_paiement;
    private final List<ObjectArticle> listes_articles;

    public ObjectFacture(int refCom, float montCom, String dateFacture, String modePaiement, List<ObjectArticle> listesArticles) {
        this.reference_commande = refCom;
        this.montant_commande = montCom;
        this.mode_paiement = modePaiement;
        this.date_facture = dateFacture;
        this.listes_articles = listesArticles;
    }

    public ObjectFacture(JSONObject object) {
        this.reference_commande = object.getInt("referenceCommande");
        this.montant_commande = object.getFloat("montantCommande");
        this.mode_paiement = object.getString("modePaiement");
        this.date_facture = object.getString("dateFacture");
        this.listes_articles = ObjectArticle.toList(object.getJSONArray("listesArticles"));
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("listes_articles", ObjectArticle.toJSONArray(listes_articles));
        obj.put("mode_paiement", mode_paiement);
        obj.put("date_facture", date_facture);
        obj.put("montant_commande", montant_commande);
        obj.put("reference_commande", reference_commande);
        return obj;
    }
}
