/** Qualquer coisa que o jogador possa "jogar": quiz, bug, lacuna ou previsão de saída. */
public interface Desafio {
    String tipo();

    /**
     * Apresenta o desafio, lê a resposta e devolve true se acertou.
     * No modo rápido (Arena Relâmpago) não há recompensa aqui nem explicação: quem chamou cuida disso.
     */
    boolean jogar(Jogador j, boolean rapido);

    /** Identificador estável, usado pela Revisão Inteligente. */
    default String chave() { return tipo() + ":" + Integer.toHexString(toString().hashCode()); }

    /** Marca o desafio como "para revisar" ao errar e tira da lista ao acertar. */
    default void registrar(Jogador j, boolean acertou) {
        if (acertou) j.errados.remove(chave());
        else j.errados.add(chave());
    }
}
