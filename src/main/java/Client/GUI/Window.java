package Client.GUI;

import Client.Rmi;
import Common.Objects.ModePaiement;
import Common.Objects.ObjectArticle;
import Common.Objects.ObjectFacture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;

public class Window extends JFrame {
    private JPanel contentPane;
    private JTabbedPane MainTabbes;
    private JPanel AcheteurPanel;
    private JPanel EmployerPanel;
    private JTabbedPane AcheteurTabbes;
    private JPanel CommandePanel;
    private JPanel RecherchePanel;
    private JTextField ReferenceCommande;
    private JButton ReferenceCommandeButton;
    private JLabel ReferenceCommandeLabel;
    private JPanel RefCommandePanel;
    private JPanel ContenuCommandePanel;
    private JList<String> ArticleJList;
    private JList<ObjectArticle> ArticlesDeLaCommande;
    private JButton AcheterButton;
    private JButton buttonPayer;
    private JPanel PanelPayerCommande;
    private JPanel InputRecherchePanel;
    private JTextField InputRechercheArticle;
    private JButton ButtonRechercheArticle;
    private JPanel PanelDisplayArticle;
    private JTextPane textArticleInfoPane;
    private JPanel ConsultFacturePanel;
    private JPanel InputTicketDeCaissePanel;
    private JTextField InputTicketReference;
    private JButton ButtonRechercheTicket;
    private JPanel DisplayTicketPanel;
    private JTextPane textFacturePane;
    private JTextField InputDateTicket;

    /**
     * Variable Privé pour les requètes.
     */
    private final Rmi rmi;
    private int refCommande;
    private Hashtable<String, String> refsArticlesList;
    private List<ObjectArticle> ArticlesDeLaCommandeList;
    private DefaultListModel<ObjectArticle> articleListModel;

