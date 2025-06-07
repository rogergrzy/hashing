public class HashTableRehash {
    private Registro[] tabela;
    private FuncaoHash funcaoHash;
    private int colisoes;
    private int elementos;

    private static final double LIMIAR_CARGA = 0.75;

    public HashTableRehash(int tamanhoInicial, FuncaoHash funcaoHash) {
        // Para a sondagem quadrática funcionar bem, o tamanho da tabela
        // deve ser um número primo. Vamos encontrar o próximo primo.
        int tamanhoPrimo = proximoPrimo(tamanhoInicial);
        this.tabela = new Registro[tamanhoPrimo];
        this.funcaoHash = funcaoHash;
        this.colisoes = 0;
        this.elementos = 0;
    }

    /**
     * Método de inserção principal.
     * Agora utiliza sondagem quadrática para resolver colisões.
     */
    public void inserir(Registro registro) {
        if ((double) elementos / tabela.length > LIMIAR_CARGA) {
            rehash();
        }

        int i = 0;
        int indice = funcaoHash.hash(registro.getCodigo(), tabela.length);

        // Laço de Sondagem Quadrática
        while (tabela[indice] != null) {
            colisoes++;
            i++;
            indice = (indice + i * i) % tabela.length; // Salto quadrático: +1, +4, +9, ...
        }

        tabela[indice] = registro;
        elementos++;
    }

    /**
     * Método de busca.
     * Também foi atualizado para usar sondagem quadrática.
     */
    public boolean buscar(String codigo) {
        int i = 0;
        int indice = funcaoHash.hash(codigo, tabela.length);

        // Laço de Sondagem Quadrática
        while (tabela[indice] != null) {
            if (tabela[indice].getCodigo().equals(codigo)) {
                return true;
            }
            i++;
            indice = (indice + i * i) % tabela.length;

            // Condição de parada para evitar loop infinito se o elemento não existe
            // e a tabela tem ciclos. O número de tentativas não deve exceder o tamanho da tabela.
            if (i >= tabela.length) {
                break;
            }
        }

        return false;
    }

    /**
     * Inserção usada exclusivamente durante o rehash.
     * Também modificado para sondagem quadrática.
     */
    private void inserirSemRehash(Registro registro) {
        int i = 0;
        int indice = funcaoHash.hash(registro.getCodigo(), tabela.length);

        // Laço de Sondagem Quadrática
        while (tabela[indice] != null) {
            // Não contamos colisões durante o rehash para evitar contagens duplicadas.
            // O ideal é contar apenas uma vez.
            i++;
            indice = (indice + i * i) % tabela.length;
        }

        tabela[indice] = registro;
        elementos++;
    }

    /**
     * Redimensiona a tabela para o dobro do tamanho (aproximadamente).
     * Encontra um novo tamanho primo para garantir a eficiência da sondagem quadrática.
     */
    private void rehash() {
        Registro[] antiga = tabela;
        int novoTamanho = proximoPrimo(tabela.length * 2);

        tabela = new Registro[novoTamanho];
        elementos = 0;
        // As colisões são resetadas, pois a nova tabela terá um comportamento diferente.
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

    // --- MÉTODOS AUXILIARES PARA SONDAGEM QUADRÁTICA ---

    /**
     * Verifica se um número é primo.
     */
    private boolean ehPrimo(int n) {
        if (n <= 1) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    /**
     * Encontra o próximo número primo a partir de um número dado.
     */
    private int proximoPrimo(int n) {
        if (n % 2 == 0) {
            n++;
        }
        while (!ehPrimo(n)) {
            n += 2;
        }
        return n;
    }
}