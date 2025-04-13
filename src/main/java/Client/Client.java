package Client;

import Client.GUI.Window;

import javax.swing.*;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        initGUI();
        initRMI();
    }

    private static void initRMI() {
        try {
            Rmi.GetInstance();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void initGUI() {
        Window window = new Window();
        window.setVisible(true);
        window.setTitle("Client");
        window.setSize(900, 600);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
