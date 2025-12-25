# 📊 FeedbackNow API

API REST para **análise de sentimento de feedbacks** (elogios, sugestões e reclamações), desenvolvida em **Java com Spring Boot**, integrada a um **serviço de IA em Python (Flask)** e com **persistência em banco de dados PostgreSQL**.

O projeto foi pensado para cenários reais de coleta e análise de opiniões de usuários, com foco em **qualidade de código, segurança, estatísticas e explicabilidade básica da IA**.

---

## 🚀 Visão Geral

A **FeedbackNow API** permite:

* Enviar comentários para análise de sentimento (positivo ou negativo)
* Integrar com um modelo de IA externo (Python)
* Persistir resultados no banco de dados
* Consultar estatísticas agregadas
* Listar históricos de análises
* Retornar palavras mais influentes na previsão (explicabilidade básica)

---

## 🛠️ Tecnologias Utilizadas

### Backend

* Java 17+
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Security (Basic Auth)
* Hibernate
* PostgreSQL
* Bean Validation
* SLF4J / Logback

### IA / Integração

* Python (Flask)
* Comunicação via REST (RestTemplate)

---

## 🔐 Segurança

* Autenticação **HTTP Basic**
* Todas as rotas protegidas por padrão
* Tratamento customizado para erros de autenticação

**Credenciais padrão (ambiente local):**

```text
Usuário: admin
Senha: 123456
```

---

## 🌐 CORS

Configurado para permitir acesso do frontend local:

```text
http://localhost:3000
```

---

## 🗄️ Banco de Dados

Configuração padrão (PostgreSQL):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/feedbacknow
spring.datasource.username=postgres
spring.datasource.password=postgres123
```

* Tabela principal: **feedbacks**
* Persistência automática com Hibernate (`ddl-auto=update`)

---

## 📦 Estrutura de Pacotes

```text
br.com.feedbacknow.api_feedbacknow
├── config          # Configurações (CORS, Security, RestTemplate)
├── controller      # Controllers REST
├── domain          # Domínio e enums
├── dto             # DTOs de entrada e saída
├── entity          # Entidades JPA
├── exception       # Tratamento global de erros
├── repository      # Repositórios JPA
└── service         # Regras de negócio e integração com IA
```

---

## ▶️ Como Rodar o Projeto (Passo a Passo)

### 1️⃣ Pré-requisitos

* Java 17+
* Maven
* PostgreSQL rodando localmente
* Serviço de IA em Python (Flask) ativo

---

### 2️⃣ Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/feedbacknow.git
cd feedbacknow
```

---

### 3️⃣ Subir o Banco de Dados

Crie o banco no PostgreSQL:

```sql
CREATE DATABASE feedbacknow;
```

---

### 4️⃣ Subir o Serviço de IA (Python)

O serviço Flask deve estar disponível em:

```text
http://localhost:5000/comentario
```

Ele deve receber:

```json
{
  "comentario": "Texto para análise"
}
```

E retornar:

```json
{
  "sentimento": "positivo",
  "probabilidade": 0.95,
  "topFeatures": ["ótimo", "atendimento"]
}
```

---

### 5️⃣ Rodar a API Spring Boot

```bash
mvn spring-boot:run
```

A API estará disponível em:

```text
http://localhost:8080
```

---

## ✅ Health Check

```http
GET /health
```

**Resposta:**

```text
OK
```

---

## 🔍 Endpoints Principais

### 🔹 Analisar Sentimento

```http
POST /sentiment
```

**Body:**

```json
{
  "comentario": "O atendimento foi excelente"
}
```

**Resposta:**

```json
{
  "id": 1,
  "comentario": "O atendimento foi excelente",
  "sentimento": "POSITIVO",
  "probabilidade": 0.97,
  "topFeatures": ["excelente", "atendimento"],
  "timestamp": "23/12/2025 21:10"
}
```

---

### 🔹 Estatísticas Gerais ou por Dias

```http
GET /stats
GET /stats?dias=7
```

**Resposta:**

```json
{
  "total": 100,
  "positivos": 70,
  "negativos": 30,
  "percentualPositivos": 70.0,
  "percentualNegativos": 30.0
}
```

---

### 🔹 Listar Sentimentos (Paginado)

```http
GET /sentiments?page=0&size=10
```

---

### 🔹 Buscar por ID

```http
GET /sentiment/{id}
```

---

## 🧪 Como Testar os Endpoints

### Opção 1️⃣ – Postman / Insomnia

* Use **Basic Auth**
* Configure usuário e senha
* Envie requisições normalmente

### Opção 2️⃣ – cURL

```bash
curl -u admin:123456 \
-X POST http://localhost:8080/sentiment \
-H "Content-Type: application/json" \
-d '{"comentario": "Produto de ótima qualidade"}'
```

---

## 🧠 Recursos Implementados

✔ Análise de sentimento via IA
✔ Persistência dos resultados
✔ Estatísticas agregadas
✔ Explicabilidade básica (topFeatures)
✔ Tratamento global de erros
✔ Logs estruturados
✔ Segurança com Spring Security

---

## 🚧 Recursos Opcionais / Próximos Passos

* 📈 Interface Web (Streamlit ou Frontend JS)
* 🐳 Docker e docker-compose
* 🧪 Testes automatizados (unitários e integração)

---

## 📝 Logs

Os logs são gravados em:

```text
logs/api-feedback.log
```

Com rotação automática.

---

## 👨‍💻 Autor
**Back end
Carlos Oberto Pereira Lima
Everton Guedes 
Kauê Araujo 
Paulo Fleury 

**Data Science 
Felipe Miguel  
Gabriela Duarte do Nascimento
João Batista
Tainah Torres   

Projeto desenvolvido para fins educacionais e profissionais, com foco em **arquitetura limpa, integração com IA e boas práticas em APIs REST**.

---

## 📄 Licença

Este projeto é livre para uso educacional e estudos.
