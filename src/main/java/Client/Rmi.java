package Client;

import Common.Article.IArticle;
import Common.Article.IArticleAcheteur;
import Common.Article.IArticleEmployer;
import Common.CalculCA.ICalculCA;
import Common.Facture.IFacture;
import Common.Facture.IFactureAcheteur;

import javax.swing.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Rmi {
    private static Rmi _instance = null;
    private final ICalculCA stubCalculCA;
    private final IArticleAcheteur stubArticleAcheteur;
    private final IArticle stubArticle;
    private final IArticleEmployer stubArticleEmployer;
    private final IFactureAcheteur stubFactureAcheteur;
    private final IFacture stubFacture;

    public synchronized static Rmi GetInstance() {
        if (_instance == null) _instance = new Rmi();
        return _instance;
    }

    private Rmi() {
        // Récupérer le registre
        try {
            Registry reg = LocateRegistry.getRegistry("localhost", 2500);

            // Recherche dans le registre de l'objet distant
            stubCalculCA = (ICalculCA) reg.lookup("CalculCAImpl");
            stubArticleAcheteur = (IArticleAcheteur) reg.lookup("ArticleAcheteurImpl");
            stubArticle = (IArticle) reg.lookup("ArticleImpl");
            stubArticleEmployer = (IArticleEmployer) reg.lookup("ArticleEmployerImpl");
            stubFactureAcheteur = (IFactureAcheteur) reg.lookup("FactureAcheteurImpl");
            stubFacture = (IFacture) reg.lookup("FactureImpl");

        } catch (RemoteException | NotBoundException e) {
            JOptionPane.showMessageDialog(null, "Aucune Connexion RMI possible", "Info", JOptionPane.ERROR_MESSAGE);
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
