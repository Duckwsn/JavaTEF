import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * Missão de código: o jogador escreve o corpo de um método em Java DE VERDADE.
 * O jogo compila o código (javax.tools), roda em outro processo e compara a saída com vários casos de teste.
 */
public record Missao(int id, int mundo, String titulo, String enunciado, String assinatura, List<Caso> casos,
                     String exige, String dica, String exemplo) implements Desafio {

    public record Caso(String argumentos, String esperado) { }

    private static final int LINHAS_CABECALHO = 4;   // linhas geradas antes do código do jogador

    @Override
    public String tipo() { return "Missão"; }

    @Override
    public boolean jogar(Jogador j, boolean rapido) {
        Terminal.linha();
        Terminal.caixa(Terminal.CIANO, "MISSÃO DE CÓDIGO: " + titulo);
        Terminal.mostrarTexto(enunciado);
        Terminal.linha();
        Terminal.linha(Terminal.cor(Terminal.CINZA, "  Você vai escrever o CORPO deste método:"));
        Terminal.mostrarTexto("    static void executar(" + assinatura + ") {\n        // seu código aqui\n    }");
        Terminal.linha(Terminal.cor(Terminal.CINZA, "  Digite o código linha a linha. Comandos: FIM = testar | DICA | SAIR = desistir"));
        while (true) {
            List<String> codigo = new ArrayList<>();
            while (true) {
                String l = Terminal.ler("  > ");
                if (l.equalsIgnoreCase("FIM")) break;
                if (l.equalsIgnoreCase("SAIR")) return false;
                if (l.equalsIgnoreCase("DICA")) {
                    Terminal.linha(Terminal.cor(Terminal.AZUL, "  Dica: " + dica));
                    continue;
                }
                codigo.add(l);
            }
            if (codigo.isEmpty()) {
                Terminal.linha(Terminal.cor(Terminal.AMARELO, "  Você não digitou nada ainda."));
                continue;
            }
            if (exige != null && !String.join("\n", codigo).contains(exige)) {
                Terminal.linha(Terminal.cor(Terminal.AMARELO, "  Sua solução precisa usar: " + exige));
                continue;
            }
            Terminal.carregando("Compilando e testando");
            if (testar(codigo)) {
                boolean primeira = j.missoesFeitas.add(id);
                Terminal.linha(Terminal.cor(Terminal.VERDE, "  Todos os testes passaram! Você programou Java de verdade."));
                if (primeira) j.moedas += 15;
                Recompensa.acerto(j, primeira ? 40 : 10);
                return true;
            }
            if (!Terminal.confirmar("Tentar de novo?")) {
                Terminal.linha("  Uma solução possível:");
                Terminal.mostrarCodigo(List.of(exemplo.split("\n")));
                Recompensa.erro(j);
                return false;
            }
            Terminal.linha(Terminal.cor(Terminal.CINZA, "  Digite o código de novo (FIM para testar):"));
        }
    }

    private boolean testar(List<String> codigo) {
        JavaCompiler compilador = ToolProvider.getSystemJavaCompiler();
        Path dir = null;
        try {
            dir = Files.createTempDirectory("aventura-java-");
            StringBuilder src = new StringBuilder();
            src.append("import java.util.*;\nimport java.util.stream.*;\npublic class Codigo {\n");
            src.append("    static void executar(").append(assinatura).append(") {\n");
            for (String l : codigo) src.append(l).append('\n');
            src.append("    }\n    public static void main(String[] args) {\n");
            for (Caso c : casos) {
                src.append("        executar(").append(c.argumentos()).append(");\n");
                src.append("        System.out.println(\"@@@\");\n");
            }
            src.append("    }\n}\n");
            Path arquivo = dir.resolve("Codigo.java");
            Files.writeString(arquivo, src.toString(), StandardCharsets.UTF_8);

            DiagnosticCollector<JavaFileObject> diag = new DiagnosticCollector<>();
            boolean compilou;
            try (StandardJavaFileManager fm = compilador.getStandardFileManager(diag, null, StandardCharsets.UTF_8)) {
                compilou = compilador.getTask(null, fm, diag,
                        List.of("-d", dir.toString(), "-classpath", dir.toString(), "-proc:none"),
                        null, fm.getJavaFileObjects(arquivo)).call();
            }
            if (!compilou) {
                int mostrados = 0;
                Terminal.linha(Terminal.cor(Terminal.VERMELHO, "  Não compilou! O compilador reclamou de:"));
                for (Diagnostic<? extends JavaFileObject> d : diag.getDiagnostics()) {
                    if (d.getKind() != Diagnostic.Kind.ERROR || mostrados++ >= 3) continue;
                    long linha = d.getLineNumber() - LINHAS_CABECALHO;
                    String msg = d.getMessage(Locale.ENGLISH).split("\n")[0];
                    Terminal.linha("   - " + (linha >= 1 && linha <= codigo.size() ? "linha " + linha + " do seu código: " : "") + msg);
                }
                return false;
            }
            return executarEComparar(dir);
        } catch (IOException | InterruptedException e) {
            Terminal.linha(Terminal.cor(Terminal.VERMELHO, "  Erro ao preparar o teste: " + e.getMessage()));
            return false;
        } finally {
            if (dir != null) apagar(dir);
        }
    }

    private boolean executarEComparar(Path dir) throws IOException, InterruptedException {
        String java = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        Process p = new ProcessBuilder(java, "-Dfile.encoding=UTF-8", "-Xmx64m", "-cp", dir.toString(), "Codigo")
                .redirectErrorStream(true).start();
        StringBuilder saida = new StringBuilder();
        Thread leitor = new Thread(() -> {
            try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))) {
                int c;
                while ((c = r.read()) != -1) if (saida.length() < 20_000) saida.append((char) c);
            } catch (IOException ignorado) {
                // processo encerrado
            }
        });
        leitor.start();
        if (!p.waitFor(5, TimeUnit.SECONDS)) {
            p.destroyForcibly();
            Terminal.linha(Terminal.cor(Terminal.VERMELHO, "  Tempo esgotado (5s). Será que há um laço infinito?"));
            return false;
        }
        leitor.join(1000);
        String texto = saida.toString();
        int erro = texto.indexOf("Exception in thread");
        if (erro >= 0) {
            Terminal.linha(Terminal.cor(Terminal.VERMELHO, "  Seu código lançou um erro em tempo de execução:"));
            Terminal.linha("   - " + texto.substring(erro).split("\n")[0]);
            return false;
        }
        String[] partes = texto.split("@@@", -1);
        boolean tudoOk = true;
        for (int i = 0; i < casos.size(); i++) {
            Caso c = casos.get(i);
            String obtido = i < partes.length ? partes[i] : "";
            boolean ok = normalizar(obtido).equals(normalizar(c.esperado()));
            String chamada = "executar(" + c.argumentos() + ")";
            if (ok) {
                Terminal.linha(Terminal.cor(Terminal.VERDE, "  [OK] ") + "Caso " + (i + 1) + ": " + chamada);
            } else {
                tudoOk = false;
                Terminal.linha(Terminal.cor(Terminal.VERMELHO, "  [X]  ") + "Caso " + (i + 1) + ": " + chamada);
                Terminal.linha("       esperado: " + resumir(c.esperado()) + "   obtido: " + resumir(obtido));
            }
        }
        return tudoOk;
    }

    private static String normalizar(String s) { return s.trim().replaceAll("\\s+", " ").replace(',', '.'); }

    private static String resumir(String s) {
        String t = s.trim().replace("\n", " / ");
        if (t.isEmpty()) return "(nada)";
        return t.length() > 60 ? t.substring(0, 57) + "..." : t;
    }

    private static void apagar(Path dir) {
        try (Stream<Path> arvore = Files.walk(dir)) {
            arvore.sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
        } catch (IOException ignorado) {
            // lixo temporário: tudo bem
        }
    }
}
