public class DobramentoHash implements FuncaoHash{

    public int hash(String chave, int tamanho) {
        int key = Integer.parseInt(chave);
        int parte1 = key / 100_000_000;
        int parte2 = (key / 1000) % 1000;
        int parte3 = key % 1000;
        int soma = parte1 + parte2 + parte3;
        return soma % tamanho;
    }
}
