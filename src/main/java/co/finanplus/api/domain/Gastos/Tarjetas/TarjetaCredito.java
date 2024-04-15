package co.finanplus.api.domain.Gastos.Tarjetas;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "tarjetascredito")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TarjetaCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TarjetaCreditoID") //
    private Long tarjetaCreditoID; //

    @Column(name = "UsuarioID")
    private String usuarioID;

    @Column(name = "Nombre_Tarjeta")
    private String nombreTarjeta;

    @Column(name = "Fecha_Pago")
    private LocalDate fechaPago;

    @Column(name = "Valor_Total")
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @OneToMany(mappedBy = "tarjetaCredito", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<GastoTarjeta> gastos;

    @Column(name = "Fecha_Insertado")
    private LocalDate fecha;


   
}