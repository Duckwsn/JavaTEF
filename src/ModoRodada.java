import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Modos 2, 3 e 4: rodadas de 5 desafios sorteados (Caça-Bugs, Complete o Código, Preveja a Saída). */
public final class ModoRodada {
    private ModoRodada() { }

    public static void jogar(Jogador j, String titulo, String cor, List<? extends Desafio> pool) {
        while (true) {
            List<Desafio> sorteio = new ArrayList<>(pool);
            Collections.shuffle(sorteio);
            int total = Math.min(5, sorteio.size());
            Terminal.linha();
            Terminal.caixa(cor, titulo, "Rodada de " + total + " desafios", "Combo atual: x" + (1 + Math.min(Math.max(j.combo, 1) - 1, 9) / 3));
            int acertos = 0;
            for (int i = 0; i < total; i++) {
                Terminal.linha(Terminal.cor(Terminal.CINZA, "\n  --- Desafio " + (i + 1) + "/" + total + " ---"));
                if (sorteio.get(i).jogar(j, false)) acertos++;
            }
            Terminal.linha();
            Terminal.caixa(Terminal.AMARELO, "FIM DA RODADA", "Acertos: " + acertos + "/" + total);
            if (acertos == total) {
                Terminal.linha(Terminal.cor(Terminal.VERDE, "  Rodada perfeita! +30 XP"));
                j.ganharXp(30);
                j.conquistar(Conquista.PERFEITO);
            }
            Recompensa.bau(j);
            j.verificarConquistas();
            if (!Terminal.confirmar("Jogar outra rodada?")) return;
        }
    }
}
