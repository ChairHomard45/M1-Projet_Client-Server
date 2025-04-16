package Common.Objects;

import org.json.JSONObject;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class ObjectFacture implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
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
        this.reference_commande = object.getInt("reference_commande");
        this.montant_commande = object.getFloat("montant_commande");
        this.mode_paiement = object.getString("mode_paiement");
        this.date_facture = object.getString("date_facture");
        this.listes_articles = ObjectArticle.toList(object.getJSONArray("listes_articles"));
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

    public String toString() {
        return "refF : " + reference_commande + " - " +
                "MontantF : " + montant_commande + " - " +
                "DateF : " + date_facture + " - " +
                "ModePaiementF : " + mode_paiement + " - " +
                "ListeArticleF : " + listes_articles + "\n";
    }

    public String toStringTextPane() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='text-align:left; font-size:1.5em'>")
                .append("<div>")
                .append("Référence Facture : ").append(reference_commande).append(" <br> ")
                .append("Montant Facture : ").append(montant_commande).append(" <br> ")
                .append("Date de paiement : ").append(date_facture).append(" <br> ")
                .append("Mode de Paiement : ").append(mode_paiement).append(" <br><br> ")
                .append("Liste Articles :<br>");

        for (ObjectArticle article : listes_articles) {
            sb.append(article.toStringListing()).append("<br>");
        }

        sb.append("</div></body></html>");
        return sb.toString();
    }

}
