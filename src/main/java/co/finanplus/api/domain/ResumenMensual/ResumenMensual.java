package co.finanplus.api.domain.ResumenMensual;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "resumenmensual")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResumenMensual {
    

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resumenID;

    @Column(name = "UsuarioID")
    private String usuarioID;

    @Temporal(TemporalType.DATE)
    @Column(name = "FechaInicio")
    private LocalDate fechaInicio;

    @Column(name = "TotalIngresos")
    private BigDecimal totalIngresos;

    @Column(name = "TotalGastos")
    private BigDecimal totalGastos;

    @Column(name = "Balance")
    private BigDecimal balance;

    @Column(name = "Cerrado", nullable = false)
    private Boolean cerrado;

}
