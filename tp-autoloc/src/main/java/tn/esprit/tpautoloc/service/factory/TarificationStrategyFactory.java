package tn.esprit.tpautoloc.service.factory;

import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.service.strategy.ITarificationStrategy;

import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Selectionne dynamiquement la TarificationStrategy adaptee a une reservation
 * (duree du sejour). Toutes les implementations Spring de TarificationStrategy
 * sont injectees automatiquement (Map<code, strategy>).
 */
@Component
public class TarificationStrategyFactory {

    private final Map<String, ITarificationStrategy> strategies;

    public TarificationStrategyFactory(List<ITarificationStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(ITarificationStrategy::getCode, Function.identity()));
    }

    public ITarificationStrategy resoudre(Reservation reservation) {
        if (reservation != null) {
            if (estWeekend(reservation)) {
                return strategies.get("WEEKEND");
            }
            if (reservation.getDateDebut() != null && reservation.getDateFin() != null) {
                long jours = ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin());
                if (jours >= 7) {
                    return strategies.get("LONGUE_DUREE");
                }
            }
        }
        return strategies.get("STANDARD");
    }

    private boolean estWeekend(Reservation reservation) {
        if (reservation == null || reservation.getDateDebut() == null) {
            return false;
        }
        DayOfWeek startDay = reservation.getDateDebut().getDayOfWeek();
        long jours = 1;
        if (reservation.getDateFin() != null) {
            jours = ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin());
        }
        return (startDay == DayOfWeek.FRIDAY || startDay == DayOfWeek.SATURDAY || startDay == DayOfWeek.SUNDAY)
                && (jours <= 3);
    }

    public Map<String, ITarificationStrategy> getStrategies() {
        return strategies;
    }
}
