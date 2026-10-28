package tn.esprit.tpautoloc.domain;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.tpautoloc.domain.enums.StatutReservation;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"client", "vehicule", "contrat"})
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    private StatutReservation statut;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Vehicule vehicule;

    @OneToOne
    private Contrat contrat;

    /**
     * Alias getter pour compatibilité avec le sujet de l'Atelier 6
     */
    public Long getId() {
        return idReservation;
    }

    public void setId(Long id) {
        this.idReservation = id;
    }

    public void setStatut(StatutReservation statut) {
        this.statut = statut;
    }

    public void setStatut(String statutStr) {
        if (statutStr != null) {
            try {
                this.statut = StatutReservation.valueOf(statutStr);
            } catch (IllegalArgumentException e) {
                this.statut = null;
            }
        } else {
            this.statut = null;
        }
    }

    public static class ReservationBuilder {
        public ReservationBuilder statut(String statutStr) {
            if (statutStr != null) {
                try {
                    this.statut = StatutReservation.valueOf(statutStr);
                } catch (IllegalArgumentException e) {
                    this.statut = null;
                }
            }
            return this;
        }

        public ReservationBuilder statut(StatutReservation statut) {
            this.statut = statut;
            return this;
        }
    }
}
