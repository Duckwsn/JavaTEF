import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Aventura Java: aprenda Java jogando. Ponto de entrada e menu principal. */
public class Main {

    public static void main(String[] args) {
        for (String a : args) {
            if (a.equals("--sem-cor")) Terminal.comCor = false;
            if (a.equals("--rapido")) Terminal.animado = false;
        }
        Terminal.configurarSaida();
        Jogador j = null;
        try {
            j = iniciar();
            menu(j);
        } catch (Terminal.EntradaEncerrada e) {
            System.out.println("\n  Entrada encerrada. Até logo!");
        } finally {
            if (j != null) j.salvar();
        }
    }

    private static void banner() {
        String[] copo = {
            "        ( (        ",
            "         ) )       ",
            "      ........     ",
            "      |      |]    ",
            "      \\      /     ",
            "       `----'      "
        };
        for (String l : copo) {
            Terminal.linha(Terminal.cor(Terminal.CIANO, "  " + l));
            Terminal.pausa(60);
        }
        Terminal.caixa(Terminal.AMARELO, "A V E N T U R A   J A V A", "Aprenda Java jogando: do zero ao intermediário");
    }

    private static Jogador iniciar() {
        banner();
        Jogador salvo = Jogador.carregar();
        if (salvo != null) {
            Terminal.linha("  Progresso encontrado: " + salvo.nome + " (nível " + salvo.nivel() + ", " + salvo.titulo() + ")");
            Terminal.linha("  1) Continuar   2) Novo jogo (apaga o progresso atual)");
            if (Terminal.lerInt("  Escolha: ", 1, 2) == 1) {
                Terminal.linha(Terminal.cor(Terminal.MAGENTA, "\n  Mestre Duke: ") + "Bem-vindo de volta, " + salvo.nome + "!");
                salvo.registrarDia();
                return salvo;
            }
            Jogador.apagar();
        }
        String nome = "";
        while (nome.isBlank()) nome = Terminal.ler("  Como devemos te chamar, aventureiro? ");
        if (nome.length() > 20) nome = nome.substring(0, 20);
        Jogador j = new Jogador(nome);
        Terminal.linha();
        Terminal.digitar(Terminal.cor(Terminal.MAGENTA, "  Mestre Duke: ") + "Olá, " + nome + "! Eu sou o Duke, guardião do Java.", 20);
        Terminal.digitar("  Neste reino você aprende programando: aulas, bugs para caçar, chefes para vencer", 20);
        Terminal.digitar("  e tesouros para ganhar. Comece pela Trilha da Aventura. Boa sorte!", 20);
        Terminal.linha(Terminal.cor(Terminal.AMARELO, "\n  Presente de boas-vindas: 20 moedas e 2 dicas."));
        j.registrarDia();
        return j;
    }

    private static void menu(Jogador j) {
        while (true) {
            j.salvar();
            Terminal.linha();
            mostrarStatus(j);
            int paraRever = paraRevisar(j).size();
            Terminal.linha("   1) Trilha da Aventura     (aulas, treino e chefes)");
            Terminal.linha("   2) Caça-Bugs              (ache o erro no código)");
            Terminal.linha("   3) Complete o Código      (preencha a lacuna)");
            Terminal.linha("   4) Preveja a Saída        (seja o computador)");
            Terminal.linha("   5) Laboratório de Código  (escreva Java de verdade!)");
            Terminal.linha("   6) Arena Relâmpago        (60 segundos de quiz)");
            Terminal.linha("   7) Revisão Inteligente    (refaça o que você errou)" + (paraRever > 0 ? "  [" + paraRever + "]" : ""));
            Terminal.linha("   8) Desafio do Dia         (bônus uma vez por dia)");
            Terminal.linha("   9) Biblioteca de Aulas    (releia qualquer aula)");
            Terminal.linha("  10) Loja");
            Terminal.linha("  11) Perfil e Conquistas");
            Terminal.linha("  12) Opções");
            Terminal.linha("   0) Salvar e sair");
            switch (Terminal.lerInt("  Escolha: ", 0, 12)) {
                case 1 -> ModoTrilha.abrir(j);
                case 2 -> rodadaDe(j, "CAÇA-BUGS", Terminal.VERMELHO, Bug.class);
                case 3 -> rodadaDe(j, "COMPLETE O CÓDIGO", Terminal.VERDE, Lacuna.class);
                case 4 -> rodadaDe(j, "PREVEJA A SAÍDA", Terminal.CIANO, Saida.class);
                case 5 -> ModoLaboratorio.abrir(j);
                case 6 -> {
                    if (j.mundosConcluidos.isEmpty()) {
                        Terminal.linha(Terminal.cor(Terminal.AMARELO, "  A Arena abre depois que você derrotar o chefe do Mundo 1."));
                    } else {
                        ModoRelampago.jogar(j);
                    }
                }
                case 7 -> revisao(j);
                case 8 -> desafioDoDia(j);
                case 9 -> biblioteca(j);
                case 10 -> Loja.abrir(j);
                case 11 -> perfil(j);
                case 12 -> opcoes(j);
                default -> {
                    Terminal.linha("  Progresso salvo. Continue praticando, " + j.nome + "!");
                    return;
                }
            }
        }
    }

    private static List<Desafio> paraRevisar(Jogador j) {
        List<Desafio> pool = new ArrayList<>();
        for (Desafio d : Conteudo.todosDesafios(j)) if (j.errados.contains(d.chave())) pool.add(d);
        return pool;
    }

