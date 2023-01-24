# Api-Chamdos
Api Restfull construida com Spring-Boot. Trata-se de uma api backend para registros de chamados de solicitação de serviço de suporte, conta com tres gupos de usuários (Administrador, suporte e usuario comum), nela um usuário comum pode solicitar um atendimento descrevendo um problema a ser resolvido e um usuário suporte pode classificar e finalizar esse chamado.

## Tecnologias Utilizadas

 - Java 11
 - Spring-boot 2.7.6
 - Spring-security
 - Spring-hateoas
 - Spring-openapi
 - Spring-actuator
 - Postgresql
 - Docker
 - Docker-compose
 - H2 (para testes)

## Para rodar o projeto
Existem duas  maneiras de executar o projeto. Veja a seguir.
### Executando via mvn
Primeiro suba o banco de dados configurado no arquivo docker-compose na raiz do projeto
```
docker-compose up
```
Em seguida rode o projeto com
```
mvn spring-boot:run
```
Pronto, a aplicação estará disponível em http://localhost:8080/
### Executando via Docker
Primeiro vá para o diretório ./src/main/docker
```
cd ./src/main/docker
```
Agora execute o arquivo docker-compose com o comando --build
```
docker-compose up --build
```
Pronto, a aplicação estará disponível em <http://localhost:8000-8999/>, a aplicação é iniciada em uma porta aleatória entre 8000 e 8999
**OPCIONAL:**  Você pode executar mais de uma instancia da aplicação em diferentes portas com o seguinte comando
```
docker-compose up -d --scale chamados=5
```
Acima foi usado como exemplo a criação de cinco instancias da aplicação, mas você pode executar quantas instancias precisar.

**OBS.:** Para saber em que porta a aplicação está rodando basta executar ``docker container ls`` e verificar o numero da porta.
## Criando usuários pra logar na api
Por enquanto a criação de usuarios ainda é manual via inserção direta no banco de dados. mas para testar a api você pode executar o seguinte script para criar alguns usuarios na base de dados.

Com o banco de dados e a aplicação rodando, no diretorio ``src/main/docker`` execute:
```
./inserts.sh
```
Serão inseridos três usuarios no banco de dados, um de cada grupo (ADMIN, SUPPORTER, COMMUN), conforme detalhado na tabela a seguir:
|    Username    |           Password            |       Group                 |
|----------------|-------------------------------|-----------------------------|
|admin           |test123                        |ADMIN                        |
|supporter       |test123                        |SUPPORTER                    |
|user            |test123                        |COMMUN_USER                  |
Lembrando que o usuario do grupo COMMUN_USER pode abrir os chamados, o usuario do grupo SUPPORTER pode classificar e fechar os chamado, usuarios do grupo ADMIN pode fazer tudo isso e ainda podem excluir os chamados.
## Consumindo a api
Para obter detalhes dos endpoints e metodos da api acesse: http://localhost:8080/swagger-ui/index.html (Spring-openapi).
Para monitora a api acesse: http://localhost:8080/actuator (Spring-actuator).

Para mais informações ou sugestões entre em contato.
