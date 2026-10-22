# Aventura Java

Jogo de terminal que ensina Java do zero ao intermediário. Requer **JDK 17+**.

Comando para JDK 17+: **winget install EclipseAdoptium.Temurin.21.JDK**

## Como rodar
- Linux/macOS: `./run.sh`
- Windows: `run.bat` (use Windows Terminal para as cores)
- Opções: `--sem-cor` (sem ANSI) e `--rapido` (sem animações)

## Modos de atividade
1. **Trilha da Aventura**: 6 mundos com aulas, treino e chefe + chefe final
2. **Caça-Bugs**: aponte a linha com o erro
3. **Complete o Código**: digite a palavra que falta
4. **Preveja a Saída**: seja o computador
5. **Laboratório de Código**: 13 missões em que você escreve Java de verdade; o jogo compila,
   roda em outro processo e testa com vários casos (precisa de JDK, não só JRE)
6. **Arena Relâmpago**: 60 segundos de quiz (abre após o Mundo 1)
7. **Revisão Inteligente**: refaz só os desafios que você errou; acertar tira da lista
8. **Desafio do Dia**: bônus diário
9. **Biblioteca de Aulas**: releia qualquer aula já liberada

## Gamificação
XP e níveis, moedas, combo (até x4), sequência diária, baú aleatório, loja (dica, escudo, poção de XP),
15 conquistas, batalhas de chefe com corações. O progresso é salvo em `~/.aventura-java/save.properties`.

## Mundos
1. Variáveis e tipos  2. if/else/switch  3. Laços  4. Métodos, arrays e Strings
5. Orientação a Objetos  6. Coleções, exceções, lambdas e streams

## Para expandir
Todo o conteúdo está em `src/Conteudo.java`: adicione `Quiz`, `Bug`, `Lacuna` ou `Saida` aos mundos.
