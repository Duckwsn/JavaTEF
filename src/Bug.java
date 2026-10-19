import java.util.List;

/** Caça-Bug: o jogador aponta a linha do código que tem o erro. */
public record Bug(String titulo, List<String> linhas, int linhaBug, String explicacao) implements Desafio {

    @Override
    public String tipo() { return "Bug"; }

    @Override
    public boolean jogar(Jogador j, boolean rapido) {
        Terminal.linha();
        Terminal.linha(Terminal.cor(Terminal.NEGRITO, "  BUG DETECTADO: " + titulo));
        Terminal.mostrarCodigo(linhas);
        int escolha = Terminal.lerInt("  Qual linha tem o bug? ", 1, linhas.size());
        boolean ok = escolha == linhaBug;
        registrar(j, ok);
        if (ok) {
            j.bugsCacados++;
            Terminal.linha(Terminal.cor(Terminal.VERDE, "  Bug esmagado!") + " " + explicacao);
            Recompensa.acerto(j, 15);
        } else {
            Terminal.linha(Terminal.cor(Terminal.VERMELHO, "  Errou! O bug estava na linha " + linhaBug + ":"));
            Terminal.linha(Terminal.cor(Terminal.CIANO, "     " + linhas.get(linhaBug - 1).strip()));
            Terminal.linha("  " + explicacao);
            Recompensa.erro(j);
        }
        return ok;
    }
}
