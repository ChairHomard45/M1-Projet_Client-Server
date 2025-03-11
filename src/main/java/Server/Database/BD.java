package Server.Database;

import Server.Utils.JSONReader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BD {
    private static Connection con;
    private static BD instance;
    private JSONObject params;

    private BD() {
    }

    public static synchronized BD getInstance() {
        if (instance == null) instance = new BD();
        return instance;
    }

    public Connection getConnection() {
        if (con == null) connexion();
        return con;
    }

    public void connexion() {
        try {
        params = JSONReader.getJSONFromFile("./src/main/java/Server/Database/paramBd.json");
        con = DriverManager.getConnection(params.getString("url"), params.getString("user"), params.getString("passw"));
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void deconnexion() {
        if (con != null) {
            try {
                con.close();
                con = null;
                params = null;
                System.out.println("Database disconnected.");
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}