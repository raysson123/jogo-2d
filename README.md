# 🎮 Jogo 2D em Java

Este é um jogo de plataforma 2D desenvolvido em Java, com foco em movimentação, colisão, inimigos, coleta de itens e interface com efeitos sonoros.

## 🧩 Funcionalidades Implementadas

- Movimentação do personagem (pular, andar)
- Colisão com o cenário e objetos interativos
- Coleta de itens (ex: moedas, chaves)
- Inimigos com movimentação e ataque
- Interface de usuário (UI) básica
- Efeitos sonoros (passos, ataques, coletar itens)
- Cenário com background e camadas parallax

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 11+
- **Bibliotecas/Frameworks:**
    - (ex: LibGDX, JavaFX, LWJGL — adicione conforme o seu uso)
- **Ferramentas Gráficas:**
    - Aseprite / Photoshop / Piskel (sprites)
    - Tiled (para edição de mapa, se aplicável)
- **Áudio:** efeitos sonoros em formato WAV/OGG

## 📁 Estrutura do Projeto

```bash
jogo-2d-java/
├── assets/            
│   ├── sprites/       # Personagem, inimigos, itens
│   ├── sounds/        # Efeitos sonoros
│   └── maps/          # Mapas em Tiled ou outro formato
├── src/
│   ├── com/jogo2d/
│   │   ├── Main.java          # Inicia o jogo
│   │   ├── game/              # Lógica principal do jogo
│   │   │   ├── GameLoop.java  
│   │   │   ├── Renderer.java  
│   │   │   └── InputHandler.java  
│   │   ├── entities/          # Player, Enemy, Item
│   │   │   ├── Player.java    
│   │   │   ├── Enemy.java     
│   │   │   ├── Item.java      
│   │   │   └── CollisionBox.java
│   │   ├── ui/                # Interface gráfica
│   │   │   └── UIManager.java
│   │   └── audio/             # Gerenciamento de áudio
│   │       └── SoundManager.java
├── libs/                      # Bibliotecas externas (Jar)
├── README.md
└── build.gradle / pom.xml     # Gerenciador de builds
```

## ▶️ Como Rodar o Jogo

### Usando Gradle

```bash
./gradlew run
```

### Usando Maven

```bash
mvn compile exec:java -Dexec.mainClass="com.jogo2d.Main"
```

### Alternativa: Compilação Manual

```bash
javac -d bin src/com/jogo2d/**/*.java
java -cp bin com.jogo2d.Main
```

> ⚠️ Certifique-se de que todas as dependências estão no classpath (`libs/`) ou no build.gradle/pom.xml.

## 🎮 Controles

- Setas ou `A`/`D` – mover
- `Espaço` – pular
- `J` – atacar/interagir

## 📌 Histórico de Atualizações (commits)

- `f0b509c` / `57bd6de`: Inimigos com movimentação e ataque; sprites atualizados
- `9edd5f3` / `d8c09da`: Efeitos sonoros finalizados; UI inicial pronta
- `cd283e5`: Colisão com objetos ativada; coleta de itens funcional
- `5a06e37` / `b60555d`: Parte 5 da colisão finalizada
- `cad487d` / `8d559a2` / `b134d35`: Implementação do background do jogo
- `f3c58c3`: Enumeração das funcionalidades implementadas até o momento
- `03faecc` / `033e052`: Etapas “passo a passo” do desenvolvimento

## 📦 Dependências

- Java 11 ou superior (recomendado)
- [Especifique as bibliotecas: ex LibGDX, ou JavaFX se for o caso]

## 👨‍💻 Autor
** Herandy Alexsander Melo de Barros**  
GitHub: [@Herandy-alexsander](https://github.com/Herandy-alexsander)

**Raysson Fernandes lucas**  
GitHub: [@raysson123](https://github.com/raysson123)

## 📄 Licença

Este projeto está licenciado sob a **MIT License** – veja o arquivo `LICENSE` para mais detalhes.

---

Contribuições, sugestões e reportes de bugs são muito bem-vindos! 🚀
