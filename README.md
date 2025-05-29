# Plataforma de Marcenaria

Sistema para conectar marceneiros e clientes de forma organizada, segura e acessível.

## Sobre o Projeto

A plataforma conecta clientes que desejam móveis planejados com marceneiros qualificados, simplificando o processo de orçamento e contratação. Oferecemos uma plataforma segura e transparente para que ambas as partes possam se conectar e realizar projetos com confiança.

### Principais Funcionalidades

- Cadastro e autenticação de usuários (clientes, vendedores e marceneiros)
- Solicitação de orçamentos
- Agendamento de visitas técnicas
- Sistema de lances para projetos
- Gerenciamento de projetos
- Avaliações e feedback

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2.3
- Spring Security com JWT
- Spring Data JPA
- PostgreSQL
- Maven
- Swagger/OpenAPI para documentação

## Configuração do Ambiente

### Pré-requisitos

- JDK 17
- Maven
- PostgreSQL

### Configuração do Banco de Dados

1. Crie um banco de dados PostgreSQL chamado `plataforma_marcenaria`
2. Configure as credenciais no arquivo `application.properties`

### Executando o Projeto

```bash
# Clone o repositório
git clone [URL_DO_REPOSITÓRIO]

# Entre no diretório
cd plataforma-marcenaria

# Instale as dependências
mvn install

# Execute o projeto
mvn spring-boot:run
```

## Documentação da API

A documentação da API está disponível através do Swagger UI em:
```
http://localhost:8080/api/swagger-ui.html
```

## Contribuição

1. Faça um Fork do projeto
2. Crie uma Branch para sua Feature (`git checkout -b feature/AmazingFeature`)
3. Faça o Commit das suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Faça o Push para a Branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes. 