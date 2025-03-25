package Client;

import Client.GUI.Window;
import Common.Article.IArticle;
import Common.Article.IArticleAcheteur;
import Common.Article.IArticleEmployer;
import Common.CalculCA.ICalculCA;
import Common.Facture.IFacture;
import Common.Facture.IFactureAcheteur;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        Window window = new Window();
        initRMI();
    }

    private static void initRMI() {
        try {
            // Récupérer le registre
            Registry reg = LocateRegistry.getRegistry(null);

            // Recherche dans le registre de l'objet distant
            ICalculCA stubCalculCA = (ICalculCA) reg.lookup("CalculCAImpl");
            IArticleAcheteur stubArticleAcheteur = (IArticleAcheteur) reg.lookup("ArticleAcheteurImpl");
            IArticle stubArticle = (IArticle) reg.lookup("ArticleImpl");
            IArticleEmployer stubArticleEmployer = (IArticleEmployer) reg.lookup("ArticleEmployerImpl");
            IFactureAcheteur stubFactureAcheteur = (IFactureAcheteur) reg.lookup("FactureAcheteurImpl");
            IFacture stubFacture = (IFacture) reg.lookup("FactureImpl");


            System.out.println(stubCalculCA.getCA("18-03-2025"));
            System.out.println(stubArticleAcheteur.acheterArticle(1, "FFFEEE", 1));
            System.out.println(stubArticleAcheteur.creerCommande(3));
            System.out.println(stubArticle.getInfoArticle("FFFEEE"));
            System.out.println(stubArticle.getRefsArticles("FF"));
            System.out.println(stubArticleEmployer.ajouterArticle("FFFEEE", 10));
            System.out.println(stubFactureAcheteur.payerFacture(1, "ESPECE"));
            System.out.println(stubFacture.consulterFacture(1, "25-03-2025").toString());

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
