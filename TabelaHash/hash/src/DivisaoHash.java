// Implementação Função Hash Resto da Divisão
// Resto da divisão:
// h(k) = k mod m

public class DivisaoHash implements FuncaoHash {
    public int hash(String chave, int tamanho) {
        return Math.abs(Integer.parseInt(chave) % tamanho);
    }
}
