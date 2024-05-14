CREATE TABLE tarjetascredito (
  TarjetaCreditoID INT AUTO_INCREMENT PRIMARY KEY,
  UsuarioID VARCHAR(100),
  Nombre_Tarjeta VARCHAR(255),
  Fecha_Pago DATE,
  Valor_Total DECIMAL(10, 2),
  Fecha_Insertado DATE,
  Cerrado BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (UsuarioID) REFERENCES usuarios(id)
);

-- Comando para crear la tabla GastosTarjeta
CREATE TABLE gastostarjeta (
  GastoID INT AUTO_INCREMENT PRIMARY KEY,
  TarjetaCreditoID INT,
  Nombre_Gasto VARCHAR(255),
  Cuota_Total INT,
  Cuota_Actual INT,
  Valor_Cuota_Gasto DECIMAL(10, 2),
  Valor_Total_Gasto DECIMAL(10, 2),
  tipo ENUM('Necesidad', 'Deseos', 'Metas') NOT NULL,
  Fecha_Insertado DATE,
  Cerrado BOOLEAN DEFAULT FALSE,
  Pagado BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (TarjetaCreditoID) REFERENCES tarjetascredito(TarjetaCreditoID)
);
