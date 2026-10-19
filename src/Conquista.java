/** Medalhas que o jogador desbloqueia ao longo da aventura. */
public enum Conquista {
    PRIMEIRO_ACERTO("Primeiro Passo", "Acerte seu primeiro desafio"),
    COMBO_5("Em Chamas", "Faça um combo de 5 acertos seguidos"),
    COMBO_10("Imparável", "Faça um combo de 10 acertos seguidos"),
    CACADOR("Caçador de Bugs", "Encontre 10 bugs"),
    CHEFE_1("Matador de Chefes", "Derrote seu primeiro chefe"),
    MUNDO_3("Explorador", "Conclua 3 mundos"),
    TODOS_MUNDOS("Mestre Java", "Conclua todos os mundos"),
    FINAL("Lenda do Java", "Derrote o Compilador Sombrio"),
    RELAMPAGO_10("Velocista", "Acerte 10 na Arena Relâmpago"),
    RICO("Tesouro", "Junte 200 moedas"),
    NIVEL_5("Veterano", "Chegue ao nível 5"),
    STREAK_3("Persistente", "Jogue 3 dias seguidos"),
    PERFEITO("Sem Erros", "Complete uma rodada perfeita"),
    PROGRAMADOR("Programador de Verdade", "Resolva sua primeira missão de código"),
    ESCRITOR("Mão na Massa", "Resolva 5 missões de código");

    public final String titulo;
    public final String descricao;

    Conquista(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }
}
