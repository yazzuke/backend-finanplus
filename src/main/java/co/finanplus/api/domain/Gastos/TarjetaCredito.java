package co.finanplus.api.domain.Gastos;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tarjetascredito")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TarjetaCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TarjetaCreditoID")  
    private Long TarjetaCreditoID;

    @Column(name = "UsuarioID")
    private String usuarioID;

    @Column(name = "Nombre_Tarjeta")
    private String nombreTarjeta;

    @Column(name = "Fecha_Pago")
    private LocalDate fechaPago;
    
    @Column(name = "Valor_Total")
    private BigDecimal valorTotal;

}