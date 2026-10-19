import java.util.ArrayList;
import java.util.List;

/** Todo o conteúdo pedagógico: 6 mundos, do "Olá, mundo" até coleções e exceções. */
public final class Conteudo {
    public static final List<Mundo> MUNDOS = List.of(mundo1(), mundo2(), mundo3(), mundo4(), mundo5(), mundo6());
    public static final List<Missao> MISSOES = missoes();

    private Conteudo() { }

    public static List<Mundo> liberados(Jogador j) {
        List<Mundo> lista = new ArrayList<>();
        for (Mundo m : MUNDOS) if (j.desbloqueou(m.id())) lista.add(m);
        return lista;
    }

    public static List<Desafio> todosDesafios(Jogador j) {
        List<Desafio> lista = new ArrayList<>();
        for (Mundo m : liberados(j)) lista.addAll(m.todos());
        return lista;
    }

    private static List<String> L(String... linhas) { return List.of(linhas); }

    // ------------------------------------------------------------------ MUNDO 1
    private static Mundo mundo1() {
        return new Mundo(1, "Vila Variável", "Slime da Sintaxe",
            "Bem-vindo, aventureiro! Um Slime da Sintaxe roubou todos os pontos e vírgulas da vila. "
            + "Aprenda o básico de Java para recuperá-los!",
            List.of(
                new Licao("Seu primeiro programa", """
                    Todo programa Java começa em uma classe com um método main:

                        public class Main {
                            public static void main(String[] args) {
                                System.out.println("Olá, mundo!");
                            }
                        }

                     - class Main  -> a "caixa" que guarda o programa
                     - main        -> a porta de entrada: o Java começa a executar aqui
                     - println     -> escreve na tela e pula de linha (print não pula)
                     - toda instrução termina com ponto e vírgula ;"""),
                new Licao("Variáveis e tipos", """
                    Uma variável é uma caixinha com nome que guarda um valor.
                    Em Java você precisa dizer o TIPO da caixinha:

                        int idade = 15;          // número inteiro
                        double altura = 1.75;    // número com casas decimais (use ponto!)
                        boolean ligado = true;   // verdadeiro ou falso
                        char letra = 'A';        // um caractere (aspas simples)
                        String nome = "Ana";     // texto (aspas duplas, S maiúsculo)

                    Nomes: sem espaço, não começam com número, e usamos camelCase (nomeCompleto)."""),
                new Licao("Operadores e conversões", """
                    Operadores: + - * / e % (resto da divisão)

                        int a = 7 / 2;        // 3    (inteiro / inteiro = inteiro!)
                        double b = 7 / 2.0;   // 3.5
                        int resto = 7 % 2;    // 1

                    O + também junta textos:

                        String msg = "Idade: " + 15;   // "Idade: 15"

                    Conversão manual (cast):

                        int n = (int) 9.99;   // vira 9 (corta, não arredonda)"""),
                new Licao("Lendo do teclado", """
                    Para o programa conversar com a pessoa, use Scanner:

                        import java.util.Scanner;

                        Scanner teclado = new Scanner(System.in);
                        System.out.print("Seu nome: ");
                        String nome = teclado.nextLine();   // lê uma linha de texto
                        int idade = teclado.nextInt();      // lê um número inteiro

                    Dica: o import fica no topo do arquivo, antes da classe.""")),
            List.of(
                Quiz.de("Qual tipo guarda o valor 3.14?", "Números com casas decimais usam double.",
                        "double", "int", "String", "boolean"),
                Quiz.de("Qual declaração de variável é válida?", "Tipos primitivos são minúsculos, e nomes não começam com número.",
                        "int idade = 5;", "int 2idade = 5;", "Int idade = 5;", "int idade == 5;"),
                Quiz.de("Qual é o valor de r depois de:\n\n    int r = 7 / 2;", "Divisão entre inteiros descarta a parte decimal: 7/2 = 3.",
                        "3", "3.5", "4", "2"),
                Quiz.de("Qual comando escreve um texto na tela?", "System.out.println escreve e pula de linha.",
                        "System.out.println(\"Oi\");", "print(\"Oi\");", "echo \"Oi\";", "Console.WriteLine(\"Oi\");"),
                Quiz.de("Qual destes é um valor boolean?", "boolean só aceita true ou false, sem aspas.",
                        "true", "\"true\" (com aspas)", "1", "'t'")),
            List.of(
                new Bug("Algo impede de compilar", L(
                    "public class Main {",
                    "    public static void main(String[] args) {",
                    "        int idade = 15;",
                    "        String nome = \"Ana\"",
                    "        System.out.println(nome + \" tem \" + idade);",
                    "    }",
                    "}"), 4, "Faltou o ponto e vírgula no final da linha 4."),
                new Bug("Tipo errado", L(
                    "public class Main {",
                    "    public static void main(String[] args) {",
                    "        int pontos = \"100\";",
                    "        System.out.println(pontos);",
                    "    }",
                    "}"), 3, "int guarda números; \"100\" entre aspas é um String. Use int pontos = 100;")),
            List.of(
                new Lacuna("Declare uma variável de texto.", L("____ nome = \"Luz\";"), List.of("String"),
                        "Tipo de texto, começa com S maiúsculo.", "Textos em Java são do tipo String."),
                new Lacuna("Converta o double em int.", L("double preco = 9.99;", "int inteiro = (____) preco;"),
                        List.of("int"), "Você quer virar um número inteiro.", "O cast (int) corta a parte decimal.")),
            List.of(
                new Saida("Divisão", L("int a = 7;", "int b = 2;", "System.out.println(a / b);"), "3",
                        "Inteiro dividido por inteiro dá inteiro."),
                new Saida("Juntando textos", L("String s = \"Ja\" + \"va\" + 1 + 2;", "System.out.println(s);"), "Java12",
                        "Com um texto na conta, o + junta (concatena) tudo: não soma 1 + 2.")));
    }

