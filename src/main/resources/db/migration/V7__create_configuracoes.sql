-- Tabela de Configurações do Sistema
CREATE TABLE configuracoes (
    id BIGSERIAL PRIMARY KEY,
    chave VARCHAR(100) NOT NULL UNIQUE,
    valor TEXT,
    descricao TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índice
CREATE INDEX idx_configuracoes_chave ON configuracoes(chave);

-- Inserir configuração padrão
INSERT INTO configuracoes (chave, valor, descricao, updated_at) VALUES
('usar_mock_consultas', 'true', 'Define se as consultas usarão dados fictícios (mock) ou API externa real', CURRENT_TIMESTAMP);
