# Payway - Sistema de Transações

Sistema de gestão de contas e transações financeiras desenvolvido com Spring Boot

## Stack

- Java 21
- Spring Boot 3.5.11
- PostgreSQL 15
- Docker & Docker Compose
- Flyway (migrations)
- JUnit 5 + Mockito

## Arquitetura

O projeto segue **Arquitetura Hexagonal (Ports and Adapters)** com separação clara entre:

- **Domain**: Regras de negócio, portas (interfaces) e casos de uso
- **Infrastructure**: Implementações técnicas (banco de dados, adapters, mappers)
- **Presentation**: Controllers REST e DTOs

![payway_arq.drawio.svg](docs/payway_arq.drawio.svg)
 
## Funcionalidades

### Contas
- `POST /accounts` - Criar conta
- `GET /accounts/{document}` - Buscar conta por CPF/CNPJ

### Transações
- `POST /transactions` - Criar transação
  - Valida existência da conta
  - Tipos: compra normal, parcelada, saque, crédito

### Referência da API
Você pode acessar a referência da API através do link:
http://localhost:8080/swagger-ui/index.html

## Como Executar

### Pré-requisitos
- Docker Desktop
- Git

### Iniciar

```bash
./run.sh
```

Acesse: http://localhost:8080

### Apenas Banco de Dados

Se preferir rodar a app na IDE:

```bash
docker-compose up postgres
```

Depois execute `PaywayApplication` pela IDE.

## Endpoints

### Criar Conta
```bash
curl -X POST http://localhost:8080/accounts \
  -H "Content-Type: application/json" \
  -d '{"document_number": "12345678901"}'
```

### Buscar Conta
```bash
curl http://localhost:8080/accounts/12345678901
```

### Criar Transação
```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "account_id": 1,
    "operation_type_id": 4,
    "amount": 123.45
  }'
```

## Tipos de Operação

| ID | Tipo | Descrição |
|----|------|-----------|
| 1 | NORMAL_PURCHASE | Compra à vista |
| 2 | PURCHASE_WITH_INSTALLMENTS | Compra parcelada |
| 3 | WITHDRAWAL | Saque |
| 4 | CREDIT_VOUCHER | Crédito |

## Live Reload

Após iniciar com `./run.sh`, em outro terminal:

```bash
# Edite um arquivo .java
./mvnw compile
# Container reinicia automaticamente
```

## Testes

```bash
./mvnw test
```

## Migrations

Localizadas em `src/main/resources/db/migrations/`:

- `V1__create_accounts_table.sql`
- `V2__create_operation_types_table.sql`
- `V3__create_transactions_table.sql`

Executadas automaticamente pelo Flyway na inicialização.

## Parar a aplicação

```bash
docker-compose down
```

## Licença

MIT
