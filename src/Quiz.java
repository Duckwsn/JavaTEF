import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Pergunta de múltipla escolha. As alternativas são embaralhadas a cada vez. */
public record Quiz(String enunciado, String certa, List<String> erradas, String explicacao) implements Desafio {

    public static Quiz de(String enunciado, String explicacao, String certa, String... erradas) {
        return new Quiz(enunciado, certa, Arrays.asList(erradas), explicacao);
    }

    @Override
    public String tipo() { return "Quiz"; }

    @Override
    public boolean jogar(Jogador j, boolean rapido) {
        List<String> opcoes = new ArrayList<>(erradas);
        opcoes.add(certa);
        Collections.shuffle(opcoes);
        boolean dicaUsada = false;

        Terminal.linha();
        Terminal.enunciado(enunciado);
        int escolha = -1;
        while (escolha < 0) {
            for (int i = 0; i < opcoes.size(); i++) {
                Terminal.linha("     " + (i + 1) + ") " + opcoes.get(i));
            }
            boolean podeDica = !rapido && j.dicas > 0 && !dicaUsada && opcoes.size() > 2;
            String r = Terminal.ler("  Resposta" + (podeDica ? " (ou 'd' para dica, você tem " + j.dicas + ")" : "") + ": ");
            if (podeDica && r.equalsIgnoreCase("d")) {
                List<String> alvos = new ArrayList<>(opcoes);
                alvos.remove(certa);
                Collections.shuffle(alvos);
                opcoes.remove(alvos.get(0));
                if (alvos.size() >= 3) opcoes.remove(alvos.get(1));
                j.dicas--;
                dicaUsada = true;
                Terminal.linha(Terminal.cor(Terminal.AZUL, "  Dica usada: alternativas erradas removidas!"));
                continue;
            }
            try {
                int n = Integer.parseInt(r);
                if (n >= 1 && n <= opcoes.size()) escolha = n - 1;
            } catch (NumberFormatException ignorado) {
                // pergunta de novo
            }
        }
        boolean ok = opcoes.get(escolha).equals(certa);
        registrar(j, ok);
        if (rapido) {
            Terminal.linha(ok ? Terminal.cor(Terminal.VERDE, "  Certo!") : Terminal.cor(Terminal.VERMELHO, "  Errado! Era: " + certa));
            return ok;
        }
        if (ok) {
            Terminal.linha(Terminal.cor(Terminal.VERDE, "  Correto!") + " " + explicacao);
            Recompensa.acerto(j, dicaUsada ? 7 : 10);
        } else {
            Terminal.linha(Terminal.cor(Terminal.VERMELHO, "  Não foi dessa vez.") + " Resposta: " + certa);
            Terminal.linha("  " + explicacao);
            Recompensa.erro(j);
        }
        return ok;
    }
}
