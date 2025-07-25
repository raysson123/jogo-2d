package MAIN;

import Entity.Player;
import Entity.Inimigo;
import Entity.Flecha;
import Telas.GameOverScreen;
import Telas.TelaFim;
import Telas.TelaInicial;
import object.SuperObject;
import title.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // --- Configurações da Tela ---
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    // --- Estados do Jogo ---
    public static final int PLAY_STATE = 0;
    public static final int PAUSE_STATE = 1;
    public static final int GAME_OVER_STATE = 2;
    public int gameState = PLAY_STATE;

    // --- Sistema de Jogo ---
    TileManager tileM = new TileManager(this);
    Thread gameThread;
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public UI ui = new UI(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);

    // --- Entidades ---
    public Player player = new Player(this, keyH);
    public SuperObject[] obj = new SuperObject[10];
    public Inimigo[] inimigos = new Inimigo[10];
    public Flecha[] flechas = new Flecha[10];

    // --- FPS ---
    int FPS = 60;

    // --- Telas ---
    public TelaInicial telaInicial = new TelaInicial(this);
    public GameOverScreen gameOverScreen = new GameOverScreen(this);
    public TelaFim telaFim = new TelaFim(this); // ✅ nova tela de fim

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame() {
        aSetter.setObject();
        inimigos[0] = new Inimigo(this);
        aSetter.setInimigos();
        playMusic(0);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            update();
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000;
                if (remainingTime < 0) remainingTime = 0;
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (telaInicial.active) return;

        if (telaFim.active) {
            telaFim.update(); // ✅ atualiza contador da tela final
            return;
        }

        if (gameState == GAME_OVER_STATE) return;
        if (gameState == PAUSE_STATE) return;

        player.update();

        if (player.vida <= 0) {
            gameState = GAME_OVER_STATE;
            return;
        }

        for (Inimigo inimigo : inimigos) {
            if (inimigo != null) {
                inimigo.update();
            }
        }

        for (Flecha flecha : flechas) {
            if (flecha != null && flecha.ativa) {
                flecha.update();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (telaInicial.active) {
            telaInicial.draw(g2);
        } else if (telaFim.active) {
            telaFim.draw(g2); // ✅ desenha tela de fim
        } else if (gameState == GAME_OVER_STATE) {
            gameOverScreen.draw(g2);
        } else {
            tileM.draw(g2);

            for (SuperObject objeto : obj) {
                if (objeto != null) {
                    objeto.draw(g2, this);
                }
            }

            for (Inimigo inimigo : inimigos) {
                if (inimigo != null) {
                    inimigo.draw(g2);
                }
            }

            for (Flecha flecha : flechas) {
                if (flecha != null && flecha.ativa) {
                    flecha.draw(g2);
                }
            }

            player.draw(g2);
            ui.draw(g2);
        }

        g2.dispose();
    }

    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }

    public void restartGame() {
        player.setValoresIniciais();
        aSetter.setObject();
        aSetter.setInimigos();
        gameState = PLAY_STATE;
        gameOverScreen.selectedOption = 0;
        telaFim.active = false; // ✅ reseta tela de fim
        System.out.println("Jogo reiniciado");
    }
}
