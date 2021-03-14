# Boarding Schedule System

## Motivação

Projeto criado para prover o serviço (REST) de agendamento de embarque para uma empresa fictícia chamada Oil & Gas Corp., onde os seguintes serviços(API) são oferecidos:
	[x] Lista de todas as empresas,
    [x] Cadastro de empresa,
    [x] Busca de uma empresa pelo ID,
    [x] Deleção de uma empresa existente,
    [x] Atualização uma empresa existente,
    [x] Busca de uma empresa pelo nome,
    [x] Deleção um empregado existente,
    [x] Atualização um empregado existente,
    [x] Cadastro de um empregado,
    [x] Lista de todos os empregados,
    [x] Lista de todos os detalhes do empregado,
    [x] Busca de um empregado pelo ID,
    [x] Busca de um empregado pelo nome,
    [x] Cadastro de um agendamento de embarque.
    [x] Atualização de um agendamento existente,
    [x] Deleção de um agendamento,
    [x] Busca de agendamentos entre datas,
    [x] Busca de todos os agendamentos,
    [x] Busca de todos os agendamentos por empregado e 
    [x] Busca de todos os agendamentos por empresa
	
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



