import java.util.List;

/** Preveja a saída: o jogador age como o computador e diz o que o código imprime. */
public record Saida(String titulo, List<String> linhas, String resposta, String explicacao) implements Desafio {

    @Override
    public String tipo() { return "Saída"; }

    private static String normalizar(String s) { return s.replaceAll("\\s+", ""); }

    @Override
    public boolean jogar(Jogador j, boolean rapido) {
        Terminal.linha();
        Terminal.linha(Terminal.cor(Terminal.NEGRITO, "  SIMULADOR: " + titulo));
        Terminal.mostrarCodigo(linhas);
        String r = Terminal.ler("  O que aparece na tela? (várias linhas: separe com espaço): ");
        boolean ok = normalizar(r).equals(normalizar(resposta));
        registrar(j, ok);
        if (ok) {
            Terminal.linha(Terminal.cor(Terminal.VERDE, "  Você pensou como o computador!") + " " + explicacao);
            Recompensa.acerto(j, 15);
        } else {
            Terminal.linha(Terminal.cor(Terminal.VERMELHO, "  Saída real: ") + resposta);
            Terminal.linha("  " + explicacao);
            Recompensa.erro(j);
        }
        return ok;
    }
}
