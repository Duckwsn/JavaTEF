import javax.tools.ToolProvider;

/** Modo 5: Laboratório de Código. Escreva Java de verdade e veja o compilador testar. */
public final class ModoLaboratorio {
    private ModoLaboratorio() { }

    public static void abrir(Jogador j) {
        if (ToolProvider.getSystemJavaCompiler() == null) {
            Terminal.linha(Terminal.cor(Terminal.AMARELO,
                    "  O Laboratório precisa de um JDK (com compilador). Você está usando apenas um JRE."));
            return;
        }
        while (true) {
            Terminal.linha();
            Terminal.caixa(Terminal.CIANO, "LABORATÓRIO DE CÓDIGO",
                    "Aqui o Java é de verdade: seu código é compilado e testado!");
            java.util.List<Missao> liberadas = new java.util.ArrayList<>();
            for (Missao m : Conteudo.MISSOES) if (j.desbloqueou(m.mundo())) liberadas.add(m);
            for (int i = 0; i < liberadas.size(); i++) {
                Missao m = liberadas.get(i);
                Terminal.linha("  " + (i + 1) + ") [Mundo " + m.mundo() + "] " + m.titulo()
                        + (j.missoesFeitas.contains(m.id()) ? Terminal.cor(Terminal.VERDE, "  [FEITA]") : ""));
            }
            Terminal.linha("  0) Voltar");
            int esc = Terminal.lerInt("  Escolha a missão: ", 0, liberadas.size());
            if (esc == 0) return;
            liberadas.get(esc - 1).jogar(j, false);
        }
    }
}
