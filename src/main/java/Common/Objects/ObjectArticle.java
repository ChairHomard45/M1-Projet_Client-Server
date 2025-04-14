package Common.Objects;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ObjectArticle implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private final String referenceArticle;
    private final String nomArticle;
    private final String familleArticle;
    private final float prixUnitaire;
    private final int Qte;

    public ObjectArticle(String refArticle, String nomArticle, String familleArticle, float prixUnitaire, int Qte) {
        this.referenceArticle = refArticle;
        this.nomArticle = nomArticle;
        this.familleArticle = familleArticle;
        this.prixUnitaire = prixUnitaire;
        this.Qte = Qte;
    }

    public ObjectArticle(JSONObject object){
        this.referenceArticle = object.getString("reference_article");
        this.nomArticle = object.getString("nom_article");
        this.familleArticle = object.getString("famille_article");
        this.prixUnitaire = object.getFloat("unite_prix");
        this.Qte = object.getInt("quantite_stock");
    }

    public String getReferenceArticle() {
        return referenceArticle;
    }

    public int getQte(){
        return Qte;
    }

    public static List<ObjectArticle> toList(JSONArray jsonArray) {
        List<ObjectArticle> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(new ObjectArticle(jsonArray.getJSONObject(i)));
        }
        return list;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("reference_article", referenceArticle);
        obj.put("nom_article", nomArticle);
        obj.put("famille_article", familleArticle);
        obj.put("unite_prix", prixUnitaire);
        obj.put("quantite_stock", Qte);
        return obj;
    }

    public static JSONArray toJSONArray(List<ObjectArticle> articles) {
        JSONArray array = new JSONArray();
        articles.forEach(article ->
            array.put(article.toJSON())
        );
        return array;
    }

    public String toStringList()
    {
        return "refA : " + referenceArticle + " - " +
                "nomA : " + nomArticle + " - " +
                "famA : " + familleArticle + " - " +
                "prix : " + prixUnitaire + " - " +
                "qteA : " + Qte + "\n";
    }

    public String toStringTextPane()
    {
        return "<html><body style='text-align:center; font-size:2em'>" +
                "<div style='border:2px solid black; padding:20px;'>" +
                "Référence Article : " + referenceArticle + "<br>" +
                "Nom Article : " + nomArticle + "<br>" +
                "Famille Article : " + familleArticle + "<br>" +
                "Prix : " + prixUnitaire + "<br>" +
                "Quantité en stock : " + Qte + "<br>" +
                "<div></body></html>";
    }

    public String toString()
    {
        return nomArticle + " - Qte : " + Qte + " - PrixU : " + prixUnitaire + " - PrixT : " + Qte * prixUnitaire + "\n";
    }
}
