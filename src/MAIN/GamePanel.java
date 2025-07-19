package MAIN;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class GamePanel extends JPanel implements Runnable {
    // Configurações da tela
    final int originalTileSize = 16; // Tamanho original de um 'tile' (bloco) em pixels
    final int scale = 3; // Fator de escala para aumentar o tamanho dos 'tiles' na tela
    public final int tileSize = originalTileSize * scale; // Tamanho final de um 'tile' na tela (16 * 3 = 48 pixels)
    final int maxScreenCol = 16; // Número máximo de colunas de 'tiles' na tela
    final int maxScreenRow = 12; // Número máximo de linhas de 'tiles' na tela
    final int screenWidth = tileSize * maxScreenCol; // Largura total da tela do jogo em pixels (48 * 16 = 768 pixels)
    final int screenHeight = tileSize * maxScreenRow; // Altura total da tela do jogo em pixels (48 * 12 = 576 pixels)

    Thread gameThread; // A thread que executará o ciclo principal do jogo
    KeyHandler keyH = new KeyHandler(); // Instância do nosso manipulador de teclado

    // Posição inicial do jogador
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4; // Velocidade de movimento do jogador

    // Sprite do jogador
    BufferedImage playerSprite;
    int spriteNum = 1; // Para simular quadros de animação (ex: 1 ou 2)
    int spriteCounter = 0; // Contador para controlar a troca de quadros de animação

    // FPS (Frames por Segundo)
    int FPS = 60; // Define a taxa de quadros desejada para o jogo

    public GamePanel() {
        this.setPreferredSize(new java.awt.Dimension(screenWidth, screenHeight)); // Define o tamanho preferido do painel
        this.setBackground(Color.black); // Define a cor de fundo do painel como preto
        this.setDoubleBuffered(true); // Ativa o 'double buffering' para uma renderização mais suave, evitando cintilação
        this.addKeyListener(keyH); // Adiciona o KeyHandler como ouvinte de teclado a este painel
        this.setFocusable(true); // Permite que este painel receba o foco para capturar entradas do teclado

        getPlayerImage(); // Carrega a imagem do jogador ao inicializar o painel
    }

    public void getPlayerImage() {
        try {
            // URL de uma imagem de placeholder. Em um jogo real, você carregaria seus próprios arquivos de imagem.
            URL imageUrl = new URL("https://placehold.co/48x48/ffffff/000000?text=Player");
            playerSprite = ImageIO.read(imageUrl);
        } catch (IOException e) {
            System.err.println("Erro ao carregar a imagem do jogador: " + e.getMessage());
            // Em caso de erro ao carregar a imagem, cria um sprite de fallback magenta
            // para que o jogo ainda mostre algo no lugar do jogador.
            playerSprite = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = playerSprite.createGraphics();
            g2.setColor(Color.MAGENTA); // Cor de erro facilmente visível
            g2.fillRect(0, 0, tileSize, tileSize);
            g2.dispose(); // Libera os recursos gráficos temporários
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this); // Cria uma nova thread, passando esta instância de GamePanel (que implementa Runnable)
        gameThread.start(); // Inicia a execução da thread, que por sua vez chama o método run()
    }

    @Override
    public void run() {
        // Calcula o intervalo de tempo para cada quadro para atingir o FPS desejado
        double drawInterval = 1000000000.0 / FPS; // 1 bilhão de nanossegundos (1 segundo) dividido pelo FPS
        double nextDrawTime = System.nanoTime() + drawInterval; // Calcula o tempo em que o próximo quadro deve ser desenhado

        // O loop principal do jogo
        while (gameThread != null) { // Continua enquanto a thread do jogo estiver ativa
            // 1. UPDATE: Atualiza informações do jogo (posição do personagem, etc.)
            update();

            // 2. DRAW: Desenha a tela com as informações atualizadas
            repaint(); // Chama o método paintComponent() do JPanel para redesenhar o painel

            try {
                // Calcula o tempo restante até o próximo desenho para manter o FPS consistente
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000; // Converte nanossegundos para milissegundos

                if (remainingTime < 0) { // Se o tempo restante for negativo (o desenho demorou mais que o esperado)
                    remainingTime = 0; // Define como 0 para não atrasar o próximo quadro
                }

                Thread.sleep((long) remainingTime); // Pausa a thread pelo tempo restante calculado

                nextDrawTime += drawInterval; // Atualiza o tempo para o próximo desenho
            } catch (InterruptedException e) {
                // Se a thread for interrompida, restaura o estado de interrupção
                // e pode-se decidir sair do loop ou logar o erro.
                Thread.currentThread().interrupt();
                System.err.println("A thread do jogo foi interrompida: " + e.getMessage());
                // Opcional: break; para sair do loop se a interrupção for crítica
            }
        }
    }

    public void update() {
        // Lógica de atualização do jogo (movimento do jogador, inimigos, etc.)
        // Verifica o estado das teclas e move o jogador
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            if (keyH.upPressed) {
                playerY -= playerSpeed;
            }
            if (keyH.downPressed) {
                playerY += playerSpeed;
            }
            if (keyH.leftPressed) {
                playerX -= playerSpeed;
            }
            if (keyH.rightPressed) {
                playerX += playerSpeed;
            }
            // Lógica simples para simular animação (troca de sprite)
            spriteCounter++;
            if (spriteCounter > 12) { // Troca de sprite a cada 12 frames (ajuste para a velocidade da animação)
                spriteNum = (spriteNum == 1) ? 2 : 1; // Alterna entre 1 e 2 de forma mais concisa
                spriteCounter = 0;
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Chama o método paintComponent da superclasse (JPanel) para limpar o painel

        Graphics2D g2 = (Graphics2D) g; // Converte o objeto Graphics para Graphics2D para mais funcionalidades de desenho

        // Desenha o sprite do jogador na sua posição atual.
        // O playerSprite nunca será nulo devido ao tratamento de erro em getPlayerImage().
        g2.drawImage(playerSprite, playerX, playerY, tileSize, tileSize, null);

        g2.dispose(); // Libera os recursos gráficos usados por este contexto Graphics2D
    }
}