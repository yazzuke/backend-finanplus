package co.finanplus.api.domain.Gastos.Variables;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "gastosvariablesindividuales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class GastoVariableIndividual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gastoID;

    @ManyToOne
    @JoinColumn(name = "Gasto_VariableID")
    @JsonBackReference
    private GastoVariable gastoVariable;

    @Column(name = "Nombre_Gasto")
    private String nombreGasto;

    @Column(name = "Valor_Gasto")
    private BigDecimal valorGasto;

    @Column(name = "Fecha")
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoVariable tipo;

    @Column(name = "Fecha_Insertado")
    private LocalDate fechaInsertado;

    public BigDecimal getValorTotalGasto() {
        return valorGasto;
    }
}
