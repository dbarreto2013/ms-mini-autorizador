
# ms-mini-autorizador

Este projeto é um serviço Spring Boot para o gerenciamento de autorização de transações e cartões. 
Este README detalha as configurações principais e instruções para execução.

## Sumário

- [Tecnologias](#tecnologias)
- [Configuração](#configuração)
- [Executando o Projeto](#executando-o-projeto)

## Tecnologias

- **Java**: 21
- **Spring Boot**: 3.3.4
- **MySQL**: Base de dados relacional para persistência
- **MongoDB**: Base noSQL para persistir transações
- **Flyway**: Migração de banco de dados
- **Docker** e **Docker Compose**: Contêineres para infraestrutura local

## Configuração

### Docker

1. Na pasta `docker`, o arquivo `docker-compose.yml` configura o ambiente para executar o MySQL e MongoDB. Para inicializar, execute:

![Docker2](./docs/images/1_docker.png)


```bash
docker-compose up -d
```
![Docker](./docs/images/1_docker_2.png)
![Docker2](./docs/images/1_docker_3.png)
![Docker2](./docs/images/1_docker_4.png)
![Docker2](./docs/images/1_docker_5.png)
![Docker2](./docs/images/1_docker_6.png)

## Executando o Projeto

Para compilar e executar o projeto:

2. Compile o projeto com Maven:
![Execute](./docs/images/2_execute.png)

   ```bash
   mvn clean install
   ```
![Execute](./docs/images/2_execute_2.png)

3. Inicie a aplicação:

![Init](./docs/images/3_init.png)

   ```bash
   mvn spring-boot:run
   ```
![Init](./docs/images/3_init_2.png)
![Init](./docs/images/3_init_3.png)
![Init](./docs/images/3_init_4.png)
![Init](./docs/images/3_init_5.png)

4. A aplicação estará disponível em `http://localhost:8080`, uso de swagger `http://localhost:8080/swagger-ui/index.html`

![Exectuion](./docs/images/4_execution.png)
![Exectuion](./docs/images/4_execution_2.png)
![Exectuion](./docs/images/4_execution_3.png)
![Exectuion](./docs/images/4_execution_4.png)
![Exectuion](./docs/images/4_execution_5.png)
![Exectuion](./docs/images/4_execution_6.png)
![Exectuion](./docs/images/4_execution_7.png)
![Exectuion](./docs/images/4_execution_8.png)
![Exectuion](./docs/images/4_execution_9.png)
![Exectuion](./docs/images/4_execution_10.png)
![Exectuion](./docs/images/4_execution_11.png)


5. Cenáros de exceção da aplicação

Caso o cartão já exista:
```
Status Code: 422
Body (json):
{
"senha": "1234",
"numeroCartao": "6549873025634501"
}
```
![Exceptions](./docs/images/5_exception.png)


Falha autentição
```
Erro de autenticação: 401 
```
![Exceptions](./docs/images/5_exception_1.png)


Falha autentição
```
Caso o cartão não exista:
   Status Code: 404 
   Sem Body
```
![Exceptions](./docs/images/5_exception_2.png)


Saldo insuficiente
```
   Status Code: 422 
   Body: SALDO_INSUFICIENTE
```
![Exceptions](./docs/images/5_exception_3.png)


Senha inválida
```
   Status Code: 422 
   Body: SENHA_INVALIDA
```
![Exceptions](./docs/images/5_exception_4.png)

Cartão inexistente
```
   Status Code: 422 
   Body: CARTAO_INEXISTENTE
```
![Exceptions](./docs/images/5_exception_5.png)


6. Coverage
![Coverage](./docs/images/6_coverage.png)
