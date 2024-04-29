package co.finanplus.api.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalesMensualesDTO {
    private int year;
    private int month;
    private BigDecimal totalIngresos;
    private BigDecimal totalGastos;

    public TotalesMensualesDTO(int year, int month, BigDecimal totalIngresos, BigDecimal totalGastos) {
        this.year = year;
        this.month = month;
        this.totalIngresos = totalIngresos;
        this.totalGastos = totalGastos;
    }

}