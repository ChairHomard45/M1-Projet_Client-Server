package Server;

import Server.Database.BD;

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
            // Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 0);
            Registry reg = LocateRegistry.getRegistry();
            System.out.println("Le Serveur est prêt...");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}