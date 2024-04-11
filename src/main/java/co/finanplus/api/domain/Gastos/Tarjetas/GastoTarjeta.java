package co.finanplus.api.domain.Gastos.Tarjetas;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "gastostarjeta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GastoTarjeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gastoID;

    @ManyToOne
    @JoinColumn(name = "TarjetaCreditoID")
    @JsonBackReference
    private TarjetaCredito tarjetaCredito;

    @Column(name = "Nombre_Gasto")
    private String nombreGasto;

    @Column(name = "Cuota_Gasto")
    private Integer cuotaGasto;

    @Column(name = "Valor_Cuota_Gasto")
    private BigDecimal valorCuotaGasto;

    @Column(name = "Valor_Total_Gasto")
    private BigDecimal valorTotalGasto;

    @Column(name = "Interes")
    private BigDecimal interes;
}
