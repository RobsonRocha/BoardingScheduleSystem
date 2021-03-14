# Boarding Schedule System

## Motivação

Projeto criado para prover o serviço (REST) de agendamento de embarque para uma empresa fictícia chamada Oil & Gas Corp., onde os seguintes serviços(API) são oferecidos:
	Lista de todas as empresas,
    Cadastro de empresa,
    Busca de uma empresa pelo ID,
    Deleção de uma empresa existente,
    Atualização uma empresa existente,
    Busca de uma empresa pelo nome,
    Deleção um empregado existente,
    Atualização um empregado existente,
    Cadastro de um empregado,
    Lista de todos os empregados,
    Lista de todos os detalhes do empregado,
    Busca de um empregado pelo ID,
    Busca de um empregado pelo nome,
    Cadastro de um agendamento de embarque.
    Atualização de um agendamento existente,
    Deleção de um agendamento,
    Busca de agendamentos entre datas,
    Busca de todos os agendamentos,
    Busca de todos os agendamentos por empregado e 
    Busca de todos os agendamentos por empresa
	
Os detalhes são encontrados no documento Swagger na url `http://localhost:8080/swagger-ui.html`.
 
## Linguagem

A linguagem utilizada é Java, com auxílio do framework [Spring boot](https://projects.spring.io/spring-boot/) para geração do container.

## Compilação

Para facilitar a importação de bibliotecas e a compilação dos arquivos em um único pacote, foi utilizado [Gradle](https://gradle.org/).
Para compilar gerando o pacote basta executar o comando abaixo na linha de comando.

```./gradlew build```

Na pasta build/lib é gerado o arquivo `BoardingScheduleSystem-1.0.0-SNAPSHOT.jar`

## Banco de dados

Para facilitar a demostração do funcionamento do registro, o banco de dados usado foi o [H2](http://www.h2database.com/html/main.html).


## Testes

Para os testes foram utilizadas as bibliotecas [JUnit](http://junit.org/).
Para executar os testes basta escrever na linha de comando abaixo.

 ```gradle test```


## Execução

Para executar o serviço, foi criado um arquivo shell chamado runApplication.sh que está na raiz do projeto. Ele já cria a imagem localmente e roda o projeto,
rodando todos os testes para verificação. Basta rodar na linha de comando o seguinte:

```./runApplication.sh```



