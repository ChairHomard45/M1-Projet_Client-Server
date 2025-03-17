package Server.Utils;

public final class PathsClass {

    /**
     * @return l'ID des magasins
     * */
    public static String getMagasinID(){
        return "AB-";
    }

    /**
     * @return le path vers le param Json
     * */
    public static String getBDParamPath(){
        return "./src/main/java/Server/Database/paramBd.json";
    }

    /**
    * @return le path des Json Factures
    * */
    public static String getFacturePath(){
        return "./src/main/java/Server/FacturesJson/";
    }
}
