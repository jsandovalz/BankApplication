# Bank Application — Microservicios Spring Boot

Sistema compuesto por dos microservicios independientes que se comunican de forma asíncrona mediante RabbitMQ.

## Arquitectura

```
┌─────────────────┐         RabbitMQ           ┌──────────────────┐
│  client-service │  ── eventos client.* ──▶  │  account-service  │
│   (puerto 8001) │                            │   (puerto 8000)  │
│                 │                            │                  │
│  Person / Client│                            │ Account/Transact.│
└─────────────────┘                            └──────────────────┘
        │                                                │
        ▼                                                ▼
   H2 (client-db)                                  H2 (account-db)
```

- **client-service**: gestiona `Person` y `Client` (CRUD de clientes).
- **account-service**: gestiona `Account` y `Transaction` (CRUD de cuentas, movimientos y reportes).
- Ambos microservicios tienen su propia base de datos en memoria (H2), sin acceso directo a los datos del otro.
- **Comunicación asíncrona**: cuando `client-service` crea, actualiza o elimina un cliente, publica un evento a RabbitMQ. `account-service` escucha ese evento y mantiene una caché local (`ClientCache`) con el nombre del cliente, usada para armar el reporte de estado de cuenta sin necesidad de llamadas síncronas entre servicios.

## Stack técnico

- Java 11
- Spring Boot (Web, Data JPA, Validation, AMQP)
- H2 Database (en memoria)
- RabbitMQ (mensajería asíncrona)
- Lombok
- JUnit 5 + Mockito

## Patrones y buenas prácticas aplicadas

| Patrón / práctica | Dónde se aplica |
|---|---|
| **Repository** | `ClientRepository`, `AccountRepository`, `TransactionRepository`, `ClientCacheRepository` (Spring Data JPA) |
| **DTO** | Separación entre entidades JPA y contratos de la API (`ClientDto`, `AccountDto`, `TransactionDto`, `BankStatementDto`) |
| **Mapper** | `ClientMapper`, `AccountMapper`, `TransactionMapper` — separan la conversión entidad↔DTO de la lógica de negocio |
| **Publisher/Subscriber** | `ClientEventPublisher` (client-service) y `ClientEventListener` (account-service) vía RabbitMQ |
| **Exception Handling centralizado** | `@RestControllerAdvice` con excepciones de dominio propias (`ResourceNotFoundException`, `DuplicateResourceException`, `InsufficientBalanceException`, `AccountInactiveException`) |
| **Bean Validation** | Anotaciones `@NotBlank`, `@Min`, `@DecimalMin`, `@NotNull` en los DTOs + `@Valid` en los controllers |
| **Inyección por constructor** | En todos los `@Service`, `@RestController` y `@Component` |
| **Transaccionalidad** | `@Transactional` en operaciones de escritura, especialmente en el registro de transacciones (lectura + escritura de saldo) |
| **Logging** | SLF4J (`@Slf4j`) en listener de RabbitMQ |
| **Herencia JPA** | `Base` (`@MappedSuperclass`) → `Person` (`@MappedSuperclass`) → `Client` (`@Entity`) |

## Cómo levantar el proyecto

### 1. RabbitMQ

```bash
apt-get update && apt-get install -y rabbitmq-server
service rabbitmq-server start
```

### 2. account-service (puerto 8000)

```bash
cd account
./mvnw spring-boot:run
```

### 3. client-service (puerto 8001)

```bash
cd client
./mvnw spring-boot:run
```

## Endpoints principales

### client-service — `/api/clients`
| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/clients` | Lista todos los clientes |
| GET | `/api/clients/{id}` | Obtiene un cliente por id |
| POST | `/api/clients` | Crea un cliente |
| PUT | `/api/clients/{id}` | Actualiza un cliente |
| PATCH | `/api/clients/{id}` | Actualiza parcialmente (`isActive`) |
| DELETE | `/api/clients/{id}` | Elimina un cliente |

### account-service — `/api/accounts`
| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/accounts` | Lista todas las cuentas |
| GET | `/api/accounts/{id}` | Obtiene una cuenta por id |
| POST | `/api/accounts` | Crea una cuenta |
| PUT | `/api/accounts/{id}` | Actualiza una cuenta |
| PATCH | `/api/accounts/{id}` | Actualiza parcialmente (`isActive`) |
| DELETE | `/api/accounts/{id}` | Elimina una cuenta |

### account-service — `/api/transactions`
| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/transactions` | Lista todas las transacciones |
| GET | `/api/transactions/{id}` | Obtiene una transacción por id |
| POST | `/api/transactions` | Registra un movimiento (positivo o negativo) |
| GET | `/api/transactions/clients/{clientId}/report?dateTransactionStart=&dateTransactionEnd=` | Reporte de estado de cuenta por rango de fechas |

## Reglas de negocio

- Un movimiento puede ser positivo (crédito) o negativo (débito).
- Al registrar un movimiento se actualiza el saldo disponible de la cuenta.
- Si el movimiento deja el saldo en negativo, se rechaza con el mensaje **"Saldo no disponible"** (HTTP 422).
- No se permiten movimientos sobre cuentas inactivas (HTTP 422).
- No se permiten DNIs ni números de cuenta duplicados (HTTP 409).

## Testing

Cada microservicio incluye en `sampleTest.java`:
- Prueba unitaria de la entidad de dominio (F5).
- Prueba de integración end-to-end contra H2 (F6), incluyendo el caso de saldo no disponible.


## Colección Postman

`collection_bank_postman.json` incluye todos los endpoints de ambos microservicios.
