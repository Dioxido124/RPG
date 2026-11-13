RPG em Console (POO & SOLID em Java)

Aplicação de RPG em modo console (terminal) desenvolvida em Java para o Trabalho Prático da disciplina de Paradigmas de Programação. O sistema simula combates em turnos contra um inimigo comum e um chefão final, disponibilizando três classes de heróis (Guerreiro, Mago e Arqueiro), sistema de inventário com poções de cura e equipamentos, além de aplicar de forma explícita os 4 Pilares da Programação Orientada a Objetos (POO) e todos os 5 Princípios SOLID.

📸 Capturas de Tela / Execução no Console

Substitua os links abaixo pelas suas capturas de tela ou GIFs gravados durante a execução do jogo no terminal (ou no OnlineGDB/Programiz).

1. Seleção de Classe e Equipamento Inicial

2. Combate em Turnos contra Inimigo Simples (Goblin Batedor)

3. Uso de Habilidade Especial e Poção de Cura

4. Batalha Final contra o Chefão (Dragão Malakor)

Como Compilar e Executar

Opção 1: Via Terminal Local (Linux, Windows ou macOS)

Certifique-se de ter o JDK 11+ instalado (javac -version). Na pasta onde está o arquivo Main.java, execute:

javac -encoding UTF-8 Main.java
java Main


Opção 2: Via Compilador Online (Sem Instalação)

Acesse o OnlineGDB Java Compiler ou o Programiz Java Online Compiler.

Cole todo o conteúdo de Main.java no editor principal (Main.java).

Clique em Run e digite os números das opções (1, 2 ou 3) diretamente no console interativo.

Mecânicas do Jogo (Main.java)

Classes de Personagens (Personagem):

Guerreiro (HP: 130 | ATQ: 22 | DEF: 14): Alta vida e defesa. Sua habilidade especial Golpe Demolidor aplica 150% do seu ataque total.

Mago (HP: 95 | ATQ: 28 | DEF: 8): Alto ataque mágico. Sua habilidade especial Tempestade Arcana ignora a defesa do inimigo causando dano puro (ATQ + 6) e restaura 10 HP.

Arqueiro (HP: 110 | ATQ: 24 | DEF: 10): Combatente veloz. Sua habilidade especial Flecha Dupla realiza dois disparos consecutivos que ignoram metade da defesa do alvo.

Itens e Inventário (Inventario, PocaoCura, Equipamento):

O herói inicia com duas poções no Inventario (Poção de Cura +45 HP e Poção Grande +65 HP) e um equipamento ativo (Amuleto de Aço: ATQ+5 / DEF+3).

Ao derrotar o Goblin Batedor, o jogador recebe e equipa automaticamente a recompensa (Espada Rúnica: ATQ+8 / DEF+2), fortalecendo-se para o Chefão.

Encontros e Chefão (Inimigo e Chefao):

1º Encontro: Goblin Batedor (HP: 65 | ATQ: 18 | DEF: 6) — inimigo simples com ataque físico padrão.

2º Encontro: Dragão Malakor (Chefão) (HP: 125 | ATQ: 25 | DEF: 10) — subclasse polimórfica Chefao que libera o golpe especial Sopro Vulcânico (ATQ + 10) a cada 2 turnos ou sempre que sua vida fica abaixo de 50%.

📐 Diagrama de Classes UML (Mermaid)

O diagrama abaixo reflete exatamente a arquitetura enxuta implementada em Main.java:

