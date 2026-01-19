-- Tabela de Consultas
CREATE TABLE consultas (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    tipo VARCHAR(50) NOT NULL,
    valor VARCHAR(255) NOT NULL,
    resultado TEXT,
    data_consulta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) DEFAULT 'SUCESSO'
);

-- Índices
CREATE INDEX idx_consultas_user_id ON consultas(user_id);
CREATE INDEX idx_consultas_tipo ON consultas(tipo);
CREATE INDEX idx_consultas_data ON consultas(data_consulta);
