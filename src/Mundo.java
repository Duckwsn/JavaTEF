import java.util.ArrayList;
import java.util.List;

/** Um mundo da trilha: história, aulas, desafios e um chefe. */
public record Mundo(int id, String nome, String chefe, String historia, List<Licao> licoes,
                    List<Quiz> quizzes, List<Bug> bugs, List<Lacuna> lacunas, List<Saida> saidas) {

    public List<Desafio> todos() {
        List<Desafio> lista = new ArrayList<>();
        lista.addAll(quizzes);
        lista.addAll(bugs);
        lista.addAll(lacunas);
        lista.addAll(saidas);
        return lista;
    }
}
