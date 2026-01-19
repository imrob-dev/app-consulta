-- Tabela de Planos
CREATE TABLE planos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10, 2) NOT NULL,
    limite_consultas INTEGER NOT NULL,
    validade_dias INTEGER NOT NULL,
    active BOOLEAN DEFAULT true
);

-- Inserir planos padrão
INSERT INTO planos (nome, descricao, preco, limite_consultas, validade_dias, active) VALUES
('Básico', 'Plano ideal para iniciantes. Inclui 100 consultas por mês.', 49.90, 100, 30, true),
('Profissional', 'Plano para profissionais. Inclui 500 consultas por mês.', 149.90, 500, 30, true),
('Empresarial', 'Plano completo para empresas. Inclui 2000 consultas por mês.', 399.90, 2000, 30, true);
