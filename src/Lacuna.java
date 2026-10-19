import java.util.List;

/** Complete o código: o jogador digita a palavra que falta no lugar do ____. */
public record Lacuna(String enunciado, List<String> linhas, List<String> aceitas, String dica, String explicacao)
        implements Desafio {

    @Override
    public String tipo() { return "Lacuna"; }

    @Override
    public boolean jogar(Jogador j, boolean rapido) {
        Terminal.linha();
        Terminal.enunciado(enunciado);
        Terminal.mostrarCodigo(linhas);
        boolean usouDica = false;
        String resposta;
        while (true) {
            resposta = Terminal.ler("  O que falta no ____ ? (digite '?' para uma dica): ");
            if (resposta.equals("?") && !usouDica) {
                usouDica = true;
                Terminal.linha(Terminal.cor(Terminal.AZUL, "  Dica: " + dica));
                continue;
            }
            break;
        }
        boolean ok = aceitas.contains(resposta);
        registrar(j, ok);
        if (ok) {
            Terminal.linha(Terminal.cor(Terminal.VERDE, "  Código completo!") + " " + explicacao);
            Recompensa.acerto(j, usouDica ? 8 : 15);
        } else {
            String digitado = resposta;
            boolean quase = aceitas.stream().anyMatch(a -> a.equalsIgnoreCase(digitado));
            if (quase) Terminal.linha(Terminal.cor(Terminal.AMARELO, "  Quase! Java diferencia MAIÚSCULAS de minúsculas."));
            Terminal.linha(Terminal.cor(Terminal.VERMELHO, "  Faltava: ") + aceitas.get(0));
            Terminal.linha("  " + explicacao);
            Recompensa.erro(j);
        }
        return ok;
    }
}
