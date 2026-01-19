-- Dados de teste para H2 (ambiente local)

-- Planos
INSERT INTO planos (nome, descricao, preco, limite_consultas, validade_dias, active) VALUES
('Básico', 'Plano ideal para iniciantes.', 49.90, 100, 30, true);
INSERT INTO planos (nome, descricao, preco, limite_consultas, validade_dias, active) VALUES
('Profissional', 'Plano para profissionais.', 149.90, 500, 30, true);
INSERT INTO planos (nome, descricao, preco, limite_consultas, validade_dias, active) VALUES
('Empresarial', 'Plano completo para empresas.', 399.90, 2000, 30, true);

-- Usuários de teste (senha: password)
INSERT INTO users (email, password, nome, telefone, role, active, plano_id, data_expiracao, consultas_usadas) VALUES
('admin@saas.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Administrador', '11999999999', 'ROLE_ADMIN', true, 3, TIMESTAMPADD(DAY, 365, CURRENT_TIMESTAMP), 0);

INSERT INTO users (email, password, nome, telefone, role, active, plano_id, data_expiracao, consultas_usadas) VALUES
('user@saas.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Usuário Teste', '11988888888', 'ROLE_USER', true, 2, TIMESTAMPADD(DAY, 30, CURRENT_TIMESTAMP), 5);