    public Window() {
        rmi = Rmi.GetInstance();
        ReferenceCommandeButton.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                articleListModel = new DefaultListModel<>();
                if (ReferenceCommande.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Le champ doit être remplie!", "Info", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!ReferenceCommande.getText().matches("^[0-9]{1,9}$")) {
                    JOptionPane.showMessageDialog(null, "Le champ doit contenir que des chiffres et doit contenir 1 à 9 chiffres!", "Info", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    int resultFonction = rmi.getStubArticleAcheteur().creerCommande(Integer.parseInt(ReferenceCommande.getText()));
                    if (resultFonction == -1) {
                        JOptionPane.showMessageDialog(null, "Une erreur s'est produite pendant la création de la commande.", "Info", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (resultFonction == 2) {
                        JOptionPane.showMessageDialog(null, "L'Id de commande qui vous essayer de créer existe déjà et est terminer", "Info", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    if (resultFonction == 1 || resultFonction == 0) {

                        ReferenceCommande.setEnabled(false);
                        ReferenceCommandeButton.setEnabled(false);
                        ContenuCommandePanel.setVisible(true);
                        PanelPayerCommande.setVisible(true);

                        refCommande = Integer.parseInt(ReferenceCommande.getText());
                        refsArticlesList = (Hashtable<String, String>) rmi.getStubArticle().getRefsArticles();
                        ArticleJList.setListData(refsArticlesList.values().toArray(new String[0]));

                    }

                    if (resultFonction == 1) {
                        ArticlesDeLaCommandeList = rmi.getStubArticleAcheteur().consulterCommande(refCommande);
                        JOptionPane.showMessageDialog(null, "reprise de commande.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        for (ObjectArticle article : ArticlesDeLaCommandeList) {
                            articleListModel.addElement(article);
                        }
                    }
                    if (resultFonction == 0) {
                        JOptionPane.showMessageDialog(null, "Commande créer avec succès.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                    ArticlesDeLaCommande.setModel(articleListModel);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        buttonPayer.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                if (ArticlesDeLaCommandeList.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "La commande ne doit pas être vide", "Info", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                JRadioButton especeButton = new JRadioButton(ModePaiement.ESPECE);
                JRadioButton carteButton = new JRadioButton(ModePaiement.CARTE_BANCAIRE);
                JRadioButton chequeButton = new JRadioButton(ModePaiement.CHEQUE);

                ButtonGroup group = new ButtonGroup();
                group.add(especeButton);
                group.add(carteButton);
                group.add(chequeButton);

                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Choisissez un mode de paiement :"));
                panel.add(especeButton);
                panel.add(carteButton);
                panel.add(chequeButton);

                int result = JOptionPane.showConfirmDialog(
                        null,
                        panel,
                        "Mode de Paiement",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (result == JOptionPane.OK_OPTION) {
                    String selectedPayment = null;

                    if (especeButton.isSelected()) {
                        selectedPayment = ModePaiement.ESPECE;
                    } else if (carteButton.isSelected()) {
                        selectedPayment = ModePaiement.CARTE_BANCAIRE;
                    } else if (chequeButton.isSelected()) {
                        selectedPayment = ModePaiement.CHEQUE;
                    }

                    if (selectedPayment == null) {
                        JOptionPane.showMessageDialog(null, "Veuillez sélectionner un mode de paiement.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        rmi.getStubFactureAcheteur().payerFacture(refCommande, selectedPayment);
                        JOptionPane.showMessageDialog(null, "Paiement effectué avec succès !");
                        ClearCommande();
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Erreur lors du paiement.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        AcheterButton.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                int maxQte;
                ObjectArticle obj;
                if (ArticleJList.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(null, "La séléction ne doit pas être vide", "Info", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    String refArticle = getKeyByValue(refsArticlesList, ArticleJList.getSelectedValue());
                    obj = rmi.getStubArticle().getInfoArticle(refArticle);
                    if (obj == null) {
                        JOptionPane.showMessageDialog(null, "Erreur dans la récupération du nombre de l'Article", "Info", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    maxQte = obj.getQte();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }

                Integer selectedQuantity = showQuantitySelectionDialog(maxQte);

                if (selectedQuantity == null) {
                    System.out.println("Achat annulé.");
                    return;
                }

                try {
                    ObjectArticle articleAcheter = rmi.getStubArticleAcheteur().acheterArticle(
                            refCommande,
                            obj.getReferenceArticle(),
                            selectedQuantity
                    );
                    int existingIndex = -1;
                    for (int i = 0; i < ArticlesDeLaCommandeList.size(); i++) {
                        if (ArticlesDeLaCommandeList.get(i).getReferenceArticle().equals(articleAcheter.getReferenceArticle())) {
                            existingIndex = i;
                            break;
                        }
                    }
                    if (existingIndex != -1) {
                        // Replace in both the list and the UI model
                        ArticlesDeLaCommandeList.set(existingIndex, articleAcheter);
                        articleListModel.set(existingIndex, articleAcheter);
                    } else {
                        // Add new if not already in list
                        ArticlesDeLaCommandeList.add(articleAcheter);
                        articleListModel.addElement(articleAcheter);
                    }
                    ArticlesDeLaCommande.setModel(articleListModel);
                    JOptionPane.showMessageDialog(null, "Achat effectué avec succès !");
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erreur lors de l'achat.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        ButtonRechercheArticle.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (InputRechercheArticle.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Le champ doit être remplie!", "Info", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    ObjectArticle retrievedArticle = rmi.getStubArticle().getInfoArticle(InputRechercheArticle.getText());
                    if (retrievedArticle == null) {
                        JOptionPane.showMessageDialog(null, "La référence donnée ne correspond a aucun article!", "Info", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    textArticleInfoPane.setText(retrievedArticle.toStringTextPane());
                } catch (RemoteException ex){
                    throw new RuntimeException(ex);
                }
            }
        });
        ButtonRechercheTicket.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (InputTicketReference.getText().isEmpty() && InputDateTicket.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Les champs doivent être remplie! \n RAPPEL Format Reference Ticket : numbers only \n RAPPEL Format Date : dd-mm-yyyy ", "Info", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!InputTicketReference.getText().matches("^[0-9]*$")) {
                    JOptionPane.showMessageDialog(null, "Le champ doit contenir que des chiffres!", "Info", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!InputDateTicket.getText().matches("^[0-9]{2}-[0-9]{2}-[0-9]{4}$")) {
                    JOptionPane.showMessageDialog(null, "Le champ doit être de ce format : dd-mm-yyyy", "Info", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    ObjectFacture Facture = rmi.getStubFacture().consulterFacture(Integer.parseInt(InputTicketReference.getText()),InputDateTicket.getText());
                    if (Facture == null) {
                        JOptionPane.showMessageDialog(null, "La référence ou la date donnée ne correspondent a aucune facture!", "Info", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    textFacturePane.setText(Facture.toStringTextPane());
                } catch (RemoteException ex){
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        contentPane = new JPanel();
        setContentPane(contentPane);
    }

    private Integer showQuantitySelectionDialog(int maxQuantity) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Choisissez le nombre d'articles à acheter :"));

        JSlider slider = new JSlider(1, maxQuantity);
        slider.setMajorTickSpacing(Math.max(1, maxQuantity / 5));
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        JLabel valueLabel = new JLabel("Quantité: " + slider.getValue());
        slider.addChangeListener(e -> valueLabel.setText("Quantité: " + slider.getValue()));

        panel.add(slider);
        panel.add(valueLabel); // 👈 Show selected value under the slider

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Quantité à acheter",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            return slider.getValue();
        }

        return null;
    }

    public static <K, V> K getKeyByValue(Hashtable<K, V> table, V value) {
        for (K key : table.keySet()) {
            if (table.get(key).equals(value)) {
                return key;
            }
        }
        return null;
    }

    private void ClearCommande() {
        System.out.println("Clear commande");
        // Reset text field
        ReferenceCommande.setText("");
        ReferenceCommande.setEnabled(true);
        ReferenceCommandeButton.setEnabled(true);

        // Clear article list UI
        ArticleJList.setListData(new String[0]);

        // Clear selected articles and UI list
        if (articleListModel != null) {
            articleListModel.clear();
        }
        if (ArticlesDeLaCommandeList != null) {
            ArticlesDeLaCommandeList.clear();
        }

        // Hide command-related panels
        ContenuCommandePanel.setVisible(false);
        PanelPayerCommande.setVisible(false);

        // Reset internal variables
        refCommande = 0;
        refsArticlesList = null;

        // Optional: clear selection
        ArticleJList.clearSelection();
        ArticlesDeLaCommande.clearSelection();

        JOptionPane.showMessageDialog(null, "Commande réinitialisée.");
    }
}
