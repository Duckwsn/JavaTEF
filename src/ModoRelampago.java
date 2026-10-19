import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Modo 5: Arena Relâmpago. 60 segundos, quantas perguntas você acerta? */
public final class ModoRelampago {
    private static final int DURACAO_MS = 60_000;

    private ModoRelampago() { }

    public static void jogar(Jogador j) {
        List<Quiz> pool = new ArrayList<>();
        for (Mundo m : Conteudo.liberados(j)) pool.addAll(m.quizzes());

        Terminal.linha();
        Terminal.caixa(Terminal.AMARELO, "ARENA RELÂMPAGO",
                "Você tem 60 segundos. Responda o máximo que puder!",
                "Sem dicas e sem explicações. Resposta dada depois do tempo não vale.",
                "Seu recorde: " + j.recordeRelampago + " acertos");
        Terminal.ler("  Pressione Enter quando estiver pronto... ");
        for (String n : new String[] {"3", "2", "1"}) {
            Terminal.linha(Terminal.cor(Terminal.AMARELO, "  " + n + "..."));
            Terminal.pausa(700);
        }
        Terminal.linha(Terminal.cor(Terminal.VERDE, "  JÁ!"));

        long fim = System.currentTimeMillis() + DURACAO_MS;
        List<Quiz> fila = new ArrayList<>();
        int pontos = 0;
        int sequencia = 0;
        while (System.currentTimeMillis() < fim) {
            if (fila.isEmpty()) {
                fila.addAll(pool);
                Collections.shuffle(fila);
            }
            Quiz q = fila.remove(fila.size() - 1);
            long restante = Math.max(0, (fim - System.currentTimeMillis()) / 1000);
            Terminal.linha(Terminal.cor(Terminal.AMARELO, "\n  Tempo: " + restante + "s   Acertos: " + pontos + "   Sequência: " + sequencia));
            boolean ok = q.jogar(j, true);
            if (System.currentTimeMillis() > fim) {
                Terminal.linha(Terminal.cor(Terminal.AMARELO, "  Tempo esgotado! Essa resposta não conta."));
                break;
            }
            if (ok) {
                pontos++;
                sequencia++;
            } else {
                sequencia = 0;
            }
        }

        int xp = pontos * 6;
        int moedas = pontos * 2;
        j.acertos += pontos;
        j.moedas += moedas;
        Terminal.linha();
        Terminal.caixa(Terminal.AMARELO, "TEMPO!", "Acertos: " + pontos, "Recompensa: +" + xp + " XP e +" + moedas + " moedas");
        if (pontos > j.recordeRelampago) {
            j.recordeRelampago = pontos;
            if (pontos > 0) Terminal.linha(Terminal.cor(Terminal.MAGENTA, "  NOVO RECORDE!"));
        }
        if (pontos >= 10) j.conquistar(Conquista.RELAMPAGO_10);
        j.ganharXp(xp);
        j.verificarConquistas();
    }
}
