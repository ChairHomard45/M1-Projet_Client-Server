package Common.Article;

import Common.Objects.ObjectArticle;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IArticleAcheteur extends Remote {
    ObjectArticle acheterArticle(int refCommande, String refArticle, int qte) throws RemoteException;
    int creerCommande(int refCommande) throws RemoteException;
}
