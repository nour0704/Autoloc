package tn.esprit.tpautoloc.legacy;

/**
 * Version initiale v0 problématique (Atelier 6 - Partie 1)
 * Classe "Dieu" (God Class) concentrant trop de responsabilités :
 * calcul de tarif, sauvegarde de contrat, paiement, notifications.
 */
public class AutoLocManager {

    public void louerVehicule(String typeReservation, int nombreJours, String clientEmail) {
        double prix = 0;
        if ("LongDuree".equals(typeReservation)) {
            prix = nombreJours * 120;
        } else if ("Weekend".equals(typeReservation)) {
            prix = nombreJours * 180;
        } else if ("Standard".equals(typeReservation)) {
            prix = nombreJours * 150;
        }
        System.out.println("Contrat créé");
        System.out.println("Prix total : " + prix);

        envoyerEmail(clientEmail);
        enregistrerPaiement();
        sauvegarderContrat();
    }

    private void envoyerEmail(String email) {
        System.out.println("Email envoyé à " + email);
    }

    private void envoyerSMS(String sms) {
        System.out.println("SMS envoyé à " + sms);
    }

    private void enregistrerPaiement() {
        System.out.println("Paiement enregistré");
    }

    private void sauvegarderContrat() {
        System.out.println("Contrat sauvegardé");
    }
}
