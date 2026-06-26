
# Trabalho 3 - Git e GitHub no Robocode - Bruno, Lucas e Julio

<div align="center">
  Professor: Diego da Silva de Medeiros | <a href="mailto:diegomedeiros@ifsc.edu.br">diegomedeiros@ifsc.edu.br</a>
</div>

---

# Marquitos

### Bruno Santana, Lucas Zimmermann e Julio Barbosa


## 1\. Introdução

A atividade desenvolvida consiste na familiarização de diferentes aspectos importantes no desenvolvimento de software, sendo estes 1\) Familiarização com a lógica básica de programação através do desenvolvimento do robô; 2\) Apresentação a lógica de desenvolvimento de software descentralizado, conhecendo então a estratégia de versionamento de software com base na ferramenta de código aberto *Git;* 3\) Vivência prática dos problemas de desenvolvimento de software em equipe as diferentes estratégias para contornar os diferentes impedimentos encontrados. 
Assim sendo, a ferramenta base para esse conjunto de objetivos foi o Robocode sendo este um jogo que utiliza a programação para criar um robô que irá lutar contra outros robôs em uma arena de tamanho pré-definido. A programação é utilizada para definir a inteligência do robô, seus métodos, comportamento e estratégia. Trata-se de uma ferramenta consolidada para o ensino e aprendizagem, contato com vários materiais de apoio. Cabe destacar o trabalho de Helder Linhares Bertoldo dos Reis intitulado “Robocode: Manual de instruções” (UFJF).   
Demos início no projeto por meio do *sample* do robocode, experimentando mudanças simples como de cor do robô ou adição da “dança da vitória” buscando conhecer mais do funcionamento do *Git* e do *Github*. Essa experiência possibilitou futuras implementações de *features* no robô e também uso de conceitos intermediários de *Git* como *branches, merge, commit, pull request* e *code reviews*. 

## 2\. Objetivos da Atividade

Visando o escopo da disciplina, encaramos a atividade como uma oportunidade de adquirir conhecimentos básicos desta consolidada ferramenta de versionamento de código. Desta forma, como já mencionado anteriormente, enxergamos os seguintes objetivos específicos que complementam os objetivos supracitados:

1. Familiarização com a lógica básica de programação através do desenvolvimento do robô;   
2. Apresentação a lógica de desenvolvimento de software descentralizado, conhecendo então a estratégia de versionamento de software com base na ferramenta de código aberto  *Git;*   
3. Vivência prática dos problemas de desenvolvimento de software em equipe com diferentes estratégias para contornar os diferentes impedimentos encontrados.

## 3\. Descrição da Atividade

##### **Programação do Robô**

O robô desenvolvido em equipe foi batizado de Marquito e foi implementado em Java, estendendo a classe AdvancedRobot do Robocode. Partimos do robô de exemplo (sample) fornecido pelo próprio Robocode e foi incrementando suas capacidades ao longo das iterações.  
O desenvolvimento seguiu uma progressão gradual. Inicialmente, foram feitas alterações simples, como a definição de cores personalizadas e a adição de comportamentos básicos. Após isso foram implementadas funcionalidades mais sofisticadas, dentre as quais se destacam:

* Sistema de rastreamento de inimigos: o robô mantém um dicionário (HashMap) com as informações de cada inimigo detectado (posição, energia, velocidade, direção e tempo da última detecção), permitindo comportamentos táticos mais elaborados;  
* Seleção inteligente de alvo: a função chooseTarget() pontua os inimigos com base em distância, energia, velocidade e tempo desde o último avistamento, priorizando os adversários mais vulneráveis;  
* Movimentação por campo de forças: o método doMovement() calcula vetores de força repulsivos dos inimigos e das paredes, além de uma força atrativa em direção ao centro do campo, resultando em um movimento fluido e difícil de prever;  
* Suavização de trajetória em paredes: implementada na função wallSmoothing(), que ajusta o ângulo de movimento para evitar colisões com as bordas da arena;  
* Mira preditiva: a função doGun() estima a posição futura do inimigo com base em sua velocidade e direção, calculando o ângulo de tiro necessário para acertar o alvo em movimento;  
* Gerenciamento dinâmico do radar: o radar acompanha o alvo selecionado com rotação ajustada; em caso de perda do alvo, realiza varredura completa da arena;  
* Potência de tiro adaptativa: a função firePower() ajusta a potência dos projéteis com base na distância do alvo, no número de inimigos restantes, na energia do oponente e na energia própria do robô.

##### **Uso do Git para Controle de Versão**

O Git foi utilizado desde o início da atividade como principal ferramenta de controle de versão. O repositório remoto foi hospedado no GitHub, facilitando a colaboração entre a equipe. O fluxo de trabalho adotado envolveu os seguintes passos recorrentes:

