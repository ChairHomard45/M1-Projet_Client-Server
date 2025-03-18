package Server;

import Common.CalculCA.ICalculCA;
import Server.CalculCA.CalculCAImpl;
import Server.Database.BD;
import Server.Facture.FactureAcheteurImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Serveur extends CalculCAImpl {
    public static void main(String[] args) {
        BD.getInstance().connexion();
        FactureAcheteurImpl factureAcheteurImpl = new FactureAcheteurImpl();
        factureAcheteurImpl.payerFacture(2,"");
        //initRMI();
    }

    private static void initRMI() {
        try {
            CalculCAImpl calculCA = new CalculCAImpl();
            ICalculCA stub = (ICalculCA) UnicastRemoteObject.exportObject(calculCA, 0);
            Registry reg = LocateRegistry.getRegistry();
            reg.rebind("CalculCAImpl", stub);
            System.out.println("Le Serveur est prêt...");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}