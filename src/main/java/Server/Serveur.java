package Server;

import Common.CalculCA.ICalculCA;
import Server.CalculCA.CalculCA;
import Server.Database.BD;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Serveur extends CalculCA {
    public static void main(String[] args) {
        BD.getInstance().connexion();
        initRMI();
    }

    private static void initRMI() {
        try {
            CalculCA calculCA = new CalculCA();
            ICalculCA stub = (ICalculCA) UnicastRemoteObject.exportObject(calculCA, 0);
            Registry reg = LocateRegistry.getRegistry();
            reg.rebind("CalculCA", stub);
            System.out.println("Le Serveur est prêt...");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}