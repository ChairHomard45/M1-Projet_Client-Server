package Server;

import Server.Database.BD;

public class Serveur {
    public static void main(String[] args) {
        BD.getInstance().connexion();
        new Rmi();
    }
}
