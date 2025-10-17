# TODO List

Uma aplicação de lista de tarefas em Java feita para rodar no console.  
O usuário pode criar, listar, atualizar, excluir e filtrar tarefas, com salvamento e carregamento de dos dados em um .xml. Atualmente possui um backend e um frontend isolados, cada um com funcionalidades parciais.

## Frontend

### Funcionalidades
- Criar tarefas com:
  - Nome
  - Descrição
  - Data de término
  - Prioridade (1 a 5)
  - Categoria
  - Status (TODO, DOING, DONE)
- Listar todas as tarefas (ordenadas por prioridade).
- Ler uma tarefa
- Atualizar uma tarefa
- Excluir tarefas
- Filtrar tarefas por status

### Requisitos
- Ter um navegador compatível com ES Modules

### Como executar
Basta abrir o arquivo "./frontend/index.html" no navegador

## Backend

### Funcionalidades

- Criar tarefas com:
  - Nome
  - Descrição
  - Data/hora de término
  - Prioridade (1 a 5)
  - Categoria
  - Status (TODO, DOING, DONE)
- Listar todas as tarefas (ordenadas por prioridade).
- Buscar tarefa por Id.
- Atualizar uma tarefa por Id.
- Excluir tarefa por Id.
- Filtrar tarefas por:
  - Prioridade
  - Status
  - Categoria
  - Data de término
- Contagem de tarefas por status.
- Persistência em XML:
  - Carregamento automático ao iniciar.
  - Salvamento automático ao sair.
- Possibilidade de adicionar alarmes às atividades.

### Requisitos
- Java 8+

### Como executar


Para clonar o projeto:

    git clone https://github.com/Lugh-o/TODO-List.git
    cd TODO-List/backend/

Para compilar:

    ./gradlew build

Para executar a aplicação:

    mkdir -p ./data
    ./gradlew run

Para executar os testes unitários:

    ./gradlew test


### Estrutura do XML
O arquivo `./backend/data/tasks.xml` é usado para persistência, ele armazena todas as tarefas e seus lembretes associados.  
Ele contém:

#### Estrutura Geral
```xml
<tasks nextReminderId="N" nextTaskId="M">
    <task id="...">
        ...
    </task>
    <task id="...">
        ...
    </task>
</tasks>
```

#### Estrutura de Task
```xml
<task id="1">
    <name>...</name>
    <description>...</description>
    <endDate>...</endDate>
    <priority>...</priority>
    <category>...</category>
    <status>...</status>
    <creationDate>...</creationDate>
    <modificationDate>...</modificationDate>
    <reminders>
        ...
    </reminders>
</task>
```

#### Estrutura de Reminder
```xml
<reminders>
    <reminder id="1">
        <message>...</message>
        <hoursInAdvance>...</hoursInAdvance>
    </reminder>
</reminders>
```

### Observações
-   Durante qualquer operação, é possível digitar **`q`** para cancelar e voltar ao menu principal.
-   IDs inválidos, campos obrigatórios em branco ou formatos de data incorretos são tratados com mensagens de erro.
-   Datas devem seguir os formatos:
	-   Apenas data: `yyyy-MM-dd`
	-   Data + hora: `yyyy-MM-dd HH:mm`

## Licença
Este projeto é livre para uso pessoal e acadêmico.  Sinta-se à vontade para clonar, modificar e melhorar.