    // ------------------------------------------------------------------ MUNDO 2
    private static Mundo mundo2() {
        return new Mundo(2, "Floresta Condicional", "Lobo Else",
            "Na floresta, cada caminho depende de uma decisão. O Lobo Else só deixa passar quem sabe tomar decisões em código!",
            List.of(
                new Licao("if e else", """
                    O if executa um bloco só se a condição for verdadeira:

                        int idade = 17;
                        if (idade >= 18) {
                            System.out.println("Maior de idade");
                        } else if (idade >= 12) {
                            System.out.println("Adolescente");
                        } else {
                            System.out.println("Criança");
                        }

                    A condição vai entre parênteses e precisa resultar em boolean."""),
                new Licao("Comparação e lógica", """
                    Comparar: == (igual)  != (diferente)  >  <  >=  <=
                    Lógica:   && (E)   || (OU)   ! (NÃO)

                        if (nota >= 7 && faltas < 10) { ... }
                        if (!ligado) { ... }

                    CUIDADO: = atribui um valor, == compara!
                    Para textos, compare com equals, nunca com ==:

                        if (nome.equals("Ana")) { ... }"""),
                new Licao("switch", """
                    Quando há muitos casos para um mesmo valor, use switch:

                        switch (dia) {
                            case 1: System.out.println("Domingo"); break;
                            case 2: System.out.println("Segunda"); break;
                            default: System.out.println("Outro dia");
                        }

                    O break é essencial: sem ele o Java "cai" no próximo case!""")),
            List.of(
                Quiz.de("Qual operador compara se dois números são iguais?", "== compara; = apenas atribui.",
                        "==", "=", "===", "=>"),
                Quiz.de("Com x = 5, qual o resultado de:\n\n    (x > 5 && x < 10)", "5 > 5 é falso, e && exige que as duas sejam verdadeiras.",
                        "false", "true", "5", "erro de compilação"),
                Quiz.de("Como comparar o CONTEÚDO de duas Strings?", "equals compara o texto; == compara se é o mesmo objeto.",
                        "a.equals(b)", "a == b", "a = b", "a.compare(b)"),
                Quiz.de("Qual bloco roda quando nenhuma condição do if é verdadeira?", "O else é o caminho padrão.",
                        "else", "elif", "otherwise", "default do if"),
                Quiz.de("O que o operador ! faz em !ligado?", "! inverte: true vira false e false vira true.",
                        "Inverte o valor boolean", "Multiplica por -1", "Comenta a linha", "Compara com zero")),
            List.of(
                new Bug("A vida nunca chega ao fim", L(
                    "int vida = 10;",
                    "if (vida = 0) {",
                    "    System.out.println(\"Game over\");",
                    "}"), 2, "= atribui e não retorna boolean. O certo é vida == 0."),
                new Bug("A senha nunca confere", L(
                    "String senha = \"java\";",
                    "String tentativa = new Scanner(System.in).nextLine();",
                    "if (tentativa == senha) {",
                    "    System.out.println(\"Acesso liberado\");",
                    "}"), 3, "== compara referências (objetos), não o texto. Use tentativa.equals(senha).")),
            List.of(
                new Lacuna("Faça o programa escolher entre dois caminhos.", L(
                    "if (idade >= 18) {", "    System.out.println(\"Maior\");", "} ____ {",
                    "    System.out.println(\"Menor\");", "}"), List.of("else"),
                        "O caminho 'senão'.", "else roda quando o if é falso."),
                new Lacuna("Compare dois textos do jeito certo.", L(
                    "String nome = \"Ana\";", "if (nome.____(\"Ana\")) {", "    System.out.println(\"Oi, Ana!\");", "}"),
                        List.of("equals"), "Começa com 'eq'.", "equals compara o conteúdo dos textos.")),
            List.of(
                new Saida("Qual caminho?", L("int x = 10;", "if (x > 5) {", "    System.out.println(\"A\");",
                        "} else {", "    System.out.println(\"B\");", "}"), "A", "10 > 5 é verdadeiro, então entra no if."),
                new Saida("O switch sem break", L("int n = 3;", "switch (n) {",
                        "    case 1: System.out.println(\"um\"); break;",
                        "    case 3: System.out.println(\"tres\");",
                        "    case 4: System.out.println(\"quatro\"); break;",
                        "    default: System.out.println(\"outro\");", "}"), "tres quatro",
                        "O case 3 não tem break, então 'cai' no case 4 e imprime os dois.")));
    }

