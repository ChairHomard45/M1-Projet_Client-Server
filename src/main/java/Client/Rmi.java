package Client;

import Common.Article.IArticle;
import Common.Article.IArticleAcheteur;
import Common.Article.IArticleEmployer;
import Common.CalculCA.ICalculCA;
import Common.Facture.IFacture;
import Common.Facture.IFactureAcheteur;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Rmi {
    private static Rmi _instance = null;
    private ICalculCA stubCalculCA;
    private IArticleAcheteur stubArticleAcheteur;
    private IArticle stubArticle;
    private IArticleEmployer stubArticleEmployer;
    private IFactureAcheteur stubFactureAcheteur;
    private IFacture stubFacture;

    public synchronized static Rmi GetInstance() {
        if (_instance == null) _instance = new Rmi();
        return _instance;
    }

    private Rmi() {
        // Récupérer le registre
        Registry reg = null;
        try {
            reg = LocateRegistry.getRegistry(null);

        // Recherche dans le registre de l'objet distant
        stubCalculCA = (ICalculCA) reg.lookup("CalculCAImpl");
        stubArticleAcheteur = (IArticleAcheteur) reg.lookup("ArticleAcheteurImpl");
        stubArticle = (IArticle) reg.lookup("ArticleImpl");
        stubArticleEmployer = (IArticleEmployer) reg.lookup("ArticleEmployerImpl");
        stubFactureAcheteur = (IFactureAcheteur) reg.lookup("FactureAcheteurImpl");
        stubFacture = (IFacture) reg.lookup("FactureImpl");

        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ICalculCA getStubCalculCA() {
        return stubCalculCA;
    }

    public IArticleAcheteur getStubArticleAcheteur() {
        return stubArticleAcheteur;
    }

    public IArticle getStubArticle() {
        return stubArticle;
    }

    public IArticleEmployer getStubArticleEmployer() {
        return stubArticleEmployer;
    }

    public IFactureAcheteur getStubFactureAcheteur() {
        return stubFactureAcheteur;
    }

    public IFacture getStubFacture() {
        return stubFacture;
    }
}
