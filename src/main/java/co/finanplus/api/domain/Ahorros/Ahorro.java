package co.finanplus.api.domain.Ahorros;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Ahorros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ahorro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ahorroID;

    @Column(name = "UsuarioID")
    private String usuarioID;

    @Column(name = "Concepto")
    private String concepto;

    @Column(name = "Meta")
    private BigDecimal meta;

    @Column(name = "Actual")
    private BigDecimal actual;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoAhorro tipo;

    @Column(name = "Fecha_Insertado")
    private LocalDate fecha;

}
