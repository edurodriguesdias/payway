CREATE TABLE IF NOT EXISTS operation_types (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(100) NOT NULL
);

INSERT INTO operation_types (description) VALUES
    ('Normal Purchase'),
    ('Purchase with Installments'),
    ('Withdrawal'),
    ('Credit Voucher');