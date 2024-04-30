package co.finanplus.api.domain.Gastos.Tarjetas;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

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
    
    @Column(name = "Cuota_Total")
    private Integer cuotaTotal;
    
    @Column(name = "Cuota_Actual")
    private Integer cuotaActual;


    @Column(name = "Valor_Cuota_Gasto")
    private BigDecimal valorCuotaGasto;

    @Column(name = "Valor_Total_Gasto")
    private BigDecimal valorTotalGasto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoGasto tipo;

    @Column(name = "Fecha_Insertado")
    private LocalDate fecha;


    public BigDecimal getValorTotalGasto() {
        return valorTotalGasto;
    }

      // Método para incrementar cuotaActual
      public void incrementarCuotaActual() {
        this.cuotaActual++;
    }
    
}