    private static void revisao(Jogador j) {
        List<Desafio> pool = paraRevisar(j);
        if (pool.isEmpty()) {
            Terminal.linha(Terminal.cor(Terminal.VERDE, "  Nada para revisar! Os desafios que você errar aparecem aqui."));
            return;
        }
        Terminal.linha("  Você tem " + pool.size() + " desafio(s) para revisar. Acertar tira da lista!");
        ModoRodada.jogar(j, "REVISÃO INTELIGENTE", Terminal.MAGENTA, pool);
    }

    private static void biblioteca(Jogador j) {
        while (true) {
            Terminal.linha();
            Terminal.caixa(Terminal.AZUL, "BIBLIOTECA DE AULAS");
            List<Mundo> liberados = Conteudo.liberados(j);
            for (Mundo m : liberados) Terminal.linha("  " + m.id() + ") " + m.nome());
            Terminal.linha("  0) Voltar");
            int esc = Terminal.lerInt("  Escolha: ", 0, liberados.size());
            if (esc == 0) return;
            ModoTrilha.lerLicoes(j, liberados.get(esc - 1));
        }
    }

    private static void mostrarStatus(Jogador j) {
        Terminal.linha(Terminal.cor(Terminal.NEGRITO, "  " + j.nome + " - Nível " + j.nivel() + " (" + j.titulo() + ")"));
        int no = j.xp - j.xpInicioNivel();
        int total = j.xpProximoNivel() - j.xpInicioNivel();
        Terminal.linha("  XP " + Terminal.cor(Terminal.CIANO, Terminal.barra(no, total, 20)) + " " + no + "/" + total
                + "   Moedas: " + j.moedas + "   Sequência: " + j.streak + "d" + "   Combo: " + j.combo);
        Terminal.linha();
    }

    private static <T extends Desafio> void rodadaDe(Jogador j, String titulo, String cor, Class<T> tipo) {
        List<Desafio> pool = new ArrayList<>();
        for (Desafio d : Conteudo.todosDesafios(j)) if (tipo.isInstance(d)) pool.add(d);
        ModoRodada.jogar(j, titulo, cor, pool);
    }

    private static void desafioDoDia(Jogador j) {
        LocalDate hoje = LocalDate.now();
        if (hoje.equals(j.ultimoDesafioDia)) {
            Terminal.linha(Terminal.cor(Terminal.AMARELO, "  Você já fez o desafio de hoje. Volte amanhã!"));
            return;
        }
        List<Desafio> pool = Conteudo.todosDesafios(j);
        Desafio d = pool.get(new Random(hoje.toEpochDay()).nextInt(pool.size()));
        Terminal.linha();
        Terminal.caixa(Terminal.MAGENTA, "DESAFIO DO DIA", "Acerte para ganhar +50 XP e +20 moedas de bônus");
        j.ultimoDesafioDia = hoje;
        if (d.jogar(j, false)) {
            j.moedas += 20;
            Terminal.linha(Terminal.cor(Terminal.MAGENTA, "  Bônus do dia: +20 moedas e +50 XP!"));
            j.ganharXp(50);
        } else {
            Terminal.linha(Terminal.cor(Terminal.AMARELO, "  Amanhã tem outro. Não desista!"));
        }
    }

    private static void perfil(Jogador j) {
        Terminal.linha();
        Terminal.caixa(Terminal.CIANO, "PERFIL: " + j.nome.toUpperCase(),
                "Nível " + j.nivel() + " - " + j.titulo() + "   (XP total: " + j.xp + ")",
                "Acertos: " + j.acertos + "   Erros: " + j.erros,
                "Bugs caçados: " + j.bugsCacados + "   Chefes vencidos: " + j.chefesVencidos,
                "Melhor combo: " + j.melhorCombo + "   Recorde na Arena: " + j.recordeRelampago,
                "Mundos concluídos: " + j.mundosConcluidos.size() + "/" + Conteudo.MUNDOS.size(),
                "Missões de código: " + j.missoesFeitas.size() + "/" + Conteudo.MISSOES.size() + "   Para revisar: " + paraRevisar(j).size());
        Terminal.linha(Terminal.cor(Terminal.NEGRITO, "\n  CONQUISTAS (" + j.conquistas.size() + "/" + Conquista.values().length + ")"));
        for (Conquista c : Conquista.values()) {
            boolean tem = j.conquistas.contains(c.name());
            String linha = (tem ? "  [X] " : "  [ ] ") + c.titulo + " - " + c.descricao;
            Terminal.linha(tem ? Terminal.cor(Terminal.VERDE, linha) : Terminal.cor(Terminal.CINZA, linha));
        }
        Terminal.enter();
    }

    private static void opcoes(Jogador j) {
        while (true) {
            Terminal.linha();
            Terminal.linha("  1) Animações: " + (Terminal.animado ? "ligadas" : "desligadas"));
            Terminal.linha("  2) Cores: " + (Terminal.comCor ? "ligadas" : "desligadas"));
            Terminal.linha("  3) Apagar todo o progresso");
            Terminal.linha("  0) Voltar");
            switch (Terminal.lerInt("  Escolha: ", 0, 3)) {
                case 1 -> Terminal.animado = !Terminal.animado;
                case 2 -> Terminal.comCor = !Terminal.comCor;
                case 3 -> {
                    if (Terminal.ler("  Digite APAGAR para confirmar: ").equals("APAGAR")) {
                        Jogador.apagar();
                        Terminal.linha("  Progresso apagado. Abra o jogo de novo para recomeçar.");
                        System.exit(0);
                    }
                }
                default -> { return; }
            }
        }
    }
}
