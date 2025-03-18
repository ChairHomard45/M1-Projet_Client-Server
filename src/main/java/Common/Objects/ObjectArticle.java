package Common.Objects;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class ObjectArticle {
    private String referenceArticle;
    private String nomArticle;
    private String familleArticle;
    private float prixUnitaire;
    private int Qte;

    public ObjectArticle(String refArticle, String nomArticle, String familleArticle, float prixUnitaire, int Qte) {
        this.referenceArticle = refArticle;
        this.nomArticle = nomArticle;
        this.familleArticle = familleArticle;
        this.prixUnitaire = prixUnitaire;
        this.Qte = Qte;
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
        articles.forEach(article -> {
            array.put(article.toJSON());
        });
        return array;
    }
}
