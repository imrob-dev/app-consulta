-- Tabela de Clientes (entidades consultadas)
CREATE TABLE clientes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    documento VARCHAR(20) UNIQUE,
    email VARCHAR(255),
    telefone VARCHAR(20),
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices
CREATE INDEX idx_clientes_documento ON clientes(documento);
CREATE INDEX idx_clientes_nome ON clientes(nome);
