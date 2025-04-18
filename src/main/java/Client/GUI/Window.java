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
    private JTextField textFiltreFamille;
    private JPanel PanelFiltre;
    private JTabbedPane EmployerTabbes;
    private JPanel ChiffreAffPanel;
    private JPanel AjoutArticlePanel;
    private JPanel InputPanelChiffreAffaire;
    private JPanel DisplayChiffreAffairePanel;
    private JTextPane textPaneChiffreAffaire;
    private JTextField InputTextDateChiffreAffaire;
    private JButton ButtonChiffreAffaire;
    private JPanel InputAAExemplairePanel;
    private JTextField InputTextReferenceArticle;
    private JButton ButtonInputAAExemplaire;

    /**
     * Variable Privé pour les requètes.
     */
    private final Rmi rmi;
    private int refCommande;
    private Hashtable<String, String> refsArticlesList;
    private List<ObjectArticle> ArticlesDeLaCommandeList;
    private DefaultListModel<ObjectArticle> articleCommandeListModel;

    public Window() {
        rmi = Rmi.GetInstance();
        // Reference
        ReferenceCommandeButton.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateOrGetCommandeActionPerformed(e);
            }
        });
        ReferenceCommande.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateOrGetCommandeActionPerformed(e);
            }
        });

        // Paiement
        buttonPayer.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                if (ArticlesDeLaCommandeList.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "La commande ne doit pas être vide", "Warning", JOptionPane.WARNING_MESSAGE);
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
                    JOptionPane.showMessageDialog(null, "La séléction ne doit pas être vide", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    String refArticle = getKeyByValue(refsArticlesList, ArticleJList.getSelectedValue());
                    obj = rmi.getStubArticle().getInfoArticle(refArticle);
                    if (obj == null) {
                        JOptionPane.showMessageDialog(null, "Erreur dans la récupération du nombre de l'Article", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    maxQte = obj.getQte();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }

                Integer selectedQuantity = showQuantitySelectionDialog(maxQte, "Choisissez le nombre d'articles à acheter :", "Quantité à acheter");

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
                        articleCommandeListModel.set(existingIndex, articleAcheter);
                    } else {
                        // Add new if not already in list
                        ArticlesDeLaCommandeList.add(articleAcheter);
                        articleCommandeListModel.addElement(articleAcheter);
                    }
                    ArticlesDeLaCommande.setModel(articleCommandeListModel);
                    JOptionPane.showMessageDialog(null, "Achat effectué avec succès !");
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erreur lors de l'achat.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Recerche Article
        ButtonRechercheArticle.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                RechercheArticleActionPerformed(e);
            }
        });
        InputRechercheArticle.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                RechercheArticleActionPerformed(e);
            }
        });

        // Recherche Ticket
        ButtonRechercheTicket.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                RechercheTicketActionPerformed(e);
            }
        });
        InputTicketReference.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                RechercheTicketActionPerformed(e);
            }
        });
        InputDateTicket.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                RechercheTicketActionPerformed(e);
            }
        });

        textFiltreFamille.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (textFiltreFamille.getText().isEmpty()) {
                        refsArticlesList = (Hashtable<String, String>) rmi.getStubArticle().getRefsArticles();
                    } else {
                        refsArticlesList = (Hashtable<String, String>) rmi.getStubArticle().getRefsArticles(textFiltreFamille.getText());
                    }
                    if (refsArticlesList == null) {
                        JOptionPane.showMessageDialog(null, "Erreur avec la récupération des articles de la famille " + textFiltreFamille.getText(), "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    ArticleJList.setListData(refsArticlesList.values().toArray(new String[0]));
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Chiffre D'affaire
        InputTextDateChiffreAffaire.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                ChiffreAffaireActionPerformed(e);
            }
        });
        ButtonChiffreAffaire.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                ChiffreAffaireActionPerformed(e);
            }
        });

        // Ajouter exemplaires a un article
        ButtonInputAAExemplaire.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                AjouterExemplaireArticleActionPerformed(e);
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        contentPane = new JPanel();
        setContentPane(contentPane);
    }

    private void CreateOrGetCommandeActionPerformed(java.awt.event.ActionEvent evt) {
        articleCommandeListModel = new DefaultListModel<>();

        if (ReferenceCommande.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Le champ doit être remplie!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!ReferenceCommande.getText().matches("^[0-9]{1,9}$")) {
            JOptionPane.showMessageDialog(null, "Le champ doit contenir que des chiffres et doit contenir 1 à 9 chiffres!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int resultFonction = rmi.getStubArticleAcheteur().creerCommande(Integer.parseInt(ReferenceCommande.getText()));
            if (resultFonction == -1) {
                JOptionPane.showMessageDialog(null, "Une erreur s'est produite pendant la création de la commande.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (resultFonction == 2) {
                JOptionPane.showMessageDialog(null, "L'Id de commande qui vous essayer de créer existe déjà et est terminer", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (resultFonction == 1 || resultFonction == 0) {

                ReferenceCommande.setEnabled(false);
                ReferenceCommandeButton.setEnabled(false);
                ContenuCommandePanel.setVisible(true);
                PanelPayerCommande.setVisible(true);
                PanelFiltre.setVisible(true);

                refCommande = Integer.parseInt(ReferenceCommande.getText());
                refsArticlesList = (Hashtable<String, String>) rmi.getStubArticle().getRefsArticles();
                ArticleJList.setListData(refsArticlesList.values().toArray(new String[0]));
            }

            if (resultFonction == 1) {
                ArticlesDeLaCommandeList = rmi.getStubArticleAcheteur().consulterCommande(refCommande);
                JOptionPane.showMessageDialog(null, "reprise de commande.", "Info", JOptionPane.INFORMATION_MESSAGE);
                for (ObjectArticle article : ArticlesDeLaCommandeList) {
                    articleCommandeListModel.addElement(article);
                }
            }
            if (resultFonction == 0) {
                JOptionPane.showMessageDialog(null, "Commande créer avec succès.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            ArticlesDeLaCommande.setModel(articleCommandeListModel);
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void RechercheArticleActionPerformed(java.awt.event.ActionEvent evt) {
        if (InputRechercheArticle.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Le champ doit être remplie!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ObjectArticle retrievedArticle = rmi.getStubArticle().getInfoArticle(InputRechercheArticle.getText());
            if (retrievedArticle == null) {
                JOptionPane.showMessageDialog(null, "La référence donnée ne correspond a aucun article!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            textArticleInfoPane.setText(retrievedArticle.toStringTextPane());
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void RechercheTicketActionPerformed(java.awt.event.ActionEvent evt) {
        if (InputTicketReference.getText().isEmpty() && InputDateTicket.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Les champs doivent être remplie! \n RAPPEL Format Reference Ticket : numbers only \n RAPPEL Format Date : dd-mm-yyyy ", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!InputTicketReference.getText().matches("^[0-9]*$")) {
            JOptionPane.showMessageDialog(null, "Le champ doit contenir que des chiffres!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!InputDateTicket.getText().matches("^[0-9]{2}-[0-9]{2}-[0-9]{4}$")) {
            JOptionPane.showMessageDialog(null, "Le champ doit être de ce format : dd-mm-yyyy", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ObjectFacture Facture = rmi.getStubFacture().consulterFacture(Integer.parseInt(InputTicketReference.getText()), InputDateTicket.getText());
            if (Facture == null) {
                JOptionPane.showMessageDialog(null, "La référence ou la date donnée ne correspondent a aucune facture!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            textFacturePane.setText(Facture.toStringTextPane());
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void ChiffreAffaireActionPerformed(java.awt.event.ActionEvent evt) {
        if (InputTextDateChiffreAffaire.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Le champ doit être remplie! \n RAPPEL Format Date : dd-mm-yyyy ", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!InputTextDateChiffreAffaire.getText().matches("^[0-9]{2}-[0-9]{2}-[0-9]{4}$")) {
            JOptionPane.showMessageDialog(null, "Le champ doit être de ce format : dd-mm-yyyy", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            float ChiffreAffaire = rmi.getStubCalculCA().getCA(InputTextDateChiffreAffaire.getText());
            if (ChiffreAffaire == -1) {
                JOptionPane.showMessageDialog(null, "La date " + InputTextDateChiffreAffaire.getText() + " n'existe pas!", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            textPaneChiffreAffaire.setText("<html><body style='text-align:center; font-size:1.5em'> Chiffre d'affaire pour la date : " + InputTextDateChiffreAffaire.getText()  + "<br> Chiffre d'affaire : " + ChiffreAffaire + "€ </div></body></html>");
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void AjouterExemplaireArticleActionPerformed(java.awt.event.ActionEvent evt) {
        if (InputTextReferenceArticle.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Le champ doit être remplie!", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer selectedQuantity = showQuantitySelectionDialog(100, "Choisissez le nombre d'articles à ajouter :", "Quantité à ajouter");

        if (selectedQuantity == null) {
            System.out.println("Ajout annulé.");
            return;
        }

        try {
            int status = rmi.getStubArticleEmployer().ajouterArticle(InputTextReferenceArticle.getText(), selectedQuantity);
            switch (status) {
                case -1:
                    JOptionPane.showMessageDialog(null, "Une Erreur s'est produite lors de l'ajout!", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                case 0:
                    JOptionPane.showMessageDialog(null, "L'ajout a bien été réalisé.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                case 1:
                    JOptionPane.showMessageDialog(null, "Aucun article ne correspond a la référence donné!", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                default:

            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Integer showQuantitySelectionDialog(int maxQuantity, String message, String title) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel(message));

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
                title,
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
        if (articleCommandeListModel != null) {
            articleCommandeListModel.clear();
        }
        if (ArticlesDeLaCommandeList != null) {
            ArticlesDeLaCommandeList.clear();
        }

        // Hide command-related panels
        ContenuCommandePanel.setVisible(false);
        PanelPayerCommande.setVisible(false);
        PanelFiltre.setVisible(false);

        // Reset internal variables
        refCommande = 0;
        refsArticlesList = null;

        // Optional: clear selection
        ArticleJList.clearSelection();
        ArticlesDeLaCommande.clearSelection();

        JOptionPane.showMessageDialog(null, "Commande réinitialisée.");
    }
}
