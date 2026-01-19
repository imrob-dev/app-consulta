-- Inserir usuários de teste
-- Senha para ambos: password

-- Admin
INSERT INTO users (email, password, nome, telefone, role, active, plano_id, data_expiracao, consultas_usadas, created_at) VALUES
('admin@saas.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Administrador', '11999999999', 'ROLE_ADMIN', true, 3, CURRENT_TIMESTAMP + INTERVAL '365 days', 0, CURRENT_TIMESTAMP);

-- Usuário comum
INSERT INTO users (email, password, nome, telefone, role, active, plano_id, data_expiracao, consultas_usadas, created_at) VALUES
('user@saas.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Usuário Teste', '11988888888', 'ROLE_USER', true, 2, CURRENT_TIMESTAMP + INTERVAL '30 days', 5, CURRENT_TIMESTAMP);
