# Análise de Desempenho de Tabelas Hash em Java

## 1. Visão Geral do Projeto

Este projeto realiza uma análise empírica do desempenho de diferentes implementações de Tabelas Hash (Hashing) em Java. O objetivo é avaliar como a escolha da **Função de Hash**, do **Tamanho da Tabela** e da **Estratégia de Tratamento de Colisão** impacta a eficiência em operações de inserção e busca.

O estudo foi conduzido testando-se as implementações com grandes volumes de dados (1, 5 e 20 milhões de registros) gerados aleatoriamente. As principais métricas de desempenho analisadas foram o **número de colisões** e o **tempo de execução** para inserção e busca.

## 2. Detalhes da Implementação

A estrutura do projeto foi desenvolvida de forma modular para permitir a fácil substituição e teste dos diferentes componentes.

### Funções de Hash

Três funções de hash foram implementadas, cada uma com uma abordagem distinta para mapear chaves a índices da tabela:

* **Resto da Divisão (`DivisaoHash.java`)**: Calcula o hash com a operação `h(k) = k mod m`. É uma função simples e rápida, mas sensível à escolha do tamanho `m`.
* **Multiplicação (`MultiplicacaoHash.java`)**: Utiliza o método da multiplicação com a proporção áurea, calculado por `h(k) = floor(m * (k * A mod 1))`. Tende a funcionar bem com qualquer tamanho de tabela.
* **Dobramento (`DobramentoHash.java`)**: A chave (um número de 9 dígitos) é dividida em três partes, que são somadas para compor o hash final. O objetivo é usar todas as partes da chave na geração do índice.

### Estratégias de Tratamento de Colisão

Duas estratégias clássicas para lidar com colisões foram implementadas:

* **Endereçamento Separado (`HashTableSeparado.java`)**: Cada posição da tabela aponta para uma lista (`LinkedList`) que armazena todos os registros que colidiram naquele mesmo índice.
* **Endereçamento Aberto com Sondagem e Rehash (`HashTableRehash.java`)**: Quando uma colisão ocorre, o algoritmo procura pelo próximo espaço livre na tabela. Para evitar o agrupamento primário e degradação de performance, foi utilizada a **sondagem quadrática**. Adicionalmente, quando a tabela atinge um fator de carga de 75%, um `rehash` é acionado: a tabela é redimensionada para o próximo número primo que seja o dobro do tamanho anterior, e todos os elementos são reinseridos.

## 3. Como Executar o Projeto

1.  **Gerar os Dados**: Execute a classe `GeradorRegistros.java` para criar os arquivos de dados (`registros_1M.txt`, `registros_5M.txt`, `registros_20M.txt`).
2.  **Executar os Testes**: Execute a classe `Main.java`. O programa irá rodar todas as combinações de testes e gerar dois arquivos de resultado:
    * `colisoes.csv`: Contém o número total de colisões para cada teste.
    * `tempos.csv`: Contém os tempos de inserção e busca (em milissegundos) para cada teste.

## 4. Análise dos Resultados

Os resultados coletados nos arquivos CSV permitem uma análise comparativa do desempenho das abordagens.

### Análise de Colisões

Os dados de colisões foram extraídos do arquivo `colisoes.csv`. A tabela abaixo mostra um resumo dos resultados para o conjunto de 1 milhão de registros.

| Estratégia            | Função          | Tamanho Inicial | Colisões     |
| --------------------- | --------------- | --------------- | :----------- |
| Encadeamento Separado | Divisão         | 100.000         | 900.005      |
| Encadeamento Separado | Multiplicação   | 100.000         | 900.005      |
| Encadeamento Separado | Dobramento      | 10.000          | 997.998      |
| **Rehash Quadrático** | **Divisão** | **1.000** | **477.768** |
| **Rehash Quadrático** | **Multiplicação** | **1.000** | **480.071** |

*(Fonte: `colisoes.csv`)*

**Observações:**

* **Impacto do Fator de Carga**: No encadeamento separado com tabela de 100.000 para 1 milhão de itens, o fator de carga é 10. Isso significa que, em média, cada lista encadeada teria 10 elementos, resultando em um alto número de colisões (mais de 900.000).
* **Eficácia do Rehash**: A abordagem de **Rehash foi drasticamente mais eficaz** na gestão de colisões. Mesmo começando com uma tabela 100 vezes menor (tamanho 1.000), o número final de colisões foi quase 50% menor. Isso ocorre porque o `rehash` mantém o fator de carga baixo, garantindo que a tabela nunca fique "congestionada".

### Análise de Tempo de Execução

*(Esta seção deve ser preenchida com os dados do seu arquivo `tempos.csv`)*

**Instruções:**

1.  Crie uma tabela similar à de colisões usando os dados de `tempos.csv`.
2.  Gere gráficos de barras para comparar visualmente os tempos de inserção e busca.
3.  Analise os resultados.

**Pontos esperados na análise de tempo:**

* **Tempo de Inserção**: A inserção com `Rehash` pode ser pontualmente mais lenta, pois a operação de `rehash` (redimensionar e reinserir todos os elementos) consome tempo. No entanto, o tempo médio de inserção tende a ser excelente. A inserção em encadeamento separado é consistentemente rápida, pois apenas adiciona um elemento ao final de uma lista.
* **Tempo de Busca**: **Aqui a abordagem com `Rehash` e sondagem quadrática deve ser a grande vencedora**. Como ela mantém o número de colisões baixo e evita o agrupamento, o número de saltos para encontrar um elemento é mínimo. No encadeamento separado, a busca pode se degradar para $$O(n)$$ no pior caso (se muitos elementos caírem no mesmo índice), exigindo uma varredura na lista encadeada.

## 5. Conclusão

Com base na análise de colisões e no desempenho esperado de busca, a implementação de **Endereçamento Aberto com Sondagem Quadrática e Rehash (`HashTableRehash`) é superior** para este caso de uso.

Apesar de a operação de `rehash` introduzir uma complexidade adicional, seu benefício é claro: ao manter o fator de carga da tabela baixo, ela garante um número reduzido de colisões e, consequentemente, um tempo de busca próximo de $$O(1)$$, que é o objetivo principal de uma Tabela Hash.

O projeto demonstra empiricamente que a escolha de uma boa estratégia de tratamento de colisão é, muitas vezes, mais impactante para o desempenho final do que a escolha da função de hash em si.
