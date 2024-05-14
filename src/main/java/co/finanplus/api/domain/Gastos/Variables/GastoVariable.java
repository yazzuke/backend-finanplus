package co.finanplus.api.domain.Gastos.Variables;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "gastosvariables")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GastoVariable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Gasto_VariableID")
    private Long gastoVariableID;

    @Column(name = "UsuarioID")
    private String usuarioID;

    @Column(name = "Valor_Total")
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @OneToMany(mappedBy = "gastoVariable", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<GastoVariableIndividual> gastos;



    @Column(name = "Fecha_Insertado")
    private LocalDate fecha;

}
