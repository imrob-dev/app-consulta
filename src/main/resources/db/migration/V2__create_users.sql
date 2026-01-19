-- Tabela de Usuários
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER',
    active BOOLEAN DEFAULT false,
    plano_id BIGINT REFERENCES planos(id),
    data_expiracao TIMESTAMP,
    consultas_usadas INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Índices
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_active ON users(active);
CREATE INDEX idx_users_role ON users(role);
