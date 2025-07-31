package MAIN;

import Entity.Player;
import Entity.Inimigo;
import Entity.Flecha;
import Telas.GameOverScreen;
import Telas.TelaFim;
import Telas.TelaInicial;
import object.SuperObject;
import title.TileManager;
import util.Spritesheet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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
    public KeyHandler keyH = new KeyHandler(this);
    public TileManager tileM = new TileManager(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    Sound music = new Sound();
    Sound se = new Sound();
    Thread gameThread;

    // --- Entidades ---
    public Player player = new Player(this, keyH);
    public ArrayList<SuperObject> obj = new ArrayList<>();
    public ArrayList<Inimigo> inimigos = new ArrayList<>();
    public ArrayList<Flecha> flechas = new ArrayList<>();

    // --- Telas ---
    public TelaInicial telaInicial = new TelaInicial(this);
    public GameOverScreen gameOverScreen = new GameOverScreen(this);
    public TelaFim telaFim = new TelaFim(this);

    // --- FPS ---
    int FPS = 60;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        // ✅ Carrega os sprites uma única vez
        Spritesheet.carregarSprites();
    }

    public void setupGame() {
        aSetter.setObject();
        inimigos.clear(); // Evita duplicatas
        inimigos.add(new Inimigo(this));
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
            telaFim.update();
            return;
        }

        if (gameState == GAME_OVER_STATE || gameState == PAUSE_STATE) return;

        player.update();

        if (player.vida <= 0) {
            gameState = GAME_OVER_STATE;
            return;
        }

        // Atualiza inimigos
        for (Inimigo inimigo : inimigos) {
            if (inimigo != null) {
                inimigo.update();
            }
        }

        // ✅ Remove inimigos mortos
        inimigos.removeIf(inimigo -> !inimigo.ativo);

        // ✅ Remove flechas inativas para evitar lixo na lista
        flechas.removeIf(f -> !f.ativa);

        // Atualiza flechas
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
            telaFim.draw(g2);
        } else if (gameState == GAME_OVER_STATE) {
            gameOverScreen.draw(g2);
        } else {
            tileM.draw(g2);

            for (SuperObject objeto : obj) {
                if (objeto != null) objeto.draw(g2, this);
            }

            for (Inimigo inimigo : inimigos) {
                if (inimigo != null) inimigo.draw(g2);
            }

            for (Flecha flecha : flechas) {
                if (flecha != null && flecha.ativa) flecha.draw(g2);
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
        obj.clear();
        inimigos.clear();
        flechas.clear();
        aSetter.setObject();
        aSetter.setInimigos();
        gameState = PLAY_STATE;
        gameOverScreen.selectedOption = 0;
        telaFim.active = false;
        System.out.println("Jogo reiniciado");
    }
}
