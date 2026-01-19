# Painel de Consultas - Sistema SaaS

Sistema monolítico de gerenciamento de consultas desenvolvido com Spring Boot 4.0.1.

## Tecnologias

- **Java 25**
- **Spring Boot 4.0.1**
- **Spring MVC**
- **Spring Security**
- **Spring Data JDBC**
- **Flyway** (migrações de banco)
- **Thymeleaf** + **Bootstrap 5**
- **PostgreSQL 16**
- **Docker & Docker Compose**

## Estrutura do Projeto

```
src/main/java/dev/imrob/painelconsulta/
├── config/          # Configurações (Security, RestClient)
├── controller/      # Controllers MVC e API
│   └── api/         # REST Controllers
├── domain/          # Entidades (User, Consulta, Plano, etc.)
├── dto/
│   ├── request/     # DTOs de entrada
│   └── response/    # DTOs de saída
├── exception/       # Exceções e handlers
├── repository/      # Repositórios Spring Data JDBC
├── security/        # Classes de segurança
└── service/         # Serviços de negócio
```

## Executando o Projeto

### Pré-requisitos

- Java 25+
- Docker e Docker Compose (para PostgreSQL)

### Com Docker Compose (Recomendado)

```bash
# Inicia toda a aplicação (app + PostgreSQL)
docker-compose up

# Ou apenas o banco de dados para desenvolvimento local
docker-compose up db -d

# E então execute a aplicação
./gradlew bootRun
```

### Apenas PostgreSQL local

Se você tem PostgreSQL instalado localmente, configure em `application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/saasconsultas
    username: saas
    password: saas123
```

## Acessando o Sistema

- **URL:** http://localhost:8080
- **Landing Page:** http://localhost:8080/
- **Login:** http://localhost:8080/login

### Usuários de Teste

| Email | Senha | Perfil |
|-------|-------|--------|
| admin@saas.com | password | ADMIN |
| user@saas.com | password | USER |

## Funcionalidades

### Público
- Landing page com planos
- Checkout com QR Code PIX
- Login

### Usuário (ROLE_USER)
- Dashboard com estatísticas
- Nova consulta (CPF, CNPJ, Placa, Telefone, Email, Nome)
- Histórico de consultas (paginado)
- Minha conta (alterar senha, ver plano)

### Admin (ROLE_ADMIN)
- Gerenciar usuários
- Gerenciar planos
- Ativar/desativar contas

## Profiles

- **mock** (padrão): Usa dados fictícios para consultas
- **prod**: Conecta em API externa real

```bash
# Executar com profile mock
./gradlew bootRun --args='--spring.profiles.active=mock'

# Executar com profile prod
./gradlew bootRun --args='--spring.profiles.active=prod'
```

## API REST

### Pública

```
POST /api/v1/users/register    # Registrar novo usuário
GET  /api/v1/planos/public     # Listar planos ativos
POST /api/v1/checkout/iniciar  # Iniciar checkout
GET  /api/v1/checkout/status/{id}  # Verificar pagamento
```

### Autenticada

```
POST /api/v1/consultas         # Realizar consulta
GET  /api/v1/consultas/historico  # Histórico (paginado)
GET  /api/v1/consultas/{id}    # Detalhes da consulta
```

## Tipos de Consulta Suportados

| Tipo | Exemplo |
|------|---------|
| CPF | 14043672764 |
| CNPJ | 31499929000130 |
| PLACA | NCF5440 |
| TELEFONE | 11999999999 |
| EMAIL | usuario@email.com |
| NOME | João Silva |

## Build

```bash
# Compilar
./gradlew build

# Gerar JAR
./gradlew bootJar

# Executar testes (usa H2 em memória)
./gradlew test
```

## Ambiente de Testes

O projeto utiliza **H2 Database** em memória para os testes, configurado no profile `test`:

- **URL:** `jdbc:h2:mem:testdb`
- **Console H2:** http://localhost:8080/h2-console (quando executando com profile test)
- **Mode:** PostgreSQL (compatibilidade)
- **Case Insensitive:** Sim (para compatibilidade com Spring Data JDBC)

Os scripts de schema e dados para testes estão em:
- `src/test/resources/schema-h2.sql`
- `src/test/resources/data-h2.sql`

```bash
# Executar testes
./gradlew test

# Executar aplicação com H2 (para desenvolvimento rápido)
./gradlew bootRun --args='--spring.profiles.active=test,mock'
```

## Docker

```bash
# Build da imagem
docker build -t painelconsulta .

# Executar container
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=mock \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/saasconsultas \
  painelconsulta
```

## Licença

Projeto desenvolvido para fins educacionais.
