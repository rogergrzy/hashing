import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GeradorRegistros {
    public static void main(String[] args) {
        gerarRegistros("registros_1M.txt", 1_000_000, 12345L);
        gerarRegistros("registros_5M.txt", 5_000_000, 12345L);
        gerarRegistros("registros_20M.txt", 20_000_000, 12345L);
    }

    public static void gerarRegistros(String nomeArquivo, int quantidade, long seed) {
        Random random = new Random(seed);
        try {
            FileWriter writer = new FileWriter(nomeArquivo);
            for (int i = 0; i < quantidade; i++) {
                int numero = 100_000_000 + random.nextInt(900_000_000);
                writer.write(String.format("%09d", numero) + "\n");
            }
            writer.close();
            System.out.println("Arquivo " + nomeArquivo + " gerado com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao gerar o arquivo: " + e.getMessage());
        }
    }
}
