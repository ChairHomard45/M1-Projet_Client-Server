package Server;

import Common.Article.IArticle;
import Common.Article.IArticleAcheteur;
import Common.Article.IArticleEmployer;
import Common.CalculCA.ICalculCA;
import Common.Facture.IFacture;
import Common.Facture.IFactureAcheteur;
import Common.Objects.ObjectArticle;
import Common.Objects.ObjectFacture;
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
import Server.UpdatePrix.UpdatePrixImpl;
import Server.Utils.PathsClass;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.naming.spi.ObjectFactory;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Dictionary;
import java.util.Timer;
import java.util.TimerTask;

import static Common.Utils.SchedulerDelay.calculateInitialDelay;

public class Serveur {
    public static void main(String[] args) {
        BD.getInstance().connexion();
        initRMI();
    }

    private static void initRMI() {
        try {
            System.out.println("Serveur initRMI");
            Registry reg = LocateRegistry.getRegistry();

            /* Init Class */
            CalculCAImpl calculCA = new CalculCAImpl();
            ArticleAcheteurImpl articleAcheteur = new ArticleAcheteurImpl();
            ArticleImpl article = new ArticleImpl();
            ArticleEmployerImpl articleEmployer = new ArticleEmployerImpl();
            FactureAcheteurImpl factureAcheteur = new FactureAcheteurImpl();
            FactureImpl facture = new FactureImpl();
            UpdatePrixImpl updatePrix = new UpdatePrixImpl();

            /* STUB */
            ICalculCA stubCalculCA = (ICalculCA) UnicastRemoteObject.exportObject(calculCA, 0);
            IArticleAcheteur stubArticleAcheteur = (IArticleAcheteur) UnicastRemoteObject.exportObject(articleAcheteur, 1);
            IArticle stubArticle = (IArticle) UnicastRemoteObject.exportObject(article, 2);
            IArticleEmployer stubArticleEmployer = (IArticleEmployer) UnicastRemoteObject.exportObject(articleEmployer, 3);
            IFactureAcheteur stubFactureAcheteur = (IFactureAcheteur) UnicastRemoteObject.exportObject(factureAcheteur, 4);
            IFacture stubFacture = (IFacture) UnicastRemoteObject.exportObject(facture, 5);
            IUpdatePrix stubUpdatePrix = (IUpdatePrix) UnicastRemoteObject.exportObject(updatePrix, 6);

            /* Rebind */
            reg.rebind("CalculCAImpl", stubCalculCA);
            reg.rebind("ArticleAcheteurImpl", stubArticleAcheteur);
            reg.rebind("ArticleImpl", stubArticle);
            reg.rebind("ArticleEmployerImpl", stubArticleEmployer);
            reg.rebind("FactureAcheteurImpl", stubFactureAcheteur);
            reg.rebind("FactureImpl", stubFacture);
            reg.rebind("UpdatePrixImpl", stubUpdatePrix);

            /* STUB - Int */
            ITransferTickets stubTransferTickets = waitForTransferTicketsStub(reg);

            scheduleDailyTransferTicket(stubTransferTickets, 22);

            System.out.println("Le Serveur est prêt...");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static ITransferTickets waitForTransferTicketsStub(Registry reg) {
        while (true) {
            try {
                return (ITransferTickets) reg.lookup("TransferTicketsImpl");
            } catch (Exception e) {
                System.out.println("Attente pour TransferTicketsImpl...");
                try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
            }
        }
    }


    private static ObjectFacture[] convertJsonToFacture(String json) {
        JSONArray jsonArray = JSONReader.getJSONArrayFromFile(json);
        ObjectFacture[] tickets = new ObjectFacture[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            tickets[i] = new ObjectFacture(jsonArray.getJSONObject(i));
        }
        return tickets;
    }

    private static void scheduleDailyTransferTicket(ITransferTickets stubTransferTickets, int targetHour) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    System.out.println("Calling transferTicketJson at: " + LocalDateTime.now());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    String date = java.time.LocalDate.now().format(formatter);
                    String jsonPath = PathsClass.getJSONFilePath(date);
                    ObjectFacture[] factures = convertJsonToFacture(jsonPath);
                    if (factures.length > 0) {
                        int result = stubTransferTickets.transferTicketJson(PathsClass.getMagasinID() + date, factures);
                        switch (result) {
                            case 0:
                                System.out.println("Transfert réussi!");
                                break;
                            case 1:
                                System.out.println("Liste Vide!");
                                break;
                            case -1:
                                System.out.println("Erreur du Transfert!");
                                break;
                        }
                    } else {
                        System.out.println("Aucune factures trouvé à transférer.");
                    }
                } catch (RemoteException ex) {
                    System.err.println("Failed to call updatePrix: " + ex.getMessage());
                }
            }
        };

        long delay = calculateInitialDelay(targetHour);
        long period = 24 * 60 * 60 * 1000L;

        timer.scheduleAtFixedRate(task, delay, period);
        System.out.println("Scheduled TransferTickets() daily at " + targetHour + ":00");
    }
}
