import java.util.Random;

/** Regras de XP, moedas e combo. Tudo que o jogador ganha (ou perde) passa por aqui. */
public final class Recompensa {
    private static final Random SORTE = new Random();
    private static final String[] ELOGIOS = {
        "Boa!", "Isso aí!", "Compilou de primeira!", "Duke aprova!", "Mandou bem!", "Perfeito!", "Código limpo!"
    };

    private Recompensa() { }

    public static void acerto(Jogador j, int xpBase) {
        j.combo++;
        j.acertos++;
        j.melhorCombo = Math.max(j.melhorCombo, j.combo);
        int mult = 1 + Math.min(j.combo - 1, 9) / 3;   // combo 1-3: x1 | 4-6: x2 | 7-9: x3 | 10+: x4
        int xp = xpBase * mult;
        boolean pocao = j.xpDobro > 0;
        if (pocao) {
            xp *= 2;
            j.xpDobro--;
        }
        int moedas = 1 + mult;
        j.moedas += moedas;
        String extra = (mult > 1 ? "  COMBO x" + mult : "") + (pocao ? "  POÇÃO x2" : "");
        Terminal.linha(Terminal.cor(Terminal.VERDE, "  " + ELOGIOS[SORTE.nextInt(ELOGIOS.length)]
                + "  +" + xp + " XP  +" + moedas + " moedas" + extra));
        j.ganharXp(xp);
        j.verificarConquistas();
    }

    public static void erro(Jogador j) {
        if (j.combo >= 3) Terminal.linha(Terminal.cor(Terminal.AMARELO, "  Combo de " + j.combo + " perdido!"));
        j.combo = 0;
        j.erros++;
        j.ganharXp(1); // errar também ensina: 1 XP de consolo
    }

    /** 25% de chance de achar um baú depois de uma rodada. */
    public static void bau(Jogador j) {
        if (SORTE.nextInt(100) < 25) {
            int moedas = 10 + SORTE.nextInt(31);
            j.moedas += moedas;
            Terminal.linha();
            Terminal.caixa(Terminal.AMARELO, "BAÚ DO TESOURO!", "Você achou um baú com " + moedas + " moedas!");
        }
    }
}
