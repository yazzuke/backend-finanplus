package co.finanplus.api.domain.Gastos.Diario;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(name = "gastosdiarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GastoDiario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Gasto_DiarioID")
    private Long gastoDiarioID;

    @Column(name = "UsuarioID")
    private String usuarioID;


    @Column(name = "Valor_Total")
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @OneToMany(mappedBy = "gastoDiario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<GastoDiarioIndividual> gastos;



    @Column(name = "Fecha_Insertado")
    private LocalDate fecha;

    


}
