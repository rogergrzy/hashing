import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {
        int[] tamanhosTabela = {1000, 10000, 100000};
        String[] arquivos = {"registros_1M.txt", "registros_5M.txt", "registros_20M.txt"};
        long seed = 12345L;

        FuncaoHash[] funcoes = {
                new DivisaoHash(),
                new MultiplicacaoHash(),
                new DobramentoHash()
        };

        String[] nomesFuncoes = {
                "Divisao",
                "Multiplicacao",
                "Dobramento"
        };

        BufferedWriter tempoWriter = new BufferedWriter(new FileWriter("tempos.csv"));
        BufferedWriter colisoesWriter = new BufferedWriter(new FileWriter("colisoes.csv"));

        tempoWriter.write("Tabela,Funcao,Conjunto,TempoInsercao(ms),TempoBusca(ms)\n");
        colisoesWriter.write("Tabela,Funcao,Conjunto,Colisoes\n");

        // === EXECUTA TABELA SEPARADA PARA TODOS OS PARÂMETROS PRIMEIRO ===
        for (String arquivo : arquivos) {
            List<String> registros = lerRegistros(arquivo);

            for (int tamanho : tamanhosTabela) {
                for (int i = 0; i < funcoes.length; i++) {
                    FuncaoHash funcao = funcoes[i];
                    String nomeFuncao = nomesFuncoes[i];

                    HashTableSeparado separado = new HashTableSeparado(tamanho, funcao);

                    long inicioInsercao = System.currentTimeMillis();
                    for (String codigo : registros) {
                        Registro r = new Registro(codigo);
                        separado.inserir(r);
                    }
                    long fimInsercao = System.currentTimeMillis();
                    long tempoInsercao = fimInsercao - inicioInsercao;

                    System.out.println("Separado - Inserção concluída em: " + tempoInsercao + "ms");
                    System.out.println("Total de colisões: " + separado.getColisoes());

                    Random random = new Random(seed);
                    System.out.println("Separado - Iniciando buscas de teste:");
                    long inicioBusca = System.currentTimeMillis();

                    for (int j = 0; j < 5; j++) {
                        String codigoBusca = registros.get(random.nextInt(registros.size()));
                        boolean encontrado = separado.buscar(codigoBusca);
                        System.out.println("Busca por " + codigoBusca + ": " + (encontrado ? "Encontrado" : "Não Encontrado"));
                    }

                    long fimBusca = System.currentTimeMillis();
                    long tempoBusca = fimBusca - inicioBusca;

                    System.out.println("Separado - Buscas concluídas em: " + tempoBusca + " ms");

                    int colisoes = separado.getColisoes();

                    // Escreve resultados no CSV
                    tempoWriter.write(tamanho + "," + nomeFuncao + "," + arquivo + "," + tempoInsercao + "," + tempoBusca + "\n");
                    tempoWriter.flush();

                    colisoesWriter.write(tamanho + "," + nomeFuncao + "," + arquivo + "," + colisoes + "\n");
                    colisoesWriter.flush();

                    System.out.println(">>> Resultados gravados para: " + nomeFuncao + " - " + arquivo + " - Tamanho: " + tamanho);
                }
            }
        }

        // === EXECUTA REHASH PARA TODOS OS PARÂMETROS DEPOIS ===
        for (String arquivo : arquivos) {
            List<String> registros = lerRegistros(arquivo);

            for (int tamanho : tamanhosTabela) {
                for (int i = 0; i < funcoes.length; i++) {
                    FuncaoHash funcao = funcoes[i];
                    String nomeFuncao = nomesFuncoes[i];

                    HashTableRehash rehash = new HashTableRehash(tamanho, funcao);

                    long inicioInsercaoRehash = System.currentTimeMillis();
                    for (String codigo : registros) {
                        Registro r = new Registro(codigo);
                        rehash.inserir(r);
                    }
                    long fimInsercaoRehash = System.currentTimeMillis();
                    long tempoInsercaoRehash = fimInsercaoRehash - inicioInsercaoRehash;

                    System.out.println("Rehash - Inserção concluída em: " + tempoInsercaoRehash + "ms");
                    System.out.println("Total de colisões (rehash): " + rehash.getColisoes());

                    Random randomRehash = new Random(seed);
                    System.out.println("Rehash - Iniciando buscas de teste:");
                    long inicioBuscaRehash = System.currentTimeMillis();

                    for (int j = 0; j < 5; j++) {
                        String codigoBusca = registros.get(randomRehash.nextInt(registros.size()));
                        boolean encontrado = rehash.buscar(codigoBusca);
                        System.out.println("Busca por " + codigoBusca + ": " + (encontrado ? "Encontrado" : "Não Encontrado"));
                    }

                    long fimBuscaRehash = System.currentTimeMillis();
                    long tempoBuscaRehash = fimBuscaRehash - inicioBuscaRehash;

                    System.out.println("Rehash - Buscas concluídas em: " + tempoBuscaRehash + " ms");

                    int colisoesRehash = rehash.getColisoes();

                    // Escreve resultados no CSV
                    tempoWriter.write(tamanho + "," + nomeFuncao + "_Rehash," + arquivo + "," + tempoInsercaoRehash + "," + tempoBuscaRehash + "\n");
                    tempoWriter.flush();

                    colisoesWriter.write(tamanho + "," + nomeFuncao + "_Rehash," + arquivo + "," + colisoesRehash + "\n");
                    colisoesWriter.flush();

                    System.out.println(">>> Resultados REHASH gravados para: " + nomeFuncao + " - " + arquivo + " - Tamanho: " + tamanho);
                }
            }
        }

        tempoWriter.close();
        colisoesWriter.close();
    }

    private static List<String> lerRegistros(String arquivo) throws IOException {
        List<String> registros = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(arquivo));
        String linha;
        while ((linha = reader.readLine()) != null) {
            registros.add(linha.trim());
        }
        reader.close();
        return registros;
    }
}
