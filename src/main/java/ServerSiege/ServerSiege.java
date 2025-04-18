package ServerSiege;

import Common.Siege.IUpdatePrix;
import Common.Utils.JSONReader;
import ServerSiege.Utils.PathsClass;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.*;

import static Common.Utils.SchedulerDelay.calculateInitialDelay;

public class ServerSiege {

    public static void main(String[] args) {
        new Rmi();
    }
}
