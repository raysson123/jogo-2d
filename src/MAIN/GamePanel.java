package MAIN;

import Entity.Player;
import object.SuperObject;
import title.TileManager;


import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // --- Configurações da Tela ---
    // Tamanho base de um 'tile' (bloco) em pixels. Um 'tile' é a unidade básica de medida no jogo (ex: 16x16 pixels).
    final int originalTileSize = 16;
    // Fator de escala para aumentar o tamanho dos 'tiles' na tela.
    // Isso torna os gráficos pequenos mais visíveis em telas maiores (ex: 16 * 3 = 48 pixels).
    final int scale = 3;
    // Tamanho final de um 'tile' na tela após a aplicação da escala.
    public final int tileSize = originalTileSize * scale; // Resulta em 48x48 pixels por tile.
    // Número máximo de colunas de 'tiles' que cabem na tela do jogo.
    public final int maxScreenCol = 20;
    // Número máximo de linhas de 'tiles' que cabem na tela do jogo.
    public   final int maxScreenRow = 12;
    // Largura total da tela do jogo em pixels (número de colunas * tamanho do tile).
    public final int screenWidth = tileSize * maxScreenCol; // Ex: 48 * 20 = 768 pixels.
    // Altura total da tela do jogo em pixels (número de linhas * tamanho do tile).
    public final int screenHeight = tileSize * maxScreenRow; // Ex: 48 * 12 = 576 pixels.
    //WORLD SETTINGS
    public  final  int maxWorldCol = 50;
    public  final int maxWorldRow = 50;

    // --- Thread do Jogo ---
    // A thread que executará o ciclo principal do jogo (o 'game loop').
    // Uma thread separada garante que o jogo não congele a interface do usuário.
    TileManager tileM = new TileManager(this); // Inicializa o gerenciador de tiles com o GamePanel.
    Thread gameThread;
    // Instância do nosso manipulador de teclado, que detecta as teclas pressionadas e liberadas.
    KeyHandler keyH = new KeyHandler();
    Sound music = new Sound(); // Inicializa o objeto Sound.
    Sound se = new Sound(); // Inicializa o objeto Sound.
    public UI ui = new UI(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    // --- Entidades do Jogo ---
    public AssetSetter aSetter = new AssetSetter(this); // Inicializa o AssetSetter com o GamePanel.



    // Instância do jogador. Agora o GamePanel gerencia um objeto Player.
    public Player player = new Player(this, keyH); // Passa o próprio GamePanel e o KeyHandler para o Player.
    public SuperObject obj[] = new SuperObject[10];



    // --- FPS (Frames por Segundo) ---
    int FPS = 60; // Define a taxa de quadros desejada para o jogo.

    // Construtor da classe GamePanel.
    public GamePanel() {
        // Define o tamanho preferido deste painel, que será usado pelo JFrame para dimensionar a janela.
        this.setPreferredSize(new java.awt.Dimension(screenWidth, screenHeight));
        // Define a cor de fundo do painel como preto.
        this.setBackground(Color.black);
        // Ativa o 'double buffering'. Isso significa que o desenho é feito em um buffer de imagem fora da tela
        // e depois copiado para a tela de uma vez, evitando cintilação visual.
        this.setDoubleBuffered(true);
        // Adiciona o KeyHandler como ouvinte de eventos de teclado a este painel.
        // Isso permite que o GamePanel receba e processe as entradas do teclado.
        this.addKeyListener(keyH);
        // Permite que este painel receba o foco para capturar as entradas do teclado.
        // É essencial para que o KeyListener funcione.
        this.setFocusable(true);
    }
    public void setupGame(){
        aSetter.setObject();
        playMusic(0);
    }

    // Método para iniciar a thread do jogo.
    public void startGameThread() {
        // Cria uma nova thread, passando 'this' (esta instância de GamePanel) como Runnable.
        // O método run() desta classe será executado nesta nova thread.
        gameThread = new Thread(this);
        // Inicia a execução da thread, o que faz com que o método run() seja chamado.
        gameThread.start();
    }

    // O método 'run()' é o coração do game loop, executado continuamente pela 'gameThread'.
    @Override
    public void run() {
        // Calcula o intervalo de tempo em nanossegundos que cada quadro deve durar para atingir o FPS desejado.
        // Ex: Para 60 FPS, 1 bilhão de nanossegundos (1 segundo) / 60 = ~16.666.666 nanossegundos por quadro.
        double drawInterval = 1000000000.0 / FPS;
        // Calcula o tempo em nanossegundos em que o próximo quadro deve ser desenhado.
        // É o tempo atual mais o intervalo de desenho.
        double nextDrawTime = System.nanoTime() + drawInterval;

        // Loop principal do jogo. Continua enquanto a thread do jogo estiver ativa (não nula).
        while (gameThread != null) {
            // 1. UPDATE: Atualiza todas as informações do jogo (posição do personagem, lógica de animação, etc.).
            // Delega a atualização do jogador para a classe Player.
            player.update();

            // 2. DRAW: Solicita que o painel seja redesenhado com as informações atualizadas.
            // Este método internamente chama o método paintComponent().
            repaint();

            try {
                // Calcula o tempo restante até o próximo desenho para manter o FPS consistente.
                // Isso ajuda a garantir que o jogo rode na mesma velocidade em diferentes máquinas.
                double remainingTime = nextDrawTime - System.nanoTime();
                // Converte o tempo restante de nanossegundos para milissegundos, pois Thread.sleep() usa milissegundos.
                remainingTime = remainingTime / 1000000;

                // Se o tempo restante for negativo (o processamento e desenho demoraram mais que o esperado),
                // define como 0 para não atrasar ainda mais o próximo quadro.
                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                // Pausa a thread pelo tempo restante calculado.
                // Isso "espera" até que seja a hora de desenhar o próximo quadro.
                Thread.sleep((long) remainingTime);

                // Atualiza o tempo para o próximo desenho, adicionando o intervalo de desenho ao tempo anterior.
                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                // Captura exceções se a thread for interrompida (ex: ao fechar o jogo) e imprime o rastreamento da pilha.
                e.printStackTrace();
            }
        }
    }

    // Método 'update()' é onde a lógica de atualização do jogo é processada.
    // Agora, a lógica de atualização do jogador foi movida para a classe Player.
    public void update() {
        // A lógica de atualização do jogador agora está dentro de player.update()
        player.update();
    }

    // Método 'paintComponent()' é responsável por desenhar todos os elementos gráficos na tela.
    // É chamado automaticamente quando repaint() é invocado.
    @Override
    public void paintComponent(Graphics g) {
        // Chama o método paintComponent da superclasse (JPanel) para limpar o painel.
        // Isso é importante para evitar "rastros" de desenhos anteriores.
        super.paintComponent(g);

        // Converte o objeto Graphics para Graphics2D para usar funcionalidades de desenho 2D mais avançadas,
        // como desenhar imagens.
        Graphics2D g2 = (Graphics2D) g;

        // Delega o desenho do jogador para a classe Player.
        //tile
        tileM.draw(g2);
        //obj
        for(int i = 0; i < obj.length; i++){
            if(obj[i] != null){
                obj[i].draw(g2, this);
            }
        }
        //player
        player.draw(g2);

        //UI
        ui.draw(g2);

        // Libera os recursos gráficos usados por este contexto Graphics2D.
        // É uma boa prática para otimização de memória.
        g2.dispose();
    }
    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }
    public void stopMusic() {
        music.stop();
    }
    public void playSE(int i){
        se.setFile(i);
        se.play();
    }
}