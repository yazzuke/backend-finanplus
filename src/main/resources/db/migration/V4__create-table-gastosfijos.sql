-- Comando para crear la tabla TarjetasCredito
CREATE TABLE GastosFijos (
  Gasto_FijoID INT AUTO_INCREMENT PRIMARY KEY,
  UsuarioID VARCHAR(100),
  Nombre_Gasto VARCHAR(255),
  Valor_Total DECIMAL(10, 2),
  FOREIGN KEY (UsuarioID) REFERENCES Usuarios(id)
);

-- Comando para crear la tabla GastosTarjeta
CREATE TABLE GastosFijosInv (
  GastoID INT AUTO_INCREMENT PRIMARY KEY,
  Gasto_FijoID INT,
  Nombre_Gasto VARCHAR(255),
  Valor_Gasto INT,
  Fecha DATE,
tipo ENUM('Necesidad', 'Deseos', 'Metas') NOT NULL,

   FOREIGN KEY (Gasto_FijoID) REFERENCES GastosFijos(Gasto_FijoID)
);
