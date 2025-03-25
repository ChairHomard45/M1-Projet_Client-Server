package Common.Facture;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IFactureAcheteur extends Remote {
    int payerFacture(int refCommande, String ModePaiement) throws RemoteException;
}
