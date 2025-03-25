package Common.Objects;

import java.io.Serial;
import java.io.Serializable;

public final class ModePaiement implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    public static final String ESPECE = "ESPECE";
    public static final String CARTE_BANCAIRE = "CARTE_BANCAIRE";
    public static final String CHEQUE = "CHEQUE";

    private ModePaiement(){}
}