    // ------------------------------------------------------------------ MUNDO 3
    private static Mundo mundo3() {
        return new Mundo(3, "Montanha dos Laços", "Golem do Loop Infinito",
            "Para escalar a montanha é preciso repetir passos muitas vezes. Mas cuidado com o Golem do Loop Infinito: ele nunca para!",
            List.of(
                new Licao("O laço for", """
                    Use for quando você sabe quantas vezes repetir:

                        for (int i = 0; i < 5; i++) {
                            System.out.println("Volta " + i);
                        }

                    Três partes: início (int i = 0), condição (i < 5), passo (i++).
                    Esse laço imprime Volta 0 até Volta 4 (5 voltas)."""),
                new Licao("while e do-while", """
                    while repete enquanto a condição for verdadeira:

                        int vida = 3;
                        while (vida > 0) {
                            System.out.println("Vida: " + vida);
                            vida--;
                        }

                    do-while roda o corpo PELO MENOS uma vez (testa no final):

                        do {
                            System.out.println("Menu...");
                        } while (opcao != 0);"""),
                new Licao("break e continue", """
                    break  -> sai do laço imediatamente
                    continue -> pula para a próxima volta

                        for (int i = 1; i <= 5; i++) {
                            if (i == 2) continue;   // pula o 2
                            if (i == 4) break;      // para no 4
                            System.out.println(i);  // imprime 1 e 3
                        }

                    Se a condição nunca ficar falsa, o laço é infinito. Cuidado!""")),
            List.of(
                Quiz.de("Quantas vezes o corpo roda?\n\n    for (int i = 0; i < 3; i++) { ... }", "i vale 0, 1 e 2: três voltas.",
                        "3", "2", "4", "Infinitas"),
                Quiz.de("Qual laço executa o corpo PELO MENOS uma vez?", "O do-while testa a condição só depois de executar.",
                        "do-while", "for", "while", "for-each"),
                Quiz.de("O que faz o break dentro de um laço?", "break interrompe o laço na hora.",
                        "Encerra o laço imediatamente", "Pula só a volta atual", "Reinicia o laço", "Encerra o programa"),
                Quiz.de("O que faz o continue?", "continue ignora o resto da volta e vai para a próxima.",
                        "Pula para a próxima volta do laço", "Encerra o laço", "Reinicia o programa", "Repete a mesma volta"),
                Quiz.de("Qual laço é o mais indicado quando você sabe quantas vezes repetir?", "O for reúne início, condição e passo.",
                        "for", "while", "do-while", "switch")),
            List.of(
                new Bug("Contagem regressiva sem fim", L(
                    "for (int i = 0; i < 3; i--) {",
                    "    System.out.println(\"Vida \" + i);",
                    "}"), 1, "i-- faz i diminuir e nunca chegar a 3: laço infinito. Deveria ser i++."),
                new Bug("A soma não soma", L(
                    "int soma = 0;",
                    "for (int i = 1; i <= 10; i++) {",
                    "    soma = i;",
                    "}",
                    "System.out.println(\"Soma: \" + soma);"), 3, "soma = i troca o valor. Para acumular use soma += i.")),
            List.of(
                new Lacuna("Crie um laço que conta de 1 a 3.", L(
                    "____ (int i = 1; i <= 3; i++) {", "    System.out.println(i);", "}"), List.of("for"),
                        "Três partes entre parênteses separadas por ;", "for é o laço contado."),
                new Lacuna("Pule o número 2 sem encerrar o laço.", L(
                    "for (int i = 1; i <= 5; i++) {", "    if (i == 2) ____;", "    System.out.println(i);", "}"),
                        List.of("continue"), "Significa 'continuar' na próxima volta.", "continue pula direto para a próxima volta.")),
            List.of(
                new Saida("Dobrando", L("for (int i = 1; i <= 3; i++) {", "    System.out.print(i * 2 + \" \");", "}"),
                        "2 4 6", "print não pula linha: 2, 4 e 6 saem lado a lado."),
                new Saida("Acumulador", L("int s = 0;", "for (int i = 1; i <= 4; i++) {", "    s += i;", "}",
                        "System.out.println(s);"), "10", "1 + 2 + 3 + 4 = 10.")));
    }

