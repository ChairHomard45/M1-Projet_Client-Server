package ServerSiege;

import Common.Article.IArticle;
import Common.Article.IArticleAcheteur;
import Common.Article.IArticleEmployer;
import Common.CalculCA.ICalculCA;
import Common.Facture.IFacture;
import Common.Facture.IFactureAcheteur;
import Common.Siege.ITransferTickets;
import Common.Siege.IUpdatePrix;
import Common.Utils.JSONReader;
import Server.Article.ArticleAcheteurImpl;
import Server.Article.ArticleEmployerImpl;
import Server.Article.ArticleImpl;
import Server.CalculCA.CalculCAImpl;
import Server.Database.BD;
import Server.Facture.FactureAcheteurImpl;
import Server.Facture.FactureImpl;
import ServerSiege.TransferTicket.TransferTicketsImpl;
import ServerSiege.Utils.PathsClass;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static Common.Utils.SchedulerDelay.calculateInitialDelay;

public class ServerSiege {

    public static void main(String[] args) {
        initRMI();
    }

    private static void initRMI() {
        try {
            System.out.println("Serveur Siège initRMI");

            Registry reg = LocateRegistry.getRegistry();

            /* Init Class - Impl */
            TransferTicketsImpl transferTickets = new TransferTicketsImpl();

            /* STUB - Impl */
            ITransferTickets stubTransferTickets = (ITransferTickets) UnicastRemoteObject.exportObject(transferTickets, 7);

            /* Rebind */
            reg.rebind("TransferTicketsImpl", stubTransferTickets);

            /* STUB - Int */
            IUpdatePrix stubUpdatePrix = waitForUpdatePrixStub(reg);

            scheduleDailyUpdatePrix(stubUpdatePrix, 22);

            System.out.println("Le Serveur du Siège est prêt...");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static IUpdatePrix waitForUpdatePrixStub(Registry reg) {
        while (true) {
            try {
                return (IUpdatePrix) reg.lookup("UpdatePrixImpl");
            } catch (Exception e) {
                System.out.println("Attente pour UpdatePrixImpl...");
                try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
            }
        }
    }

    private static HashMap<String,Float> RetrieveNewPrix(){
        HashMap<String,Float> newPrix = new HashMap<>();
       JSONArray jsonArrayFromFile = JSONReader.getJSONArrayFromFile(PathsClass.getFilePathUpdatedPrix("25"));
       for(JSONObject jsonObject: JSONReader.getListFromArray(jsonArrayFromFile)){
           newPrix.put(jsonObject.getString("reference_article"), convertJsonToFloat(jsonObject, "nouveau_prix"));
       }
       return newPrix;
    }

    private static Float convertJsonToFloat(JSONObject jsonObject, String key){
        BigDecimal ca = BigDecimal.ZERO;
        ca = ca.add(jsonObject.getBigDecimal(key));
        return ca.setScale(2, RoundingMode.HALF_UP).floatValue();
    }

    private static void scheduleDailyUpdatePrix(IUpdatePrix stubUpdatePrix, int targetHour) {
        HashMap<String, Float> dictionary = RetrieveNewPrix();

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    System.out.println("Calling updatePrix at: " + LocalDateTime.now());
                    stubUpdatePrix.updatePrix(dictionary);
                } catch (RemoteException ex) {
                    System.err.println("Failed to call updatePrix: " + ex.getMessage());
                }
            }
        };

        long delay = calculateInitialDelay(targetHour);
        long period = 24 * 60 * 60 * 1000L;

        timer.scheduleAtFixedRate(task, delay, period);
        System.out.println("Scheduled updatePrix() daily at " + targetHour + ":00");
    }
}
