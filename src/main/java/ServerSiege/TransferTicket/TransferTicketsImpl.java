package ServerSiege.TransferTicket;

import Common.Objects.ObjectFacture;
import Common.Siege.ITransferTickets;
import Common.Utils.JSONAppend;
import ServerSiege.Utils.PathsClass;
import org.json.JSONArray;

import java.rmi.RemoteException;

public class TransferTicketsImpl implements ITransferTickets {
    /**
     * @param tickets Tous les tickets de la journée
     * @return Status
     */
    @Override
    public synchronized int transferTicketJson(String filename, ObjectFacture[] tickets) {
        if (tickets.length == 0) {
            return 1;
        }
        JSONArray jsonArray = new JSONArray();
        for (ObjectFacture ticket : tickets) {
            jsonArray.put(ticket.toJSON());
        }

        if (JSONAppend.insertJSONArray(jsonArray, PathsClass.getJSONFilePath(filename)))
            return 0;
        return -1;
    }
}
