-- Comando para crear la tabla TarjetasCredito
CREATE TABLE GastosDiarios (
  Gasto_DiarioID INT AUTO_INCREMENT PRIMARY KEY,
  UsuarioID VARCHAR(100),
  Valor_Total DECIMAL(10, 2),
  Fecha_Insertado DATE,
  Cerrado BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (UsuarioID) REFERENCES Usuarios(id)
);

-- Comando para crear la tabla GastosTarjeta
CREATE TABLE GastosDiariosIndividuales (
  GastoID INT AUTO_INCREMENT PRIMARY KEY,
  Gasto_DiarioID INT,
  Nombre_Gasto VARCHAR(255),
  Valor_Gasto INT,
  Fecha DATE,
  tipo ENUM('Necesidad', 'Deseos', 'Metas') NOT NULL,
  Fecha_Insertado DATE,
  Cerrado BOOLEAN DEFAULT FALSE,
   FOREIGN KEY (Gasto_DiarioID) REFERENCES GastosDiarios(Gasto_DiarioID)
);
