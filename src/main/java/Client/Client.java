package Client;

import Common.CalculCA.ICalculCA;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public static void main(String[] args) {
        initRMI();
    }

    private static void initRMI() {
        try {
            // Récupérer le registre
            Registry reg = LocateRegistry.getRegistry(null);

            // Recherche dans le registre de l'objet distant
            ICalculCA stub = (ICalculCA) reg.lookup("CalculCA");
            System.out.println("CA Test" + stub.getCA("12"));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
