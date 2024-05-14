package co.finanplus.api.domain.Gastos.Fijos;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(name = "gastosfijos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class    GastoFijo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Gasto_FijoID") //
    private Long gastoFijoID; //

    @Column(name = "UsuarioID")
    private String usuarioID;

    @Column(name = "Nombre_Gasto")
    private String nombreGasto;

    @Column(name = "Valor_Total")
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @OneToMany(mappedBy = "gastoFijo", cascade = CascadeType.ALL, orphanRemoval = true) 
    @JsonManagedReference
    private List<GastoInvFijo> gastos; 

    @Column(name = "Fecha_Insertado")
    private LocalDate fecha;

}