package tn.esprit.tpautoloc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "maintenance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Maintenance{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long idMainteannce;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String descriptino;
}