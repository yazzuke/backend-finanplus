package co.finanplus.api.domain.Ingresos;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Ingresos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingresoID;

    @Column(name = "UsuarioID")
    private String usuarioID;

    @Column(name = "Concepto")
    private String concepto;

    @Column(name = "Monto")
    private BigDecimal monto;

    @Column(name = "Fecha")
    private LocalDate fecha;

}
