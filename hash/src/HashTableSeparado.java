import java.util.*;

public class HashTableSeparado {
    private List<Registro>[] tabela;
    private FuncaoHash funcaoHash;
    private int colisoes;

    public HashTableSeparado(int tamanho, FuncaoHash funcaoHash) {
        this.tabela = new List[tamanho];
        this.funcaoHash = funcaoHash;
        this.colisoes = 0;

        for (int i = 0; i < tamanho; i++) {
            tabela[i] = new LinkedList<>();
        }
    }

    public void inserir(Registro registro) {
        int indice = funcaoHash.hash(registro.getCodigo(), tabela.length);
        List<Registro> lista = tabela[indice];

        if (!lista.isEmpty()) {
            colisoes++;
        }

        lista.add(registro);
    }

    public boolean buscar(String codigo) {
        int indice = funcaoHash.hash(codigo, tabela.length);
        List<Registro> lista = tabela[indice];

        for (Registro r : lista) {
            if (r.getCodigo().equals(codigo)) {
                return true;
            }
        }
        return false;
    }

    public int getColisoes() {
        return colisoes;
    }
}
