
# Compilador em Java para a Lingaugem SIMPLE
Projeto acadêmico, feito em Java, para compilar a linguagem SIMPLE
| Alunos | Matricula |
| ------ | ------ |
| Vinicius Santana Leão da Silva | 2012130043 |
| Vinícius Moreira de Carvalho | 2122130022 |



## Dependencias necessárias

Dependencias necessárias para rodar o projeto

| Dependecies | Link |
| ------ | ------ |
| Java 17 | https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html |
| Maven | https://maven.apache.org/install.html |


## Como rodar a aplicação

Após instalar todas as dependencias, clonar o projeto do github, abra um terminal, e va no diretório do projeto, e siga as orientações a seguir.

### Onde colocar o código SIMPLE
Dentro do diretório do projeto, entre na pasta src -> main -> resources. Dentro dessa pasta terá um arquivo txt, com o nome de: "codigo.txt". Copie e cole seu código em SIMPLE, dentro do arquivo.

### Maven
Agora, detro do diretório raiz do projeto, executar o comando do maven.
```sh
mvn clean compile exec:java
```

### Outra opção
Seria possivel abrir em uma IDE, e rodar o metodo main, da classe Main também.


## Resultados
Sera mostrado no terminal, os resultados e o progresso de execução.
Caso haja erro, será mostrado no terminal também.
