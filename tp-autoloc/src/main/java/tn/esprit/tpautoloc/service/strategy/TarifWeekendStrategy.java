package tn.esprit.tpautoloc.service.strategy;

import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.Vehicule;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Component
public class TarifWeekendStrategy implements ITarificationStrategy {

    private static final BigDecimal REMISE_WEEKEND = new BigDecimal("0.90");

    @Override
    public BigDecimal calculerTarif(Vehicule vehicule, Reservation reservation) {
        long jours = 1;
        if (reservation != null && reservation.getDateDebut() != null && reservation.getDateFin() != null) {
            jours = ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin());
            if (jours <= 0) {
                jours = 1;
            }
        }
        BigDecimal tarifJournalier = (vehicule != null && vehicule.getTarifJournalier() != null)
                ? vehicule.getTarifJournalier()
                : BigDecimal.ZERO;
        BigDecimal base = tarifJournalier.multiply(BigDecimal.valueOf(jours));
        return base.multiply(REMISE_WEEKEND);
    }

    @Override
    public String getCode() {
        return "WEEKEND";
    }
}
