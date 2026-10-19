import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/** Guarda todo o progresso: XP, nível, moedas, itens, conquistas... e salva em disco. */
public class Jogador {
    private static final String[] TITULOS = {
        "Curioso dos Bytes", "Aprendiz de Sintaxe", "Caçador de Bugs", "Artesão de Métodos", "Mago dos Laços",
        "Cavaleiro das Classes", "Arquiteto de Objetos", "Mestre das Coleções", "Lenda do Java", "Duke Supremo"
    };
    private static final Path PASTA = Path.of(System.getProperty("user.home"), ".aventura-java");
    private static final Path ARQUIVO = PASTA.resolve("save.properties");

    String nome;
    int xp, moedas, combo, melhorCombo, streak, acertos, erros, bugsCacados, chefesVencidos, recordeRelampago;
    int dicas, escudos, xpDobro;
    LocalDate ultimoDia, ultimoDesafioDia;
    final Set<Integer> mundosConcluidos = new TreeSet<>();
    final Set<Integer> licoesLidas = new TreeSet<>();
    final Set<String> conquistas = new TreeSet<>();
    final Set<String> errados = new TreeSet<>();
    final Set<Integer> missoesFeitas = new TreeSet<>();

    public Jogador(String nome) {
        this.nome = nome;
        this.moedas = 20;
        this.dicas = 2;
    }

    // ---------- nível e XP ----------
    public int nivel() { return 1 + (int) Math.sqrt(xp / 100.0); }

    public int xpInicioNivel() { int n = nivel() - 1; return 100 * n * n; }

    public int xpProximoNivel() { int n = nivel(); return 100 * n * n; }

    public String titulo() { return TITULOS[Math.min(TITULOS.length - 1, nivel() - 1)]; }

    public void ganharXp(int qtd) {
        int antes = nivel();
        xp += qtd;
        if (nivel() > antes) {
            int bonus = 25 * (nivel() - antes);
            moedas += bonus;
            Terminal.linha();
            Terminal.caixa(Terminal.AMARELO, "*** LEVEL UP! ***",
                    "Você chegou ao nível " + nivel() + ": " + titulo(),
                    "Bônus: +" + bonus + " moedas");
            Terminal.pausa(700);
        }
    }

    // ---------- mundos ----------
    public boolean desbloqueou(int mundoId) {
        return mundoId <= 1 || mundosConcluidos.contains(mundoId - 1);
    }

    // ---------- conquistas ----------
    public void conquistar(Conquista c) {
        if (conquistas.add(c.name())) {
            moedas += 30;
            Terminal.linha();
            Terminal.caixa(Terminal.MAGENTA, "CONQUISTA DESBLOQUEADA!", c.titulo + " - " + c.descricao, "+30 moedas");
            Terminal.pausa(500);
        }
    }

    public void verificarConquistas() {
        if (acertos >= 1) conquistar(Conquista.PRIMEIRO_ACERTO);
        if (melhorCombo >= 5) conquistar(Conquista.COMBO_5);
        if (melhorCombo >= 10) conquistar(Conquista.COMBO_10);
        if (bugsCacados >= 10) conquistar(Conquista.CACADOR);
        if (chefesVencidos >= 1) conquistar(Conquista.CHEFE_1);
        if (mundosConcluidos.size() >= 3) conquistar(Conquista.MUNDO_3);
        if (mundosConcluidos.size() >= Conteudo.MUNDOS.size()) conquistar(Conquista.TODOS_MUNDOS);
        if (moedas >= 200) conquistar(Conquista.RICO);
        if (nivel() >= 5) conquistar(Conquista.NIVEL_5);
        if (streak >= 3) conquistar(Conquista.STREAK_3);
        if (missoesFeitas.size() >= 1) conquistar(Conquista.PROGRAMADOR);
        if (missoesFeitas.size() >= 5) conquistar(Conquista.ESCRITOR);
    }

    // ---------- sequência diária ----------
    public void registrarDia() {
        LocalDate hoje = LocalDate.now();
        if (hoje.equals(ultimoDia)) return;
        if (ultimoDia != null && ultimoDia.plusDays(1).equals(hoje)) streak++;
        else streak = 1;
        ultimoDia = hoje;
        int bonus = 10 + 5 * Math.min(streak, 7);
        moedas += bonus;
        Terminal.linha(Terminal.cor(Terminal.AMARELO,
                "  Bônus diário: +" + bonus + " moedas (sequência de " + streak + " dia(s))"));
        verificarConquistas();
    }

