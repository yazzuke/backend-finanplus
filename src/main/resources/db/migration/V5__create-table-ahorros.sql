-- Comando para crear la tabla TarjetasCredito
CREATE TABLE Ahorros (
    AhorroID INT AUTO_INCREMENT PRIMARY KEY,
    UsuarioID VARCHAR(100),
    Concepto VARCHAR(255),
    Meta DECIMAL(10, 2),
    Actual DECIMAL(10,2),
    tipo ENUM('Necesidad', 'Deseos', 'Metas') NOT NULL,
    Fecha DATE,
    FOREIGN KEY (UsuarioID) REFERENCES Usuarios(id)
  );

