package Server.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public final class DateChecker {
    /**
     *  Format: DD-MM-YYYY
     * @return True si valide et False si non valide
     * */
    public static boolean isDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            return false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);

        try {
            dateFormat.format(dateFormat.parse(date));
            return true;
        } catch (ParseException e) {
            System.out.println(date+" is Invalid Date format");
            return false;
        }
    }

}
