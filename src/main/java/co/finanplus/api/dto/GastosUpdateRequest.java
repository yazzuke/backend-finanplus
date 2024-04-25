package co.finanplus.api.dto;

import java.math.BigDecimal;

public class GastosUpdateRequest {
    private BigDecimal totalGastos;

    public BigDecimal getTotalGastos() {
        return totalGastos;
    }

    public void setTotalGastos(BigDecimal totalGastos) {
        this.totalGastos = totalGastos;
    }
}       