    // ------------------------------------------------------------------ MUNDO 4
    private static Mundo mundo4() {
        return new Mundo(4, "Cidade dos Métodos e Arrays", "Hidra de Arrays",
            "A cidade guarda tudo em prateleiras (arrays) e usa ferramentas reutilizáveis (métodos). A Hidra de Arrays quer bagunçar tudo!",
            List.of(
                new Licao("Métodos", """
                    Um método é um bloco de código com nome, que você chama quando quiser:

                        static int somar(int a, int b) {
                            return a + b;
                        }

                        int resultado = somar(2, 3);   // 5

                     - int       -> tipo do que o método devolve
                     - a, b      -> parâmetros (entradas)
                     - return    -> devolve o resultado
                     - void      -> quando não devolve nada"""),
                new Licao("Arrays", """
                    Array guarda vários valores do mesmo tipo, com tamanho fixo:

                        int[] notas = new int[3];       // 3 posições: 0, 1 e 2
                        notas[0] = 8;
                        int[] pontos = {10, 20, 30};    // já com valores
                        System.out.println(pontos.length);   // 3

                    O primeiro índice é 0 e o último é length - 1.
                    Passar do limite causa ArrayIndexOutOfBoundsException."""),
                new Licao("Strings", """
                    String tem vários métodos prontos:

                        String t = "banana";
                        t.length()         // 6
                        t.charAt(0)        // 'b'
                        t.indexOf("n")     // 2 (posição da primeira ocorrência)
                        t.toUpperCase()    // "BANANA"
                        t.substring(1, 3)  // "an"
                        t.contains("nan")  // true

                    Strings são imutáveis: os métodos devolvem uma NOVA String.""")),
            List.of(
                Quiz.de("Qual é o índice do primeiro elemento de um array?", "Em Java a contagem começa no zero.",
                        "0", "1", "-1", "Depende do tipo"),
                Quiz.de("Que palavra indica que um método NÃO devolve valor?", "void significa 'vazio'.",
                        "void", "null", "empty", "none"),
                Quiz.de("O que \"Java\".length() devolve?", "A palavra tem 4 letras.", "4", "3", "5", "\"Java\""),
                Quiz.de("Como saber o tamanho de um array chamado notas?", "Em array, length é um campo (sem parênteses).",
                        "notas.length", "notas.length()", "notas.size()", "length(notas)"),
                Quiz.de("Qual é o índice do último elemento de um array com 5 posições?", "Índices vão de 0 a 4.",
                        "4", "5", "6", "3")),
            List.of(
                new Bug("Fora dos limites", L(
                    "int[] vidas = new int[3];",
                    "vidas[0] = 10;",
                    "vidas[1] = 20;",
                    "vidas[3] = 30;"), 4, "Com 3 posições os índices são 0, 1 e 2. vidas[3] lança ArrayIndexOutOfBoundsException."),
                new Bug("O método que não devolve", L(
                    "static int dobro(int n) {",
                    "    n * 2;",
                    "}"), 2, "Faltou o return: o certo é return n * 2;")),
            List.of(
                new Lacuna("Crie um array com 3 posições.", L("int[] notas = ____ int[3];"), List.of("new"),
                        "A mesma palavra que cria objetos.", "new reserva o espaço do array na memória."),
                new Lacuna("Complete o tipo de retorno.", L("static ____ soma(int a, int b) {", "    return a + b;", "}"),
                        List.of("int"), "Somar dois inteiros dá o quê?", "O método devolve a soma, que é int.")),
            List.of(
                new Saida("Array e tamanho", L("int[] v = {4, 8, 2};", "System.out.println(v[1] + v.length);"), "11",
                        "v[1] é 8 e v.length é 3: 8 + 3 = 11."),
                new Saida("Métodos de String", L("String t = \"banana\";",
                        "System.out.println(t.toUpperCase().charAt(0) + \"-\" + t.length());"), "B-6",
                        "toUpperCase vira BANANA, charAt(0) é 'B' e length() é 6.")));
    }

