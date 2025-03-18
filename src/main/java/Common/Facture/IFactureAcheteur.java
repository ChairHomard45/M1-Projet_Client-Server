package Common.Facture;

import Common.Objects.ModePaiement;

public interface IFactureAcheteur {
    int payerFacture(int refCommande, String ModePaiement);
}
