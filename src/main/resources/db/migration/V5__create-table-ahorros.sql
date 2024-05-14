CREATE TABLE ahorros (
    AhorroID INT AUTO_INCREMENT PRIMARY KEY,
    UsuarioID VARCHAR(100),
    Concepto VARCHAR(255),
    Meta DECIMAL(10, 2),
    Actual DECIMAL(10,2),
    tipo ENUM('Necesidad', 'Deseos', 'Metas') NOT NULL,
    Fecha_Insertado DATE,
     Cerrado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (UsuarioID) REFERENCES usuarios(id)
  );