    // ------------------------------------------------------------------ MUNDO 5
    private static Mundo mundo5() {
        return new Mundo(5, "Castelo da Orientação a Objetos", "Dragão da Herança",
            "No castelo, tudo é um objeto com atributos e comportamentos. O Dragão da Herança guarda o tesouro final do básico!",
            List.of(
                new Licao("Classes e objetos", """
                    Classe é o molde; objeto é a coisa criada a partir do molde.

                        public class Pessoa {
                            String nome;          // atributo
                            int idade;

                            Pessoa(String nome, int idade) {   // construtor
                                this.nome = nome;
                                this.idade = idade;
                            }

                            void apresentar() {                // método
                                System.out.println("Oi, eu sou " + nome);
                            }
                        }

                        Pessoa p = new Pessoa("Ana", 15);
                        p.apresentar();"""),
                new Licao("Encapsulamento", """
                    Esconda os atributos com private e libere o acesso por métodos:

                        public class Conta {
                            private double saldo;

                            public double getSaldo() { return saldo; }

                            public void depositar(double valor) {
                                if (valor > 0) saldo += valor;   // regra de proteção
                            }
                        }

                    Assim ninguém coloca um saldo inválido direto no atributo.
                    this = "este objeto" (distingue atributo de parâmetro)."""),
                new Licao("Herança e polimorfismo", """
                    Uma classe pode herdar de outra com extends:

                        class Animal {
                            void falar() { System.out.println("..."); }
                        }
                        class Cachorro extends Animal {
                            @Override
                            void falar() { System.out.println("Au!"); }
                        }

                        Animal a = new Cachorro();
                        a.falar();   // Au!  (polimorfismo)

                    Polimorfismo: a mesma chamada age diferente conforme o objeto real."""),
                new Licao("Interfaces", """
                    Interface é um contrato: diz O QUE fazer, não COMO.

                        interface Voador {
                            void voar();
                        }
                        class Passaro implements Voador {
                            public void voar() { System.out.println("Bati asas!"); }
                        }

                    Uma classe herda de UMA classe (extends) mas pode implementar VÁRIAS interfaces.""")),
            List.of(
                Quiz.de("Qual palavra-chave cria um objeto?", "new aloca o objeto e chama o construtor.",
                        "new", "create", "object", "make"),
                Quiz.de("Qual é o nome do construtor da classe Gato?", "O construtor tem o mesmo nome da classe e não tem tipo de retorno.",
                        "Gato (o mesmo da classe)", "init", "main", "construir"),
                Quiz.de("Qual palavra faz uma classe herdar de outra?", "class B extends A: B herda de A.",
                        "extends", "implements", "inherits", "super"),
                Quiz.de("O que é polimorfismo?", "Mesma mensagem, comportamentos diferentes conforme o objeto.",
                        "Objetos diferentes respondendo ao mesmo método de jeitos diferentes",
                        "Criar muitas variáveis iguais", "Esconder atributos com private", "Converter String em int"),
                Quiz.de("Qual palavra impede acesso a um atributo de fora da classe?", "private restringe ao interior da classe.",
                        "private", "static", "final", "void")),
            List.of(
                new Bug("O construtor que não construía", L(
                    "public class Pessoa {",
                    "    private String nome;",
                    "    public void Pessoa(String nome) {",
                    "        this.nome = nome;",
                    "    }",
                    "}"), 3, "Construtor não tem tipo de retorno. Com void vira um método comum e o nome nunca é guardado."),
                new Bug("Cofre aberto", L(
                    "public class Conta {",
                    "    private double saldo = 0;",
                    "}",
                    "// em outra classe:",
                    "Conta c = new Conta();",
                    "c.saldo = 1000;"), 6, "saldo é private: fora da classe só dá para mexer por métodos (ex.: depositar).")),
            List.of(
                new Lacuna("Faça Cachorro herdar de Animal.", L("public class Cachorro ____ Animal {", "}"), List.of("extends"),
                        "Significa 'estende'.", "extends indica a classe-mãe."),
                new Lacuna("Guarde o parâmetro no atributo.", L("public Pessoa(String nome) {", "    ____.nome = nome;", "}"),
                        List.of("this"), "Palavra que significa 'este objeto'.", "this.nome é o atributo; nome sozinho é o parâmetro.")),
            List.of(
                new Saida("Polimorfismo", L(
                    "class Animal { void falar() { System.out.println(\"...\"); } }",
                    "class Cachorro extends Animal { void falar() { System.out.println(\"Au!\"); } }",
                    "Animal a = new Cachorro();",
                    "a.falar();"), "Au!", "O objeto real é um Cachorro, então roda o falar() do Cachorro."),
                new Saida("Contador", L(
                    "class Contador {",
                    "    private int valor = 0;",
                    "    void incrementar() { valor++; }",
                    "    int getValor() { return valor; }",
                    "}",
                    "Contador c = new Contador();",
                    "c.incrementar();",
                    "c.incrementar();",
                    "System.out.println(c.getValor());"), "2", "Duas chamadas de incrementar: 0 -> 1 -> 2.")));
    }