classDiagram
    direction TB

    class InterfaceUsuario {
        <<interface>>
        +mostrar(String msg) void
        +lerOpcao(int min, int max) int
    }

    class RegraDano {
        <<interface>>
        +calcular(int ataque, int defesa) int
    }

    class Combatente {
        <<interface>>
        +getNome() String
        +getVida() int
        +getVidaMax() int
        +getAtaqueTotal() int
        +getDefesaTotal() int
        +estaVivo() boolean
        +receberDano(int dano) int
        +atacar(Combatente alvo, RegraDano regra, InterfaceUsuario ui) void
    }

    class ItemConsumivel {
        <<interface>>
        +getNome() String
        +usar(Personagem alvo, InterfaceUsuario ui) void
    }

    class ItemEquipavel {
        <<interface>>
        +getNome() String
        +getBonusAtaque() int
        +getBonusDefesa() int
    }

    class CalculadoraDano {
        +calcular(int ataque, int defesa) int
    }

    class ConsoleUI {
        -Scanner scanner
        +mostrar(String msg) void
        +lerOpcao(int min, int max) int
    }

    class PocaoCura {
        -String nome
        -int cura
        +getNome() String
        +usar(Personagem alvo, InterfaceUsuario ui) void
    }

    class Equipamento {
        -String nome
        -int bonusAtaque
        -int bonusDefesa
        +getNome() String
        +getBonusAtaque() int
        +getBonusDefesa() int
    }

    class Inventario {
        -List~ItemConsumivel~ pocoes
        +adicionarPocao(ItemConsumivel item) void
        +temPocoes() boolean
        +usarPrimeiraPocao() ItemConsumivel
        +quantidadePocoes() int
    }

    class EntidadeBase {
        <<abstract>>
        -String nome
        -int vida
        -int vidaMax
        -int ataque
        -int defesa
        +estaVivo() boolean
        +receberDano(int dano) int
        +curar(int valor) int
    }

    class Personagem {
        <<abstract>>
        -Inventario inventario
        -ItemEquipavel equipamento
        +equipar(ItemEquipavel novo, InterfaceUsuario ui) void
        +getAtaqueTotal() int
        +getDefesaTotal() int
        +atacar(Combatente alvo, RegraDano regra, InterfaceUsuario ui) void
        +getNomeHabilidade()* String
        +usarHabilidade(Combatente alvo, RegraDano regra, InterfaceUsuario ui)* void
    }

    class Guerreiro {
        +getNomeHabilidade() String
        +usarHabilidade(Combatente alvo, RegraDano regra, InterfaceUsuario ui) void
    }

    class Mago {
        +getNomeHabilidade() String
        +usarHabilidade(Combatente alvo, RegraDano regra, InterfaceUsuario ui) void
    }

    class Arqueiro {
        +getNomeHabilidade() String
        +usarHabilidade(Combatente alvo, RegraDano regra, InterfaceUsuario ui) void
    }

    class Inimigo {
        -ItemEquipavel recompensa
        +getRecompensa() ItemEquipavel
        +atacar(Combatente alvo, RegraDano regra, InterfaceUsuario ui) void
    }

    class Chefao {
        -int turno
        +atacar(Combatente alvo, RegraDano regra, InterfaceUsuario ui) void
    }

    class MotorDeCombate {
        -InterfaceUsuario ui
        -RegraDano regraDano
        +batalhar(Personagem heroi, Inimigo inimigo) boolean
    }

    Combatente <|.. EntidadeBase : implementa
    EntidadeBase <|-- Personagem : herda
    Personagem <|-- Guerreiro : herda
    Personagem <|-- Mago : herda
    Personagem <|-- Arqueiro : herda

    EntidadeBase <|-- Inimigo : herda
    Inimigo <|-- Chefao : herda

    ItemConsumivel <|.. PocaoCura : implementa
    ItemEquipavel <|.. Equipamento : implementa

    Personagem *-- Inventario : composição
    Personagem o-- ItemEquipavel : equipado com
    Inventario o-- ItemConsumivel : armazena
    Inimigo o-- ItemEquipavel : recompensa

    RegraDano <|.. CalculadoraDano : implementa
    InterfaceUsuario <|.. ConsoleUI : implementa

    MotorDeCombate --> InterfaceUsuario : depende de abstração (DIP)
    MotorDeCombate --> RegraDano : depende de abstração (DIP)
    MotorDeCombate ..> Personagem : executa turno
    MotorDeCombate ..> Inimigo : executa turno


Aplicação dos Conceitos de POO

Abstração:

Definida pelas interfaces (Combatente, RegraDano, InterfaceUsuario, ItemConsumivel, ItemEquipavel) e pelas classes abstratas (EntidadeBase e Personagem). O método abstrato usarHabilidade(...) em Personagem obriga cada classe de herói a definir sua própria mecânica especial.

