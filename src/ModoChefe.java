import java.util.List;

/** Batalha de chefe: cada resposta certa causa dano; cada erro custa um coração. */
public final class ModoChefe {
    private ModoChefe() { }

    private static void desenharChefe() {
        String[] rosto = {
            "        .-\"\"\"\"\"-.        ",
            "       / O     O \\       ",
            "      |    ___    |      ",
            "      |   \\___/   |      ",
            "       \\  /|||\\  /       ",
            "        '-------'        "
        };
        for (String l : rosto) {
            Terminal.linha(Terminal.cor(Terminal.VERMELHO, "  " + l));
            Terminal.pausa(80);
        }
    }

    public static boolean lutar(Jogador j, String chefe, List<Desafio> perguntas, int coracoesIniciais) {
        int hpMax = perguntas.size();
        int hp = hpMax;
        int coracoes = coracoesIniciais;

        Terminal.linha();
        Terminal.caixa(Terminal.VERMELHO, "CHEFE: " + chefe.toUpperCase());
        desenharChefe();
        Terminal.digitar(Terminal.cor(Terminal.VERMELHO, "  " + chefe + ": ") + "Você não passará! Responda se for capaz!", 15);
        Terminal.pausa(500);

        for (Desafio pergunta : perguntas) {
            Terminal.linha();
            Terminal.linha("  " + Terminal.cor(Terminal.VERMELHO, chefe) + " " + Terminal.barra(hp, hpMax, 20) + " " + hp + "/" + hpMax);
            Terminal.linha("  Você " + Terminal.cor(Terminal.VERMELHO, "<3 ".repeat(coracoes))
                    + (j.escudos > 0 ? Terminal.cor(Terminal.AZUL, "(+" + j.escudos + " escudo)") : ""));
            boolean ok = pergunta.jogar(j, false);
            if (ok) {
                hp--;
                Terminal.linha(Terminal.cor(Terminal.VERDE, "  >>> Golpe crítico! " + chefe + " cambaleia..."));
                Terminal.pausa(400);
            } else if (j.escudos > 0) {
                j.escudos--;
                Terminal.linha(Terminal.cor(Terminal.AZUL, "  Seu escudo absorveu o golpe!"));
            } else {
                coracoes--;
                Terminal.linha(Terminal.cor(Terminal.VERMELHO, "  <<< " + chefe + " te acertou! Corações restantes: " + coracoes));
                if (coracoes == 0) {
                    Terminal.linha();
                    Terminal.caixa(Terminal.VERMELHO, "DERROTA", "Estude mais um pouco e volte para a revanche!");
                    return false;
                }
            }
        }
        Terminal.linha();
        Terminal.caixa(Terminal.VERDE, "VITÓRIA!", chefe + " foi derrotado!");
        return true;
    }
}
