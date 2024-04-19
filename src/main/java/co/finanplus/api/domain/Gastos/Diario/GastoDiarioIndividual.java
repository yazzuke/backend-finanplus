package co.finanplus.api.domain.Gastos.Diario;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "gastosdiariosindividuales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GastoDiarioIndividual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gastoID;

    @ManyToOne
    @JoinColumn(name = "Gasto_DiarioID")
    @JsonBackReference
    private GastoDiario gastoDiario;

    @Column(name = "Nombre_Gasto")
    private String nombreGasto;

    @Column(name = "Valor_Gasto")
    private BigDecimal valorGasto;

    @Column(name = "Fecha")
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoDiario tipo;

    @Column(name = "Fecha_Insertado")
    private LocalDate fechaInsertado;

    public BigDecimal getValorTotalGasto() {
        return valorGasto;
    }

}
