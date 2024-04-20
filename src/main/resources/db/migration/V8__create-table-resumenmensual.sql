CREATE TABLE ResumenMensual (
    ResumenID INT AUTO_INCREMENT PRIMARY KEY,
    UsuarioID VARCHAR(100) NOT NULL,
    FechaInicio DATE NOT NULL, -- El primer día del mes del resumen
    TotalIngresos DECIMAL(10, 2) NOT NULL,
    TotalGastos DECIMAL(10, 2) NOT NULL,
    Balance DECIMAL(10, 2) NOT NULL, -- Puede calcularse como TotalIngresos - TotalGastos
    Cerrado TINYINT(1) NOT NULL DEFAULT 0, -- 0 para no cerrado, 1 para cerrado
    FOREIGN KEY (UsuarioID) REFERENCES usuarios(ID)
);
