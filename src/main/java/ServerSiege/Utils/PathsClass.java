package ServerSiege.Utils;

public final class PathsClass {
    /**
     * @return le path des Json Tickets
     * */
    public static String getJSONFilePath(String filename){
        return "./src/main/java/ServerSiege/JsonTickets/" + filename.substring(0,2) + "/" + filename + ".json";
    }

    /**
     * @return le path des Json Prix
     * */
    public static String getFilePathUpdatedPrix(String filename){
        return "./src/main/java/ServerSiege/JsonPrix/" + filename + ".json";
    }
}
