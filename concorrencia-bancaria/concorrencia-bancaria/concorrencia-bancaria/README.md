# 📝 Trabalho Prático: Concorrência e Consistência em Banco de Dados com Spring Boot

Este projeto tem como objetivo demonstrar na prática os problemas de concorrência em sistemas transacionais, especificamente o fenômeno do **Lost Update (Atualização Perdida)**, e como resolvê-lo utilizando os recursos do JPA/Hibernate.

## 👥 Integrantes e Divisão do Trabalho

* **Aluno A:** Jamile Barbosa - *Responsável pela Parte 1: O Problema da Concorrência e o Cenário Sem Bloqueio.*
* **Aluno B:** Welerson Sousa - *Responsável pela Parte 2: A Solução com Controle de Versão Otimista (@Version).*

---

## 🚀 Instruções de Execução

### Pré-requisitos
* Java JDK 17 ou superior instalado.
* Apache JMeter instalado (para execução dos testes de carga).

### Como Rodar a Aplicação Spring Boot
1. Clone este repositório em sua máquina.
2. Abra o projeto na sua IDE de preferência (VS Code ou IntelliJ).
3. Aguarde o download das dependências do Maven (`pom.xml`).
4. Localize a classe principal `DemoApplication.java` (ou `BancoApplication.java`) dentro do pacote `com.example.banco`.
5. Execute o projeto clicando em **Run** ou utilizando a linha de comando:
   ```bash
   ./mvnw spring-boot:run

concorrencia-bancaria/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── bancaria/
│   │   │               ├── controller/
│   │   │               │   ├── ContaController.java
│   │   │               │   └── ContaVersionadaController.java
│   │   │               ├── service/
│   │   │               │   ├── ContaService.java
│   │   │               │   └── ContaVersionadaService.java
│   │   │               ├── repository/
│   │   │               │   ├── ContaRepository.java
│   │   │               │   └── ContaVersionadaRepository.java
│   │   │               ├── entity/
│   │   │               │   ├── ContaBancaria.java
│   │   │               │   └── ContaBancariaVersionada.java
│   │   │               └── ConcorrenciaBancariaApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── ContaBancaria.jmx
│       └── ContaBancariaVersionada.jmx
├── pom.xml
└── teste-concorrencia.jmx

## 📊 Relatório de Conclusão e Análise Comparativa

Para validar o comportamento do sistema sob concorrência, configuramos um plano de testes no **Apache JMeter** (`teste-concorrencia.jmx`) simulando **50 requisições simultâneas de depósito de R$ 10,00** em uma conta que inicia com o saldo de **R$ 1.000,00**. 

O resultado matemático esperado após todas as operações seria um saldo final de **R$ 1.500,00**.

### ❌ Parte 1: Cenário Sem Bloqueio (Aluno A - Jamile Barbosa)
* **Endpoint Testado:** `POST /contas/1/deposito`
* **Comportamento Observado:** Todas as 50 requisições retornaram HTTP Status `200 OK`. No entanto, ao consultar o banco de dados H2 após o teste, **o saldo final ficou incorreto** (R$ 1.070,00).
* **Explicação (Lost Update):** Como não há controle de concorrência, duas ou mais threads leram o saldo original (ex: R$ 1.000,00) ao mesmo tempo, calcularam o novo valor (R$ 1.010,00) e salvaram por cima uma da outra. Isso fez com que diversas atualizações fossem completamente "engolidas" (perdidas).

> **[imagem: SaldoH2 parte 1.png]**

---

###  Parte 2: Cenário Com Controle Otimista (Aluno B - Welerson Sousa)
* **Endpoint Testado:** `POST /api/v2/contas/1/deposito`
* **Comportamento Observado:** Apenas a primeira requisição que chegou ao banco de dados foi processada com sucesso (`200 OK`). As outras requisições concorrentes falharam propositalmente, retornando o HTTP Status `409 Conflict`.
* **Explicação (@Version):** O Hibernate utilizou o atributo `version` da entidade `ContaBancariaVersionada`. Quando uma thread tentava salvar a conta com um número de versão antigo (porque outra thread já havia atualizado e incrementado a versão antes), o Spring interceptou a falha (`ObjectOptimisticLockingFailureException`) e o nosso Controller respondeu de forma amigável com o erro de conflito. **A consistência do saldo foi totalmente preservada.**

> **[imagem: saldoh2.png]**

---

## 🛠️ Como Testar no JMeter

1. Abra o Apache JMeter.
2. Vá em `File -> Open` e selecione o arquivo `ContaBancaria.jmx` e `ContaBancariaVersionada.jmx` localizado na pasta test que fica dentro src deste projeto.
3. Certifique-se de que a aplicação Spring Boot está rodando.
4. Clique no botão **Start** (ícone de Play verde) para rodar os testes.
5. Verifique as abas *View Results Tree* (Árvore de Resultados) e *Summary Report* para analisar as respostas HTTP de cada cenário.