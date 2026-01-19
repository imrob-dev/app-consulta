-- Tabela de Pagamentos
CREATE TABLE pagamentos (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    plano_id BIGINT NOT NULL REFERENCES planos(id),
    valor DECIMAL(10, 2) NOT NULL,
    codigo_pix TEXT,
    status VARCHAR(50) DEFAULT 'PENDENTE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    confirmed_at TIMESTAMP
);

-- Índices
CREATE INDEX idx_pagamentos_user_id ON pagamentos(user_id);
CREATE INDEX idx_pagamentos_status ON pagamentos(status);