1. Clonagem do repositório remoto para a máquina local de cada integrante;  
2. Criação de branches específicas para cada integrante;  
3. Realização de commits incrementais com mensagens descritivas do que foi alterado;  
4. Abertura de pull requests para integração das mudanças à branch principal;  
5. Revisão do código pelos demais membros.

## 4\. Estrutura do Git Utilizada

##### **Repositório**

O repositório zzzimmer/marquito foi criado no GitHub como repositório público. Sua estrutura de arquivos é simples e objetiva, contendo:

* Marquito.java — arquivo principal com o código-fonte do robô;  
* marquito.Marquito\_1.0.jar — arquivo compilado do robô, pronto para uso no Robocode;  
* .gitignore — arquivo de configuração do Git para ignorar arquivos sem necessidade de rastreá-los.

##### **Branches**

Como equipe, adotamos uma estratégia de branches baseada nos integrantes do grupo: cada membro trabalhou em sua própria branch pessoal, onde realizava suas contribuições e experimentos de forma independente. A branch principal (main) foi reservada exclusivamente para a junção do trabalho de todos, recebendo o código apenas após revisão e aprovação via pull request. Essa abordagem permitiu que os integrantes desenvolvessem em paralelo sem interferir no trabalho uns dos outros, centralizando na main apenas o código consolidado e revisado.

##### **Commits**

Ao longo da atividade, foram realizados 19 commits, registrando desde as primeiras alterações estéticas (como mudança de cor) até as implementações avançadas do sistema de movimentação e mira. A prática de escrever mensagens de commit claras foi adotada, visando compreensão do histórico de mudanças por todos os membros da equipe.

##### **Pull Requests**

Os pull requests (PRs) foram usados para integração e revisão de código no projeto. Ao todo, foram abertos e concluídos 3 pull requests. Cada PR representava um conjunto de mudanças proposto por um integrante, que aguardava revisão e aprovação dos demais antes de ser incorporado à branch main. Esse processo incentivou a leitura mútua do código, a identificação de erros e a troca de conhecimento entre os membros.

## 5\. Resultados e Aprendizados

A atividade permitiu que os integrantes vivenciassem na prática o ciclo completo de trabalho com Git e GitHub. Entre os principais aprendizados, destacam-se:

* A importância de commits frequentes e bem descritos para rastrear a evolução do código;  
* O uso de branches para isolar o desenvolvimento, reduzindo riscos de conflito;  
* O processo de pull request como ferramenta de revisão de código e garantia de qualidade;	  
* A resolução de conflitos de merge, inevitáveis quando múltiplos membros alteram os mesmos arquivos.

Ao final da atividade, saímos com um repertório prático de Git e GitHub que inclui: criação e gerenciamento de repositórios remotos, trabalho com branches individuais, escrita de mensagens de commit claras e descritivas, abertura e revisão de pull requests, resolução de conflitos de merge. Por fim, compreendemos a lógica por trás de cada etapa — por que versionar, por que ramificar e por que revisar antes de integrar.

## 6\. Conclusão

A realização deste trabalho nos proporcionou uma experiência abrangente e integrada entre programação e controle de versão colaborativo. O desenvolvimento do robô Marquito, em Java com a plataforma Robocode, exigiu integração dos conhecimentos curriculares: partindo de um exemplo básico até a implementação de funcionalidades sofisticadas como movimentação por campo de forças, mira preditiva e gerenciamento dinâmico de radar. Esse processo evidenciou que o desenvolvimento iterativo, aliado a boas práticas de versionamento, é essencial para a evolução controlada de um projeto.

O uso do Git e GitHub ao longo de toda a atividade consolidou, na prática, conceitos fundamentais do desenvolvimento de software em equipe. A adoção de branches individuais, a escrita de commits descritivos e a revisão de código via pull requests tornaram o fluxo de trabalho mais organizado e transparente, ao mesmo tempo em que proporcionaram experiência direta na resolução de conflitos de merge — um dos desafios mais comuns em projetos colaborativos.

Em síntese, a atividade cumpriu os objetivos propostos: familiarizar os integrantes com a lógica de programação, apresentar a filosofia do desenvolvimento descentralizado e vivenciar os desafios reais do trabalho em equipe. Os conhecimentos adquiridos — tanto em Java/Robocode quanto em Git/GitHub — formam uma base sólida e diretamente aplicável em projetos futuros, acadêmicos ou profissionais.

## 7\. Anexos

Segue anexo alguns screenshots do guerreiro marquito em ação.

#### Marquito vs Debochas: Treinamento
<img src="https://imgur.com/Zeh2V17.png"/>

#### Marquito em maus lençois:
<img src="https://imgur.com/3SW4uUn.png"/>

#### Dias de luta e dias de glória:  
<img src="https://imgur.com/yx3F1MD.png"/>
