CREATE TABLE users
(
    id         SERIAL PRIMARY KEY,
    username   VARCHAR   NOT NULL UNIQUE CHECK (username <> ''),
    password   VARCHAR   NOT NULL CHECK (password <> ''),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TRIGGER updated_at
    BEFORE UPDATE
    ON users
    FOR EACH ROW
EXECUTE PROCEDURE trigger_updated_at();