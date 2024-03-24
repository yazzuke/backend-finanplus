CREATE TABLE Usuarios (
		id Varchar(100) PRIMARY KEY,
		nombre VARCHAR(100),
		email VARCHAR(100) UNIQUE
	);