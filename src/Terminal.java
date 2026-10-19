import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Tudo que mexe com a tela e o teclado: cores, animações e leitura de dados. */
public final class Terminal {
    public static final String RESET = "\u001B[0m", NEGRITO = "\u001B[1m", VERMELHO = "\u001B[31m",
            VERDE = "\u001B[32m", AMARELO = "\u001B[33m", AZUL = "\u001B[34m", MAGENTA = "\u001B[35m",
            CIANO = "\u001B[36m", CINZA = "\u001B[90m";

    private static final Scanner ENTRADA = new Scanner(System.in, StandardCharsets.UTF_8);
    public static boolean comCor = true;
    public static boolean animado = true;

    /** Lançada quando o teclado fecha (Ctrl+D / fim da entrada). */
    public static class EntradaEncerrada extends RuntimeException { }

    private Terminal() { }

    public static void configurarSaida() {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));
    }

    public static String cor(String codigo, String texto) {
        return comCor ? codigo + texto + RESET : texto;
    }

    public static void linha() { System.out.println(); }

    public static void linha(String texto) { System.out.println(texto); }

    public static void pausa(int ms) {
        if (!animado) return;
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** Efeito de máquina de escrever. */
    public static void digitar(String texto, int msPorLetra) {
        if (!animado) {
            System.out.println(texto);
            return;
        }
        for (char c : texto.toCharArray()) {
            System.out.print(c);
            System.out.flush();
            pausa(msPorLetra);
        }
        System.out.println();
    }

    public static void limpar() {
        if (comCor && animado) System.out.print("\u001B[2J\u001B[H");
        else System.out.println("\n".repeat(2));
    }

    public static String ler(String prompt) {
        System.out.print(prompt);
        System.out.flush();
        try {
            return ENTRADA.nextLine().trim();
        } catch (NoSuchElementException e) {
            throw new EntradaEncerrada();
        }
    }

    public static int lerInt(String prompt, int min, int max) {
        while (true) {
            String s = ler(prompt);
            try {
                int v = Integer.parseInt(s);
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException ignorado) {
                // cai na mensagem abaixo
            }
            linha(cor(AMARELO, "  Digite um número entre " + min + " e " + max + "."));
        }
    }

    public static void enter() {
        ler(cor(CINZA, "  [Enter para continuar] "));
    }

    public static boolean confirmar(String pergunta) {
        String r = ler("  " + pergunta + " (s/n): ");
        return r.equalsIgnoreCase("s") || r.equalsIgnoreCase("sim");
    }

    public static String barra(int atual, int max, int largura) {
        int cheio = max <= 0 ? 0 : Math.min(largura, Math.max(0, atual) * largura / max);
        return "[" + "#".repeat(cheio) + "-".repeat(largura - cheio) + "]";
    }

    public static void caixa(String corBorda, String titulo, String... linhas) {
        int largura = titulo.length();
        for (String l : linhas) largura = Math.max(largura, l.length());
        largura += 4;
        String borda = "+" + "-".repeat(largura) + "+";
        linha(cor(corBorda, borda));
        int sobra = largura - titulo.length();
        int esq = sobra / 2;
        linha(cor(corBorda, "|") + cor(NEGRITO, " ".repeat(esq) + titulo + " ".repeat(sobra - esq)) + cor(corBorda, "|"));
        if (linhas.length > 0) {
            linha(cor(corBorda, borda));
            for (String l : linhas) {
                linha(cor(corBorda, "|") + "  " + l + " ".repeat(largura - 2 - l.length()) + cor(corBorda, "|"));
            }
        }
        linha(cor(corBorda, borda));
    }

    /** Mostra texto de aula: linhas começando com 4 espaços são tratadas como código. */
    public static void mostrarTexto(String texto) {
        for (String l : texto.replaceAll("^\\n+", "").stripTrailing().split("\n")) {
            if (l.startsWith("    ")) linha(cor(CIANO, "  " + l));
            else linha("  " + l);
        }
    }

    public static void mostrarCodigo(List<String> linhas) {
        int i = 1;
        for (String l : linhas) {
            linha(cor(CINZA, String.format("  %2d | ", i++)) + cor(CIANO, l));
        }
    }

    /** Primeira linha em destaque com "?", resto como texto/código. */
    public static void enunciado(String texto) {
        String[] partes = texto.strip().split("\n", 2);
        linha(cor(NEGRITO, "  ? " + partes[0]));
        if (partes.length > 1) mostrarTexto(partes[1]);
    }

    public static void carregando(String mensagem) {
        System.out.print("  " + mensagem);
        for (int i = 0; i < 3; i++) {
            pausa(250);
            System.out.print(".");
            System.out.flush();
        }
        System.out.println(" pronto!");
    }
}
