CREATE TABLE Ingresos (
  IngresoID INT AUTO_INCREMENT PRIMARY KEY,
  UsuarioID VARCHAR(100),
  Concepto VARCHAR(255),
  Monto DECIMAL(10, 2),
  Fecha DATE,
  FOREIGN KEY (UsuarioID) REFERENCES Usuarios(id)
);
