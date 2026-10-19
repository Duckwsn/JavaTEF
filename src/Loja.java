/** Onde as moedas viram vantagem. */
public final class Loja {
    private static final int PRECO_DICA = 30;
    private static final int PRECO_ESCUDO = 50;
    private static final int PRECO_POCAO = 80;

    private Loja() { }

    public static void abrir(Jogador j) {
        while (true) {
            Terminal.linha();
            Terminal.caixa(Terminal.AMARELO, "LOJA DO DUKE", "Suas moedas: " + j.moedas);
            Terminal.linha("  1) Dica (" + PRECO_DICA + ")    - remove 2 alternativas erradas de um quiz. Você tem: " + j.dicas);
            Terminal.linha("  2) Escudo (" + PRECO_ESCUDO + ")  - absorve um erro em batalha de chefe. Você tem: " + j.escudos);
            Terminal.linha("  3) Poção de XP (" + PRECO_POCAO + ") - XP em dobro nos próximos 5 acertos. Ativa: " + j.xpDobro);
            Terminal.linha("  0) Sair");
            int esc = Terminal.lerInt("  Escolha: ", 0, 3);
            if (esc == 0) return;
            int preco = esc == 1 ? PRECO_DICA : esc == 2 ? PRECO_ESCUDO : PRECO_POCAO;
            if (j.moedas < preco) {
                Terminal.linha(Terminal.cor(Terminal.VERMELHO, "  Moedas insuficientes."));
                continue;
            }
            j.moedas -= preco;
            switch (esc) {
                case 1 -> j.dicas++;
                case 2 -> j.escudos++;
                default -> j.xpDobro += 5;
            }
            Terminal.linha(Terminal.cor(Terminal.VERDE, "  Compra feita!"));
        }
    }
}
