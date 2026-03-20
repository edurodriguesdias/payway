CREATE TABLE IF NOT EXISTS accounts (
    id BIGSERIAL PRIMARY KEY,
    document_number VARCHAR(14) NOT NULL
);

CREATE UNIQUE INDEX idx_document_number ON accounts (document_number);