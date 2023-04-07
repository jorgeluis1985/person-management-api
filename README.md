[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=nataelienai_user-manager-api&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=nataelienai_user-manager-api)

# Person Management API

Person Management API é uma simples aplicação para gerenciamento de pessoas. De forma geral, esta API permite criar, editar e
consultar pessoas, assim como os seus endereços.

Para mais detalhes, acesse a [documentação](https://person-management-api.onrender.com/docs) (pode levar alguns minutos
para carregar).

## Tecnologias

- **Linguagem:** Java 11.
- **Ferramentas:** Maven, Docker e Git.
- **Frameworks, bibliotecas e plugins:** Spring Boot, Spring Data JPA, Hibernate Validator, Lombok, JUnit, AssertJ,
Mockito, springdoc-openapi (OpenAPI 3 & Swagger), SonarLint e SonarCloud.
- **Banco de dados:** H2.

## Estrutura de pastas
```
.
└── src/
    ├── main/
    |   └── java/
    |   |   └── io/github/nataelienai/personmanagement
    |   |       ├── controller/   //  responsável pela comunicação com o usuário
    |   |       ├── dto/          //  objetos recebidos/enviados para o usuário
    |   |       |   └── mapper/   //  responsável pelo mapeamento de entity para dto
    |   |       ├── entity/       //  objetos recebidos/enviados para o banco de dados
    |   |       ├── exception/    //  exceptions da própria aplicação
    |   |       ├── repository/   //  responsável pela comunicação com o banco de dados
    |   |       └── service/      //  responsável pela aplicação das regras de negócio
    │   └── resources/
    └── test/
        └── java/
            └── io/github/nataelienai/personmanagement/
                ├── controller/
                ├── repository/
                └── service/
```
## Como executar a API localmente

### Dependências

Você precisará instalar [Git](https://git-scm.com/downloads) e [Docker](https://docs.docker.com/get-docker/) em sua máquina
para conseguir clonar e executar a aplicação.

### Executando a API

1. Dentro de um terminal, clone o repositório:
```sh
git clone https://github.com/nataelienai/person-management-api.git
```

2. Entre na pasta do repositório clonado:
```sh
cd person-management-api
```

3. Crie a imagem Docker da API:
```sh
docker build --tag person-management-api .
```

4. Execute a imagem gerada:
```sh
docker run -it --rm --name person-management-api -p 8080:8080 person-management-api
```

- Aguarde até que apareça linhas similares as seguintes:
```
2023-04-07 16:31:42.324  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2023-04-07 16:31:42.347  INFO 1 --- [           main] i.g.n.p.PersonManagementApplication      : Started PersonManagementApplication in 9.561 seconds (JVM running for 12.128)
```
- A API estará disponível pela URL `http://localhost:8080`. Para encerrar, pressione as teclas `Ctrl + C` no terminal.

### Executando os testes

1. Dentro da pasta do repositório clonado, crie uma imagem Docker da API para ambiente de desenvolvimento:
```sh
docker build --tag person-management-api-dev --target development .
```

2. Execute os testes na imagem gerada:
```sh
docker run -it --rm --name person-management-api-test person-management-api-dev ./mvnw test
```

### Documentação

Com a API em execução, você pode abrir a documentação por um navegador acessando a URL `http://localhost:8080/docs`.
