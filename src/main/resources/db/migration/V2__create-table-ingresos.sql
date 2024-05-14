  CREATE TABLE ingresos (
    IngresoID INT AUTO_INCREMENT PRIMARY KEY,
    UsuarioID VARCHAR(100),
    Concepto VARCHAR(255),
    Monto DECIMAL(10, 2),
    Fecha_Insertado DATE,
    Cerrado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (UsuarioID) REFERENCES usuarios(id)
  );
