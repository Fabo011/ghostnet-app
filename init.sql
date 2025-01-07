-- Create the appUser table (if not exists)
CREATE TABLE IF NOT EXISTS appuser (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    passwordHash TEXT NOT NULL,
    phoneNumber VARCHAR(20),
    role VARCHAR(50)
);

-- Grant privileges on the appUser table to the ghostnet user
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE appuser TO ghostnet;

-- Create the ghostnet table (if not exists)
CREATE TABLE IF NOT EXISTS ghostnet (
    id SERIAL PRIMARY KEY,
    location VARCHAR(255) NOT NULL,
    size VARCHAR(100) NOT NULL,
    status VARCHAR(50),
    reporter_id BIGINT,  -- Foreign key to appUser (reporter)
    recoveryAgent_id BIGINT,  -- Foreign key to appUser (recovery agent)
    FOREIGN KEY (reporter_id) REFERENCES appUser(id),
    FOREIGN KEY (recoveryAgent_id) REFERENCES appUser(id)
);

-- Grant privileges on the ghostnet table to the ghostnet user
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE ghostnet TO ghostnet;
