GRANT ALL PRIVILEGES ON DATABASE ghostnetdatabase TO ghostnet;

-- Create table for UserEntity
CREATE TABLE IF NOT EXISTS appuser (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phonenumber VARCHAR(50), -- Nullable for retrievers
    role VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Create table for GhostNetEntity
CREATE TABLE IF NOT EXISTS ghostnet (
    id SERIAL PRIMARY KEY,
    location VARCHAR(255) NOT NULL, -- GPS coordinates
    size VARCHAR(50),               -- Estimated size
    reporterusername VARCHAR(255),
    missingreportername VARCHAR(255),
    status VARCHAR(50) NOT NULL,   -- Enum type, handled as VARCHAR
    assignedusername VARCHAR(255) -- Nullable for unassigned nets
);

-- Ensure the database user has the necessary privileges on the tables
GRANT ALL PRIVILEGES ON TABLE appuser TO ghostnet;
GRANT ALL PRIVILEGES ON TABLE ghostnet TO ghostnet;