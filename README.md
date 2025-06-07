# Análise de Desempenho de Tabelas Hash

Este projeto implementa e analisa o desempenho de diferentes estruturas de Tabela Hash (Hashing) em Java, avaliando o impacto de distintas funções de hash, tamanhos de tabela e estratégias de tratamento de colisão.

## 1. Implementação

O projeto foi desenvolvido em Java e explora as seguintes configurações:

* **Funções de Hash**:
    * `DivisaoHash`: `h(k) = k mod m`
    * `MultiplicacaoHash`: `h(k) = floor(m * (k * A mod 1))` onde `A` é a Razão Áurea.
    * `DobramentoHash`: A chave é dividida em partes, que são somadas para gerar o hash.
* **Tratamento de Colisão**:
    * **Encadeamento Separado (`HashTableSeparado`)**: Utiliza uma lista encadeada para agrupar registros que colidem no mesmo índice.
    * **Endereçamento Aberto com Rehash (`HashTableRehash`)**: Utiliza sondagem linear para encontrar o próximo índice livre e aplica `rehash` (dobrando o tamanho da tabela) quando o fator de carga ultrapassa 75%.
* **Conjuntos de Dados**:
    * Foram gerados três arquivos de texto (`registros_1M.txt`, `registros_5M.txt`, `registros_20M.txt`) com números aleatórios de 9 dígitos, utilizando uma `seed` fixa (`12345L`) para garantir a reprodutibilidade dos testes.

## 2. Resultados e Análise

Os testes foram executados combinando cada função de hash com diferentes tamanhos de tabela (1.000, 10.000, 100.000) e os três conjuntos de dados. Medimos o número de colisões e os tempos de inserção e busca.

### Análise de Colisões

A colisão ocorre quando duas chaves diferentes são mapeadas para o mesmo índice na tabela.

**Tabela de Colisões (Encadeamento Separado)**

| Tamanho Tabela | Função        | Conjunto de Dados | Colisões     |
| :------------- | :------------ | :---------------- | :----------- |
| 1.000          | Divisão       | registros_1M.txt  | 999.000      |
| 1.000          | Multiplicação | registros_1M.txt  | 999.000      |
| 10.000         | Dobramento    | registros_1M.txt  | 997.998      |
| 100.000        | Divisão       | registros_5M.txt  | 4.900.000    |
| 100.000        | Multiplicação | registros_5M.txt  | 4.900.000    |
| 100.000        | Dobramento    | registros_20M.txt | 19.997.993   |

*(**Instrução para você**: Crie um gráfico de barras aqui para comparar visualmente as colisões. Você pode usar o Excel, Google Sheets ou uma biblioteca em Python como Matplotlib para gerar a imagem e adicioná-la aqui).*

**Observações sobre Colisões:**

* **Impacto do Tamanho da Tabela**: Como esperado, quanto menor a tabela, maior o número de colisões. Com uma tabela de tamanho 1.000 para 1 milhão de registros, quase todos os registros (999.000) resultaram em colisão, pois havia muito mais chaves do que posições disponíveis.
* **Função de Dobramento**: A função de dobramento (`DobramentoHash`) apresentou um número de colisões ligeiramente diferente e, em alguns casos, menor que as outras duas. Isso ocorre porque ela distribui as chaves de maneira diferente, somando partes da chave em vez de usar apenas o resto da divisão ou a multiplicação.

**Tabela de Colisões (Com Rehash)**

| Tamanho Inicial | Função                 | Conjunto de Dados | Colisões |
| :-------------- | :--------------------- | :---------------- | :------- |
| 1.000           | Divisao_Rehash         | registros_1M.txt  | 477.768  |
| 1.000           | Multiplicacao_Rehash   | registros_1M.txt  | 480.071  |

*(**Instrução para você**: Crie outro gráfico comparando as colisões da abordagem com Rehash vs. Encadeamento Separado).*

**Observações sobre o Rehash:**

* A estratégia de `Rehash` **reduziu drasticamente** o número de colisões em comparação com o encadeamento separado para um mesmo tamanho inicial de tabela. Por exemplo, para 1 milhão de registros com a função de Divisão, o número de colisões caiu de 999.000 para 477.768.
* Isso acontece porque, ao atingir o limiar de carga, a tabela é redimensionada, o que melhora a distribuição das chaves e abre mais "espaços vazios", diminuindo a probabilidade de colisões futuras.

### Análise de Tempo de Execução

*(**Instrução para você**: Aqui você deve criar tabelas e gráficos similares aos de colisão, mas usando os dados do seu arquivo `tempos.csv`. Analise o tempo de inserção e o tempo de busca separadamente).*

**Exemplo de Análise de Tempo de Inserção:**

* Qual estratégia foi mais rápida para inserir: Encadeamento Separado ou Rehash? Por quê? (Dica: o Rehash pode ser mais lento durante a inserção que causa o redimensionamento, mas mais rápido no geral).
* Como o tamanho da tabela influenciou o tempo de inserção?

**Exemplo de Análise de Tempo de Busca:**

* Qual estratégia ofereceu buscas mais rápidas? (Dica: tabelas com menos colisões geralmente resultam em buscas mais rápidas, pois há menos elementos para percorrer em uma lista ou menos saltos para fazer na sondagem linear).
* Houve diferença significativa no tempo de busca entre as três funções de hash?

## 3. Conclusão

Com base nos resultados, podemos concluir que a escolha da estrutura de dados e dos algoritmos tem um impacto profundo no desempenho de uma aplicação.

*(**Instrução para você**: Escreva aqui a sua conclusão final. Responda a perguntas como:)*

* **Qual foi a melhor combinação (função de hash + tratamento de colisão) e por quê?**
    * Provavelmente, a abordagem com **Rehash** será a vencedora em termos de desempenho de busca e controle de colisões, embora possa ter um custo maior de memória e picos de latência durante a operação de redimensionamento.
* **Qual a importância do fator de carga?**
    * O fator de carga (`elementos / tamanho da tabela`) foi crucial. A estratégia de `Rehash` provou que manter um fator de carga baixo (menor que 0.75) é fundamental para a eficiência da Tabela Hash.
* **Aprendizados do Projeto**: O que você aprendeu sobre estruturas de dados, análise de algoritmos e a importância de testes empíricos?