    // ---------- salvar / carregar ----------
    public void salvar() {
        Properties p = new Properties();
        p.setProperty("nome", nome);
        p.setProperty("xp", String.valueOf(xp));
        p.setProperty("moedas", String.valueOf(moedas));
        p.setProperty("melhorCombo", String.valueOf(melhorCombo));
        p.setProperty("streak", String.valueOf(streak));
        p.setProperty("acertos", String.valueOf(acertos));
        p.setProperty("erros", String.valueOf(erros));
        p.setProperty("bugs", String.valueOf(bugsCacados));
        p.setProperty("chefes", String.valueOf(chefesVencidos));
        p.setProperty("recorde", String.valueOf(recordeRelampago));
        p.setProperty("dicas", String.valueOf(dicas));
        p.setProperty("escudos", String.valueOf(escudos));
        p.setProperty("xpDobro", String.valueOf(xpDobro));
        if (ultimoDia != null) p.setProperty("ultimoDia", ultimoDia.toString());
        if (ultimoDesafioDia != null) p.setProperty("ultimoDesafioDia", ultimoDesafioDia.toString());
        p.setProperty("mundos", juntar(mundosConcluidos));
        p.setProperty("licoes", juntar(licoesLidas));
        p.setProperty("conquistas", String.join(",", conquistas));
        p.setProperty("errados", String.join(",", errados));
        p.setProperty("missoes", juntar(missoesFeitas));
        try {
            Files.createDirectories(PASTA);
            try (var saida = Files.newOutputStream(ARQUIVO)) {
                p.store(saida, "Aventura Java - progresso");
            }
        } catch (IOException e) {
            System.err.println("Aviso: não consegui salvar o progresso (" + e.getMessage() + ")");
        }
    }

    public static Jogador carregar() {
        if (!Files.exists(ARQUIVO)) return null;
        Properties p = new Properties();
        try (var entrada = Files.newInputStream(ARQUIVO)) {
            p.load(entrada);
            Jogador j = new Jogador(p.getProperty("nome", "Aventureiro"));
            j.xp = inteiro(p, "xp", 0);
            j.moedas = inteiro(p, "moedas", 0);
            j.melhorCombo = inteiro(p, "melhorCombo", 0);
            j.streak = inteiro(p, "streak", 0);
            j.acertos = inteiro(p, "acertos", 0);
            j.erros = inteiro(p, "erros", 0);
            j.bugsCacados = inteiro(p, "bugs", 0);
            j.chefesVencidos = inteiro(p, "chefes", 0);
            j.recordeRelampago = inteiro(p, "recorde", 0);
            j.dicas = inteiro(p, "dicas", 0);
            j.escudos = inteiro(p, "escudos", 0);
            j.xpDobro = inteiro(p, "xpDobro", 0);
            if (p.getProperty("ultimoDia") != null) j.ultimoDia = LocalDate.parse(p.getProperty("ultimoDia"));
            if (p.getProperty("ultimoDesafioDia") != null)
                j.ultimoDesafioDia = LocalDate.parse(p.getProperty("ultimoDesafioDia"));
            for (String s : p.getProperty("mundos", "").split(",")) if (!s.isBlank()) j.mundosConcluidos.add(Integer.parseInt(s.trim()));
            for (String s : p.getProperty("licoes", "").split(",")) if (!s.isBlank()) j.licoesLidas.add(Integer.parseInt(s.trim()));
            for (String s : p.getProperty("conquistas", "").split(",")) if (!s.isBlank()) j.conquistas.add(s.trim());
            for (String s : p.getProperty("errados", "").split(",")) if (!s.isBlank()) j.errados.add(s.trim());
            for (String s : p.getProperty("missoes", "").split(",")) if (!s.isBlank()) j.missoesFeitas.add(Integer.parseInt(s.trim()));
            return j;
        } catch (IOException | RuntimeException e) {
            return null; // save corrompido: começa do zero
        }
    }

    public static void apagar() {
        try {
            Files.deleteIfExists(ARQUIVO);
        } catch (IOException ignorado) {
            // sem problema
        }
    }

    private static int inteiro(Properties p, String chave, int padrao) {
        return Integer.parseInt(p.getProperty(chave, String.valueOf(padrao)));
    }

    private static String juntar(Set<Integer> numeros) {
        StringBuilder sb = new StringBuilder();
        for (int n : numeros) sb.append(sb.length() == 0 ? "" : ",").append(n);
        return sb.toString();
    }
}