    // ------------------------------------------------------------------ MUNDO 6
    private static Mundo mundo6() {
        return new Mundo(6, "Torre das Coleções e Exceções", "Lich NullPointer",
            "No topo da torre vivem as coleções e os erros traiçoeiros. O Lich NullPointer promete derrubar quem não trata exceções!",
            List.of(
                new Licao("ArrayList e HashMap", """
                    Coleções crescem e encolhem sozinhas:

                        import java.util.*;

                        List<String> nomes = new ArrayList<>();
                        nomes.add("Ana");
                        nomes.add("Bia");
                        nomes.get(0);       // "Ana"
                        nomes.size();       // 2
                        nomes.remove("Ana");

                        Map<String, Integer> notas = new HashMap<>();
                        notas.put("Ana", 9);
                        notas.get("Ana");   // 9

                    Dentro de <> só entram objetos (Integer, não int). Percorra com for-each:
                    for (String n : nomes) { ... }"""),
                new Licao("Exceções", """
                    Exceções são erros em tempo de execução. Trate com try/catch:

                        try {
                            int x = 10 / 0;
                        } catch (ArithmeticException e) {
                            System.out.println("Não dá para dividir por zero!");
                        } finally {
                            System.out.println("Isso roda sempre.");
                        }

                    Comuns: NullPointerException, ArrayIndexOutOfBoundsException,
                    NumberFormatException (Integer.parseInt("abc")), ArithmeticException."""),
                new Licao("Lambdas e Streams", """
                    Lambda é uma função curtinha: parâmetro -> resultado

                        List<Integer> nums = List.of(1, 2, 3, 4, 5, 6);

                        nums.stream()
                            .filter(n -> n % 2 == 0)   // fica só quem é par
                            .map(n -> n * 10)          // multiplica por 10
                            .forEach(System.out::println);   // 20, 40, 60

                    Streams deixam o código mais declarativo: você diz O QUE quer, não COMO.""")),
            List.of(
                Quiz.de("Qual a principal vantagem do ArrayList sobre um array comum?", "O ArrayList cresce e encolhe conforme necessário.",
                        "Tamanho dinâmico", "É sempre mais rápido", "Guarda int direto, sem wrapper", "Não precisa de import"),
                Quiz.de("Qual exceção ocorre em 10 / 0 com inteiros?", "Divisão inteira por zero lança ArithmeticException.",
                        "ArithmeticException", "NullPointerException", "NumberFormatException", "IOException"),
                Quiz.de("Quando o bloco finally executa?", "finally roda ocorrendo erro ou não (ótimo para fechar recursos).",
                        "Sempre, com erro ou sem erro", "Só se ocorrer erro", "Só se não ocorrer erro", "Nunca, é ignorado"),
                Quiz.de("O que significa x -> x * 2 em Java?", "É uma lambda: recebe x e devolve x * 2.",
                        "Uma lambda que recebe x e devolve x * 2", "Um ponteiro para x", "Uma atribuição inválida", "Um comentário"),
                Quiz.de("Qual coleção associa uma chave a um valor?", "HashMap guarda pares chave -> valor.",
                        "HashMap", "ArrayList", "int[]", "Scanner")),
            List.of(
                new Bug("Lista de números", L(
                    "import java.util.ArrayList;",
                    "ArrayList<int> lista = new ArrayList<>();",
                    "lista.add(5);"), 2, "Genéricos não aceitam tipos primitivos. Use ArrayList<Integer>."),
                new Bug("Removendo durante o for-each", L(
                    "List<String> nomes = new ArrayList<>(List.of(\"A\", \"B\", \"C\", \"D\"));",
                    "for (String n : nomes) {",
                    "    if (n.equals(\"B\")) nomes.remove(n);",
                    "}"), 3, "Alterar a lista dentro do for-each causa ConcurrentModificationException. Use nomes.removeIf(...).")),
            List.of(
                new Lacuna("Crie uma lista que cresce sozinha.", L("List<String> nomes = new ____<>();"),
                        List.of("ArrayList", "LinkedList"), "Começa com 'Array'.", "ArrayList é a implementação mais usada de List."),
                new Lacuna("Capture o erro de divisão por zero.", L(
                    "try {", "    int x = 10 / 0;", "} ____ (ArithmeticException e) {",
                    "    System.out.println(\"Erro!\");", "}"), List.of("catch"), "Em inglês: 'pegar'.",
                        "catch captura a exceção lançada no try.")),
            List.of(
                new Saida("Lista", L("List<Integer> l = new ArrayList<>();", "l.add(5);", "l.add(7);",
                        "System.out.println(l.size() + l.get(0));"), "7", "size() é 2 e get(0) é 5: 2 + 5 = 7."),
                new Saida("Mapa", L("Map<String, Integer> m = new HashMap<>();", "m.put(\"a\", 1);", "m.put(\"a\", 2);",
                        "System.out.println(m.get(\"a\") + \" \" + m.size());"), "2 1",
                        "A chave repetida substitui o valor: continua 1 entrada, valendo 2.")));
    }

