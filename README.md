# TODO List

Uma aplicação de lista de tarefas em Java feita para rodar no console.  
O usuário pode criar, listar, atualizar, excluir e filtrar tarefas, com salvamento e carregamento de dos dados em um .xml. Por enquanto somente o backend está feito, com uma interface por linha de comando.

## Funcionalidades

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

## Requisitos
- Java 8+

## Como executar


Para clonar o projeto:

    git clone https://github.com/Lugh-o/TODO-List.git
    cd TODO-List

Para compilar:

    mkdir -p ./backend/out
    javac -d ./backend/out $(find backend/src -name "*.java")

Para executar:

    mkdir -p backend/data
    cd ./backend
    java -cp ./out com.acelerazg.Main

E então programa exibirá o menu principal no console.

## Estrutura do XML
O arquivo `./backend/data/tasks.xml` é usado para persistência, ele armazena todas as tarefas e seus lembretes associados.  
Ele contém:

### Estrutura Geral
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

### Estrutura de Task
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

### Estrutura de Reminder
```xml
<reminders>
    <reminder id="1">
        <message>...</message>
        <hoursInAdvance>...</hoursInAdvance>
    </reminder>
</reminders>
```

## Observações
-   Durante qualquer operação, é possível digitar **`q`** para cancelar e voltar ao menu principal.
-   IDs inválidos, campos obrigatórios em branco ou formatos de data incorretos são tratados com mensagens de erro.
-   Datas devem seguir os formatos:
	-   Apenas data: `yyyy-MM-dd`
	-   Data + hora: `yyyy-MM-dd HH:mm`

## Licença
Este projeto é livre para uso pessoal e acadêmico.  Sinta-se à vontade para clonar, modificar e melhorar.