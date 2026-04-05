# Sprint 3 Java - CRM HSR

## Visao geral

Este projeto e uma aplicacao Java focada no cadastro de pacientes e medicos, com uma simulacao de agendamento de consultas. A estrutura segue uma separacao simples por camadas, com classes de modelo, acesso a dados (`DAO`), servicos e utilitarios de infraestrutura.

O sistema usa Oracle Database para persistir os dados de `Paciente` e `Medico`. O fluxo de consultas existe na camada de servico e, no estado atual do projeto, permanece apenas em memoria durante a execucao.

## Objetivo do projeto

O projeto demonstra:

- cadastro de pacientes no banco;
- cadastro de medicos no banco;
- busca, atualizacao e remocao de registros;
- validacao basica para agendamento de consultas;
- uso de JDBC para comunicacao com Oracle;
- aplicacao do padrao `Singleton` para concentrar a conexao com o banco.

## Stack utilizada

- `Java` como linguagem principal;
- `JDBC` para acesso relacional ao banco de dados;
- `Oracle Database` como banco de dados;
- `ojdbc17.jar` como driver JDBC do Oracle;
- `IntelliJ IDEA` como ambiente de desenvolvimento, conforme estrutura do projeto.

## Estrutura do projeto

```text
br.com.crm-hsr/src
├── dao
├── exceptions
├── model
├── service
├── util
└── Main.java
```

## Como o projeto funciona

O ponto de entrada da aplicacao esta em `Main.java`. Ele executa um fluxo de demonstracao:

1. instancia os DAOs;
2. remove registros de teste, se eles ja existirem;
3. cria um paciente de teste;
4. cria um medico de teste;
5. busca os registros salvos no banco;
6. agenda uma consulta usando o servico;
7. lista as consultas do paciente.

## Comunicacao com o banco de dados

A comunicacao com o banco acontece por meio da classe `util.DbConection`, que usa `DriverManager.getConnection(...)` para abrir a conexao JDBC com o Oracle.

Configuracao atual da conexao:

- URL: `jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl`
- Driver: Oracle JDBC (`ojdbc17.jar`)

### Tabelas criadas pela aplicacao

Ao instanciar `PacienteDaoImpl` e `MedicoDaoImpl`, o projeto tenta criar as tabelas automaticamente:

- `PACIENTE`
- `MEDICO`

Se a tabela ja existir, a excecao de codigo Oracle `-955` e ignorada.

## Uso do padrao Singleton em `DbConection`

A classe `DbConection` implementa uma variacao simples do padrao `Singleton` ao manter uma unica referencia estatica de `Connection`:

```java
private static Connection connection;
```

No metodo `getConnection()`, a conexao so e criada se:

- ainda nao existir, ou
- estiver fechada.

## Modelos e utilidade de cada classe

### `model.Pessoa`

Classe base para representar dados comuns de uma pessoa.

Campos principais:

- `id`
- `name`
- `email`
- `number`
- `dateBirthday`

### `model.Paciente`
Utilidade:
- especializar a entidade para a camada de negocio e persistencia;
- permitir regras especificas de paciente no futuro.

### `model.Medico`

Representa uma entidade Medico
Campos adicionais:

- `crm`
- `especialidade`
- `disponibilidadeMedico`

### `model.DisponibilidadeMedico`

Enum que define o estado de disponibilidade do medico.

Valores:

- `DISPONIVEL`
- `OCUPADO`

Utilidade:

- padronizar o status do medico;
- permitir validacao no agendamento de consulta.

### `model.Consulta`

Representa uma consulta entre paciente e medico.

Campos:

- `id`
- `date`
- `paciente`
- `medico`

## Camada DAO

As interfaces DAO definem o contrato de persistencia. As classes `Impl` implementam esse contrato com JDBC.

### `dao.PacienteDao`

Metodos:

- `criar(Paciente paciente)`: insere um novo paciente;
- `buscarPorId(Long id)`: busca um paciente pelo id;
- `buscarPorEmail(String email)`: busca um paciente pelo email;
- `buscarPorNome(String nome)`: busca pacientes por nome usando `LIKE`;
- `listarTodos()`: retorna todos os pacientes;
- `atualizar(Paciente paciente)`: atualiza os dados do paciente;
- `deletar(Long id)`: remove o paciente pelo id.

## Camada de servico

### `service.PacienteService`

Centraliza a regra de negocio ligada ao paciente e ao agendamento.
Funcoes:

- `PacienteService()`: instancia os DAOs necessarios;
- `agendarConsulta(Long pacienteId, Long medicoId)`: valida ids, verifica se paciente e medico existem, valida se o medico esta `DISPONIVEL` e cria uma nova `Consulta`;
- `listarConsultasDoPaciente(Long pacienteId)`: valida o id, verifica se o paciente existe e retorna apenas as consultas associadas a ele.

### `service.MedicoService`

Classe reservada para regras de negocio do medico.

Funcao atual:

- `informacoesDoPacienteDaConsulta(Long idConsulta)`: metodo previsto para exibir dados do paciente de uma consulta, mas ainda nao implementado.

## Tratamento de excecoes

### `exceptions.DatabaseException`

Excecao de tempo de execucao usada para encapsular erros relacionados ao banco de dados.

Utilidade:

- padronizar erros de persistencia;
- evitar espalhar `SQLException` pela aplicacao.

### `exceptions.EntityException`

Excecao de tempo de execucao usada para erros de validacao de negocio.

Utilidade:

- sinalizar dados invalidos;
- impedir o fluxo quando paciente, medico ou ids nao sao validos.