    // ------------------------------------------------------------------ MISSÕES DE CÓDIGO
    private static Missao.Caso c(String argumentos, String esperado) { return new Missao.Caso(argumentos, esperado); }

    private static List<Missao> missoes() {
        return List.of(
            new Missao(1, 1, "Apresente-se", "Imprima exatamente: NOME tem IDADE anos  (ex.: Ana tem 15 anos)",
                "String nome, int idade", List.of(c("\"Ana\", 15", "Ana tem 15 anos"), c("\"Leo\", 9", "Leo tem 9 anos")),
                null, "Use System.out.println e junte os pedaços com +.",
                "System.out.println(nome + \" tem \" + idade + \" anos\");"),
            new Missao(2, 1, "Área do retângulo", "Imprima a área do retângulo (base vezes altura).",
                "int base, int altura", List.of(c("6, 4", "24"), c("5, 5", "25"), c("10, 3", "30")),
                null, "Área = base * altura.", "System.out.println(base * altura);"),
            new Missao(3, 2, "Aprovado ou não", "Imprima Aprovado se a nota for 7 ou mais; caso contrário, Reprovado.",
                "int nota", List.of(c("8", "Aprovado"), c("7", "Aprovado"), c("4", "Reprovado")),
                null, "Use if / else com a condição nota >= 7.",
                "if (nota >= 7) {\n    System.out.println(\"Aprovado\");\n} else {\n    System.out.println(\"Reprovado\");\n}"),
            new Missao(4, 2, "Par ou ímpar", "Imprima par ou impar (sem acento).",
                "int n", List.of(c("4", "par"), c("7", "impar"), c("0", "par")),
                null, "O resto da divisão por 2 (n % 2) diz tudo.",
                "if (n % 2 == 0) {\n    System.out.println(\"par\");\n} else {\n    System.out.println(\"impar\");\n}"),
            new Missao(5, 3, "Contagem", "Imprima os números de 1 até n, um por linha.",
                "int n", List.of(c("3", "1\n2\n3"), c("5", "1\n2\n3\n4\n5")),
                null, "Um for indo de 1 até n.", "for (int i = 1; i <= n; i++) {\n    System.out.println(i);\n}"),
            new Missao(6, 3, "Soma até n", "Imprima a soma de todos os números de 1 até n.",
                "int n", List.of(c("4", "10"), c("10", "55"), c("1", "1")),
                null, "Crie int soma = 0 e acumule com soma += i dentro de um for.",
                "int soma = 0;\nfor (int i = 1; i <= n; i++) {\n    soma += i;\n}\nSystem.out.println(soma);"),
            new Missao(7, 4, "O maior do array", "Imprima o maior valor do array.",
                "int[] v", List.of(c("new int[]{3, 9, 2}", "9"), c("new int[]{-5, -2, -8}", "-2"), c("new int[]{7}", "7")),
                null, "Comece com v[0] como maior e compare com os demais.",
                "int maior = v[0];\nfor (int x : v) {\n    if (x > maior) maior = x;\n}\nSystem.out.println(maior);"),
            new Missao(8, 4, "Texto ao contrário", "Imprima a String invertida (java vira avaj).",
                "String s", List.of(c("\"java\"", "avaj"), c("\"abc\"", "cba"), c("\"a\"", "a")),
                null, "Percorra de trás para frente com charAt, ou use new StringBuilder(s).reverse().",
                "System.out.println(new StringBuilder(s).reverse());"),
            new Missao(9, 5, "Classe Contador",
                "Declare DENTRO do método uma classe local chamada Contador, com um atributo private int valor,\n"
                + "o método incrementar() e o método getValor(). Crie um Contador, chame incrementar()\n"
                + "'vezes' vezes e imprima getValor().",
                "int vezes", List.of(c("3", "3"), c("0", "0"), c("7", "7")),
                "class Contador", "class Contador { private int valor; void incrementar() { valor++; } int getValor() { return valor; } }",
                "class Contador {\n    private int valor;\n    void incrementar() { valor++; }\n    int getValor() { return valor; }\n}\n"
                + "Contador c = new Contador();\nfor (int i = 0; i < vezes; i++) c.incrementar();\nSystem.out.println(c.getValor());"),
            new Missao(10, 5, "Herança em ação",
                "Declare as classes locais Animal (método String som() que devolve \"...\") e\n"
                + "Gato extends Animal (som() devolve \"Miau\"). Crie Animal a = new Gato() e imprima a.som().",
                "", List.of(c("", "Miau")),
                "extends", "class Animal { String som() { return \"...\"; } }  e  class Gato extends Animal { ... }",
                "class Animal {\n    String som() { return \"...\"; }\n}\nclass Gato extends Animal {\n    @Override\n    String som() { return \"Miau\"; }\n}\n"
                + "Animal a = new Gato();\nSystem.out.println(a.som());"),
            new Missao(11, 6, "Soma dos pares", "Imprima a soma dos números pares da lista.",
                "List<Integer> nums", List.of(c("List.of(1, 2, 3, 4, 5, 6)", "12"), c("List.of(7, 9)", "0"), c("List.of(2, 2)", "4")),
                null, "for-each com if (x % 2 == 0), ou stream().filter(...).mapToInt(x -> x).sum().",
                "int soma = 0;\nfor (int x : nums) {\n    if (x % 2 == 0) soma += x;\n}\nSystem.out.println(soma);"),
            new Missao(12, 6, "Ordem alfabética", "Imprima os nomes em ordem alfabética, um por linha.",
                "List<String> nomes", List.of(c("new ArrayList<>(List.of(\"Zeca\", \"Ana\", \"Bia\"))", "Ana\nBia\nZeca"),
                    c("new ArrayList<>(List.of(\"Leo\", \"Ana\"))", "Ana\nLeo")),
                null, "Collections.sort(nomes) ordena a lista.",
                "Collections.sort(nomes);\nfor (String n : nomes) {\n    System.out.println(n);\n}"),
            new Missao(13, 6, "Captura segura",
                "Converta o texto em número com Integer.parseInt e imprima o DOBRO.\nSe não for um número, capture a exceção e imprima invalido.",
                "String txt", List.of(c("\"21\"", "42"), c("\"abc\"", "invalido"), c("\"-3\"", "-6")),
                "catch", "try { ... } catch (NumberFormatException e) { ... }",
                "try {\n    int n = Integer.parseInt(txt);\n    System.out.println(n * 2);\n} catch (NumberFormatException e) {\n    System.out.println(\"invalido\");\n}"));
    }
}
