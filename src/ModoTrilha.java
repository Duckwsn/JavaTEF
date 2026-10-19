import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Modo 1: Trilha da Aventura. Aulas, treino e chefe em cada mundo. */
public final class ModoTrilha {
    private ModoTrilha() { }

    public static void abrir(Jogador j) {
        while (true) {
            Terminal.linha();
            Terminal.caixa(Terminal.AMARELO, "TRILHA DA AVENTURA");
            for (Mundo m : Conteudo.MUNDOS) {
                String estado = j.mundosConcluidos.contains(m.id()) ? Terminal.cor(Terminal.VERDE, "[CONCLUÍDO]")
                        : j.desbloqueou(m.id()) ? Terminal.cor(Terminal.AMARELO, "[LIBERADO]")
                        : Terminal.cor(Terminal.CINZA, "[BLOQUEADO]");
                Terminal.linha("  " + m.id() + ") " + m.nome() + "  " + estado);
            }
            int opcaoFinal = Conteudo.MUNDOS.size() + 1;
            boolean completo = j.mundosConcluidos.size() >= Conteudo.MUNDOS.size();
            Terminal.linha("  " + opcaoFinal + ") O Compilador Sombrio (CHEFE FINAL)  "
                    + (j.conquistas.contains(Conquista.FINAL.name()) ? Terminal.cor(Terminal.VERDE, "[CONCLUÍDO]")
                    : completo ? Terminal.cor(Terminal.AMARELO, "[LIBERADO]") : Terminal.cor(Terminal.CINZA, "[BLOQUEADO]")));
            Terminal.linha("  0) Voltar");
            int esc = Terminal.lerInt("  Escolha: ", 0, opcaoFinal);
            if (esc == 0) return;
            if (esc == opcaoFinal) {
                if (completo) chefeFinal(j);
                else Terminal.linha(Terminal.cor(Terminal.AMARELO, "  Conclua todos os mundos primeiro!"));
            } else if (!j.desbloqueou(esc)) {
                Terminal.linha(Terminal.cor(Terminal.AMARELO, "  Derrote o chefe do mundo " + (esc - 1) + " para liberar este."));
            } else {
                entrarMundo(j, Conteudo.MUNDOS.get(esc - 1));
            }
        }
    }

    private static void entrarMundo(Jogador j, Mundo m) {
        Terminal.linha();
        Terminal.caixa(Terminal.CIANO, "MUNDO " + m.id() + ": " + m.nome().toUpperCase());
        if (!j.licoesLidas.contains(m.id())) {
            Terminal.digitar(Terminal.cor(Terminal.MAGENTA, "  Mestre Duke: ") + m.historia(), 12);
        }
        while (true) {
            Terminal.linha();
            Terminal.linha("  1) Ler as lições" + (j.licoesLidas.contains(m.id()) ? "" : Terminal.cor(Terminal.AMARELO, "  (+30 XP na primeira vez)")));
            Terminal.linha("  2) Treino (" + m.quizzes().size() + " perguntas)");
            Terminal.linha("  3) Enfrentar o chefe: " + m.chefe());
            Terminal.linha("  0) Voltar");
            switch (Terminal.lerInt("  Escolha: ", 0, 3)) {
                case 1 -> lerLicoes(j, m);
                case 2 -> treinar(j, m);
                case 3 -> chefe(j, m);
                default -> { return; }
            }
        }
    }

    static void lerLicoes(Jogador j, Mundo m) {
        List<Licao> paginas = m.licoes();
        for (int i = 0; i < paginas.size(); i++) {
            Terminal.linha();
            Terminal.caixa(Terminal.AZUL, "AULA " + (i + 1) + "/" + paginas.size() + ": " + paginas.get(i).titulo());
            Terminal.mostrarTexto(paginas.get(i).texto());
            Terminal.linha();
            Terminal.enter();
        }
        if (j.licoesLidas.add(m.id())) {
            j.moedas += 10;
            Terminal.linha(Terminal.cor(Terminal.VERDE, "  Aulas concluídas! +30 XP e +10 moedas"));
            j.ganharXp(30);
        }
    }

    private static void treinar(Jogador j, Mundo m) {
        List<Quiz> perguntas = new ArrayList<>(m.quizzes());
        Collections.shuffle(perguntas);
        int acertos = 0;
        for (Quiz q : perguntas) if (q.jogar(j, false)) acertos++;
        Terminal.linha();
        Terminal.caixa(Terminal.AMARELO, "RESULTADO DO TREINO", "Acertos: " + acertos + "/" + perguntas.size());
        if (acertos == perguntas.size()) {
            Terminal.linha(Terminal.cor(Terminal.VERDE, "  Treino perfeito! +40 XP"));
            j.ganharXp(40);
            j.conquistar(Conquista.PERFEITO);
        }
        Recompensa.bau(j);
    }

    private static void chefe(Jogador j, Mundo m) {
        List<Desafio> perguntas = new ArrayList<>(m.todos());
        Collections.shuffle(perguntas);
        perguntas = new ArrayList<>(perguntas.subList(0, Math.min(5, perguntas.size())));
        if (ModoChefe.lutar(j, m.chefe(), perguntas, 3)) {
            boolean primeira = j.mundosConcluidos.add(m.id());
            j.chefesVencidos++;
            j.moedas += primeira ? 60 : 15;
            Terminal.linha(Terminal.cor(Terminal.VERDE, "  Recompensa: +" + (primeira ? 60 : 15) + " moedas e +" + (primeira ? 100 : 30) + " XP"));
            j.ganharXp(primeira ? 100 : 30);
            if (primeira && m.id() < Conteudo.MUNDOS.size()) {
                Terminal.linha(Terminal.cor(Terminal.AMARELO, "  Novo mundo liberado: " + Conteudo.MUNDOS.get(m.id()).nome() + "!"));
            }
            j.verificarConquistas();
        }
    }

    private static void chefeFinal(Jogador j) {
        Terminal.digitar(Terminal.cor(Terminal.MAGENTA, "  Mestre Duke: ")
                + "É o fim da jornada! O Compilador Sombrio vai testar TUDO que você aprendeu...", 15);
        List<Desafio> todos = new ArrayList<>();
        for (Mundo m : Conteudo.MUNDOS) todos.addAll(m.todos());
        Collections.shuffle(todos);
        List<Desafio> perguntas = new ArrayList<>(todos.subList(0, 8));
        if (ModoChefe.lutar(j, "O Compilador Sombrio", perguntas, 4)) {
            j.chefesVencidos++;
            j.moedas += 150;
            Terminal.linha(Terminal.cor(Terminal.VERDE, "  Recompensa: +150 moedas e +300 XP"));
            j.ganharXp(300);
            j.conquistar(Conquista.FINAL);
            Terminal.caixa(Terminal.AMARELO, "PARABÉNS, " + j.nome.toUpperCase() + "!",
                    "Você concluiu a jornada do Java básico ao intermediário.",
                    "Próximos passos: Maven/Gradle, JavaFX ou Spring Boot, testes com JUnit.");
        }
    }
}