Encapsulamento:

Todos os atributos (vida, vidaMax, ataque, defesa, lista pocoes) são private.

A vida nunca é alterada diretamente por fora da classe: os métodos receberDano(int) e curar(int) em EntidadeBase garantem que a vida permaneça sempre no intervalo válido entre 0 e vidaMax.

Herança e Composição:

Herança: Guerreiro, Mago e Arqueiro estendem Personagem (que estende EntidadeBase); Chefao estende Inimigo.

Composição: Personagem possui um Inventario e um ItemEquipavel, somando dinamicamente os bônus do equipamento em getAtaqueTotal() e getDefesaTotal().

Polimorfismo:

Chamadas como heroi.usarHabilidade(inimigo, regraDano, ui) e inimigo.atacar(heroi, regraDano, ui) dentro do MotorDeCombate executam comportamentos diferentes em tempo de execução conforme a instância real seja um Guerreiro, Mago, Arqueiro, Inimigo ou Chefao.

Onde e Como os 5 Princípios SOLID Foram Aplicados

1. [S] — Single Responsibility Principle (Responsabilidade Única)

Cada classe possui uma única responsabilidade bem delimitada:

CalculadoraDano: Responsável exclusivamente pela regra matemática de cálculo de dano (Math.max(2, ataque - (defesa / 2))).

ConsoleUI: Responsável exclusivamente pela interação com o terminal (System.out.println e validação de entrada com Scanner).

Inventario: Responsável exclusivamente por guardar e remover itens consumíveis da mochila do herói.

MotorDeCombate: Responsável exclusivamente por controlar o loop de turnos de uma batalha.

2. [O] — Open/Closed Principle (Aberto/Fechado)

O sistema está aberto para extensão e fechado para modificação:

Novos Itens Consumíveis (ItemConsumivel): Para criar um novo item (ex.: ElixirDeForca implements ItemConsumivel), basta implementar o método usar(Personagem, InterfaceUsuario). O Inventario e o MotorDeCombate continuam funcionando sem nenhuma modificação.

Novas Fórmulas de Dano (RegraDano) e Novos Personagens (Personagem): Novas classes podem ser adicionadas sem alterar uma única linha do MotorDeCombate.

3. [L] — Liskov Substitution Principle (Substituição de Liskov)

Subclasses podem substituir suas superclasses sem quebrar o funcionamento do programa:

Chefao substituindo Inimigo: Na função main, a lista List<Inimigo> encontros armazena tanto um Inimigo comum (Goblin Batedor) quanto um Chefao (Dragão Malakor). O método motor.batalhar(heroi, inimigo) recebe ambos como Inimigo sem usar instanceof ou condições especiais, e o Chefao executa seu Sopro Vulcânico respeitando integralmente o contrato da superclasse.

Guerreiro, Mago e Arqueiro substituindo Personagem: Qualquer uma das três classes escolhidas pelo usuário opera de maneira uniforme dentro de batalhar(...).

4. [I] — Interface Segregation Principle (Segregação de Interface)

Interfaces específicas são melhores do que uma única interface genérica:

ItemConsumivel vs. ItemEquipavel: Em vez de criar uma única interface ItemGeral contendo tanto usar() quanto getBonusAtaque() (o que forçaria PocaoCura a implementar métodos inúteis de equipamento), o código segrega as capacidades em duas interfaces enxutas: ItemConsumivel (implementada por PocaoCura) e ItemEquipavel (implementada por Equipamento).

5. [D] — Dependency Inversion Principle (Inversão de Dependência)

Módulos de alto nível dependem de abstrações, e não de classes concretas:

Injeção via Construtor no MotorDeCombate: A classe MotorDeCombate não instancia new ConsoleUI() nem new CalculadoraDano() internamente. Ela declara dependência apenas das interfaces InterfaceUsuario e RegraDano, que são injetadas via construtor no método main:

InterfaceUsuario ui = new ConsoleUI(scanner);
RegraDano regraDano = new CalculadoraDano();
MotorDeCombate motor = new MotorDeCombate(ui, regraDano);
