package co.finanplus.api.domain.Gastos.Fijos;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "gastosfijosinv")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GastoInvFijo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gastoID;

    @ManyToOne
    @JoinColumn(name = "Gasto_FijoID")
    @JsonBackReference
    private GastoFijo gastoFijo;

    @Column(name = "Nombre_Gasto")
    private String nombreGasto;

    @Column(name = "Valor_Gasto")
    private Integer valorGasto;

    @Column(name = "Fecha")
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoGasto tipo;

  
}
