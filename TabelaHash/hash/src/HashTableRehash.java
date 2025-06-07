public class HashTableRehash {
    private Registro[] tabela;
    private FuncaoHash funcaoHash;
    private int colisoes;
    private int elementos;

    private static final double LIMIAR_CARGA = 0.75;

    public HashTableRehash(int tamanhoInicial, FuncaoHash funcaoHash) {
        this.tabela = new Registro[tamanhoInicial];
        this.funcaoHash = funcaoHash;
        this.colisoes = 0;
        this.elementos = 0;
    }

    public void inserir(Registro registro) {
        if ((double) elementos / tabela.length > LIMIAR_CARGA) {
            rehash();
        }

        int indice = funcaoHash.hash(registro.getCodigo(), tabela.length);
        int original = indice;

        while (tabela[indice] != null) {
            colisoes++;
            indice = (indice + 1) % tabela.length;
            if (indice == original) {
                // Tabela cheia (loop completo) — isso não deve ocorrer se o rehash estiver funcionando
                return;
            }
        }

        tabela[indice] = registro;
        elementos++;
    }

    public boolean buscar(String codigo) {
        int indice = funcaoHash.hash(codigo, tabela.length);
        int original = indice;

        while (tabela[indice] != null) {
            if (tabela[indice].getCodigo().equals(codigo)) {
                return true;
            }
            indice = (indice + 1) % tabela.length;
            if (indice == original) {
                break;
            }
        }

        return false;
    }

    private void inserirSemRehash(Registro registro) {
        int indice = funcaoHash.hash(registro.getCodigo(), tabela.length);
        int original = indice;
        int tentativas = 0;  // contador de tentativas

        while (tabela[indice] != null) {
            colisoes++;
            indice = (indice + 1) % tabela.length;
            tentativas++;

            if (tentativas >= tabela.length) {  // já percorreu toda a tabela, aborta inserção
                System.err.println("Tabela cheia! Não foi possível inserir o registro: " + registro.getCodigo());
                return;
            }
        }

        tabela[indice] = registro;
        elementos++;
    }

    private void rehash() {
        Registro[] antiga = tabela;
        int novoTamanho = tabela.length * 2;
        tabela = new Registro[novoTamanho];
        elementos = 0;
        colisoes = 0;

        for (Registro r : antiga) {
            if (r != null) {
                inserirSemRehash(r);
            }
        }
    }

    public int getColisoes() {
        return colisoes;
    }
}
