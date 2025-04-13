package Common.Article;

import Common.Objects.ObjectArticle;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IArticleAcheteur extends Remote {
    ObjectArticle acheterArticle(int refCommande, String refArticle, int qte) throws RemoteException;
    int creerCommande(int refCommande) throws RemoteException;
    List<ObjectArticle> consulterCommande(int refCommande) throws RemoteException;
}
