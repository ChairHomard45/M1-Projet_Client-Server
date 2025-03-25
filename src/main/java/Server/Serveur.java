package Server;

import Common.Article.IArticle;
import Common.Article.IArticleAcheteur;
import Common.Article.IArticleEmployer;
import Common.CalculCA.ICalculCA;
import Common.Facture.IFacture;
import Common.Facture.IFactureAcheteur;
import Common.Objects.ObjectArticle;
import Server.Article.ArticleAcheteurImpl;
import Server.Article.ArticleEmployerImpl;
import Server.Article.ArticleImpl;
import Server.CalculCA.CalculCAImpl;
import Server.Database.BD;
import Server.Facture.FactureAcheteurImpl;
import Server.Facture.FactureImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Serveur {
    public static void main(String[] args) {
        BD.getInstance().connexion();
        initRMI();
    }

    private static void initRMI() {
        try {
            System.out.println("Serveur initRMI");

            /* Init Class */
            CalculCAImpl calculCA = new CalculCAImpl();
            ArticleAcheteurImpl articleAcheteur = new ArticleAcheteurImpl();
            ArticleImpl article = new ArticleImpl();
            ArticleEmployerImpl articleEmployer = new ArticleEmployerImpl();
            FactureAcheteurImpl factureAcheteur = new FactureAcheteurImpl();
            FactureImpl facture = new FactureImpl();

            /* STUB */
            ICalculCA stubCalculCA = (ICalculCA) UnicastRemoteObject.exportObject(calculCA, 0);
            IArticleAcheteur stubArticleAcheteur = (IArticleAcheteur) UnicastRemoteObject.exportObject(articleAcheteur, 1);
            IArticle stubArticle = (IArticle) UnicastRemoteObject.exportObject(article, 2);
            IArticleEmployer stubArticleEmployer = (IArticleEmployer) UnicastRemoteObject.exportObject(articleEmployer, 3);
            IFactureAcheteur stubFactureAcheteur = (IFactureAcheteur) UnicastRemoteObject.exportObject(factureAcheteur, 4);
            IFacture stubFacture = (IFacture) UnicastRemoteObject.exportObject(facture, 5);

            Registry reg = LocateRegistry.getRegistry();

            /* Rebind */
            reg.rebind("CalculCAImpl", stubCalculCA);
            reg.rebind("ArticleAcheteurImpl", stubArticleAcheteur);
            reg.rebind("ArticleImpl", stubArticle);
            reg.rebind("ArticleEmployerImpl", stubArticleEmployer);
            reg.rebind("FactureAcheteurImpl", stubFactureAcheteur);
            reg.rebind("FactureImpl", stubFacture);

            System.out.println("Le Serveur est prêt...");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}