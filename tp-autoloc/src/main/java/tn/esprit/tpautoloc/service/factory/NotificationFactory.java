package tn.esprit.tpautoloc.service.factory;

import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.service.observer.EmailNotificationObserver;
import tn.esprit.tpautoloc.service.observer.IReservationObserver;
import tn.esprit.tpautoloc.service.observer.SmsNotificationObserver;

/**
 * Patron GoF : FACTORY METHOD applique aux observateurs de notification.
 * Permet d'obtenir le bon canal de notification (email, sms, ...) sans exposer
 * les classes concretes au code appelant.
 */
@Component
public class NotificationFactory {

    private final EmailNotificationObserver emailNotificationObserver;
    private final SmsNotificationObserver smsNotificationObserver;

    public NotificationFactory(EmailNotificationObserver emailNotificationObserver,
                               SmsNotificationObserver smsNotificationObserver) {
        this.emailNotificationObserver = emailNotificationObserver;
        this.smsNotificationObserver = smsNotificationObserver;
    }

    public IReservationObserver creerObservateur(String canal) {
        if (canal == null) {
            throw new IllegalArgumentException("Le canal de notification ne peut pas être null");
        }
        switch (canal.trim().toUpperCase()) {
            case "EMAIL":
                return emailNotificationObserver;
            case "SMS":
                return smsNotificationObserver;
            default:
                throw new IllegalArgumentException("Canal de notification inconnu : " + canal);
        }
    }
}
