package co.finanplus.api.dto;

import java.math.BigDecimal;

public class GastosUpdateRequest {
    private BigDecimal totalGastos;
    private BigDecimal balance;

    public BigDecimal getTotalGastos() {
        return totalGastos;
    }

    public void setTotalGastos(BigDecimal totalGastos) {
        this.totalGastos = totalGastos;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
