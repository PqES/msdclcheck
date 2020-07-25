# DCL⁺check

DCL⁺ check é uma ferramenta de conformidade arquitetural que permite a arquitetos de software restringir as comunicações realizadas entre microsserviços e verificar o projeto arquitetural de cada um deles.

Basicamente, a ferramenta possui como entrada o Aplicação composta por uma suíte de microsserviços, juntamente com sua Especificação Arquitetural. A execução da  ferramenta é apoiada pelos  módulos DCL+core, Extratores e Verificadoresde Comunicação e do Projeto Estrutural. As saídas retratam as Violações de Comunicação e Violações do Projeto Estrutural.

# Entradas
No que compete à aplicação, DCL⁺check requer o acesso ao repositório do código-fonte de cada microsserviço. Isso é necessário para que sejam extraídas as comunicações estaticamente realizadas entre os microsserviços, bem como a extração das dependências estruturais de cada microsserviço. Em contrapartida, a especificação arquitetural consiste em um arquivo de texto onde são definidas as restrições em DCL⁺.

 # DCL⁺Core
Nesse módulo, as informações presentes na especificação arquitetural são capturadas por meio de expressões regulares (Regex), a fim de detectar padrões de definição de um microsserviço, restrições de projeto estrutural e restrições de comunicação. As informações capturadas são armazenadas em estruturas de dados HashMap com o intuito de posteriormente verificar a conformidade de comunicação e do projeto estrutural.

# Extrator de Comunicação
Esse módulo extrai, do código-fonte de cada um dos microsserviços, as declarações de chamadas à outros microsserviços e armazena tais informações em  uma  estrutura  de dados HashMap. A atual implementação da  ferramenta DCL⁺check utiliza a técnica de análise estática para detectar as chamadas entre os microsserviços. Até o presente momento, a ferramenta possui implementação da  extração estática das comunicações por meio de AST  (Abstract  Syntax  Tree) para os microsserviços  desenvolvidos em Java e JavaScript. Espera-se a implementação do extrator de comunicação para alinguagem C# como trabalho futuro,  assim como para as demais linguagens de programação.

# Verificador de Comunicação 
Esse módulo verifica se as informações contidas na estrutura de dados HashMap, reportada pelo extrator de comunicação, estão em conformidade com as informações contidas na estrutura HashMap extraídas da especificação arquitetural. Essas informações são analisadas a fim de identificar violações arquiteturais de comunicação.

# Extrator do Projeto Extrutural
Esse módulo é apoiado por extratores externos desenvolvidos para as linguagens Java por meio do JavaDepExtractor, JavaScript por meio do JsDepExtractore C# por  meio do CsDepExtractor. Essas ferramentas extraem as dependências de projeto estrutural conforme a linguagem implementada em  cada microsserviço que compõe a aplicação. Em outras palavras, essas ferramentas extraem as dependências de um microsserviço em um conjunto de triplas no formato [dir−file,dependency−type,target−file] e definem como saída um arquivo texto com as dependências especificadas (dependencies.txt). 

# Verificador do Projeto Estrutural
Esse módulo analisa se dependências estabelecidas internamente à algum microsserviço – independente de sua linguagem – estão de acordo com o seu projeto arquitetural. Para tal propósito, foi utilizada a ferramenta piDCLcheck, o qual constitui outra contribuição prática deste estudo. A ferramenta define um conjunto de triplas no formato [dcl−file,folder−dir,dependencies−file]. A entrada [dcl−file] compreende às restrições de projeto arquitetural de cada microsserviço,[folder−dir] especifica o diretório onde encontra-se o arquivo de dependências e,  por fim, [dependencies−file] compreende ao arquivo de dependências gerados pelos extratores estruturais. Como resultado, a ferramenta reporta um conjunto de violações do projeto estrutural.

# Saídas
- Violações de Comunicação e Violações do Projeto Estrutural
Na ferramenta DCL⁺check, essas violações de comunicação e do projeto estrutural são agrupadas e reportadas em um arquivo texto.



