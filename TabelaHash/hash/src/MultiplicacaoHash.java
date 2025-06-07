public class MultiplicacaoHash implements FuncaoHash {
    public int hash(String chave, int tamanho) {
        double key = Double.parseDouble(chave);
        double A = (Math.sqrt(5) - 1) / 2;  // Proporção Áurea
        double frac = (key * A) % 1;
        return (int) (tamanho * frac);
    }
}
