package Common.Facture;

import Common.Objects.ObjectFacture;

public interface IFacture {
    ObjectFacture consulterFacture(int refCommanden, String date);
}
