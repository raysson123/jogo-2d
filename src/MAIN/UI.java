package MAIN;

import Telas.GameOverScreen;
import object.OBJ_Key;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {

    public boolean gameOverOn = false;
    public GameOverScreen gameOverScreen;

    GamePanel gp;
    Font arial_40;
    BufferedImage keyImage;

    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;

    // Menu de pausa
    public boolean pauseMenuOn = false;
    public String[] pauseOptions = {"Retornar", "Reiniciar", "Sair", "Ajuda"};
    public int selectedOption = 0;

    public UI(GamePanel gp) {
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        OBJ_Key key = new OBJ_Key();
        keyImage = key.image;
        gameOverScreen = new GameOverScreen(gp);
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
        messageCounter = 0;
    }

    public void draw(Graphics2D g2) {
        g2.setFont(arial_40);
        g2.setColor(Color.white);

        if (gameOverOn) {
            gameOverScreen.draw(g2);
            return;
        }

        drawHUD(g2);

        if (messageOn) drawMessage(g2);

        if (pauseMenuOn) drawPauseMenu(g2);
    }

    private void drawHUD(Graphics2D g2) {
        // 🔑 Chave
        g2.drawImage(keyImage, gp.tileSize / 2, gp.tileSize / 2, gp.tileSize, gp.tileSize, null);
        g2.drawString("x " + gp.player.hasKey, 75, 65);

        // ❤️ Vida
        drawLifeBar(g2);
    }

    private void drawMessage(Graphics2D g2) {
        g2.setFont(g2.getFont().deriveFont(30F));
        g2.setColor(Color.WHITE);
        g2.drawString(message, gp.tileSize / 2, gp.tileSize * 6);

        messageCounter++;
        if (messageCounter > 120) {
            messageCounter = 0;
            messageOn = false;
        }
    }

    private void drawLifeBar(Graphics2D g2) {
        int margin = 10;
        int width = gp.tileSize * 4;
        int height = 20;
        int x = gp.screenWidth - width - margin;
        int y = gp.tileSize + 10;

        // Nível
        g2.setFont(g2.getFont().deriveFont(20F));
        g2.setColor(Color.CYAN);
        g2.drawString("Nível: " + gp.player.nivel, x + 5, y - 10);

        // Fundo
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(x, y, width, height);

        // Proporção
        double lifeRatio = (double) gp.player.vida / gp.player.vidaMaxima;
        int currentWidth = (int) (width * lifeRatio);

        // Cor dinâmica
        if (lifeRatio > 0.6) g2.setColor(Color.GREEN);
        else if (lifeRatio > 0.3) g2.setColor(Color.ORANGE);
        else g2.setColor(Color.RED);

        g2.fillRect(x, y, currentWidth, height);

        // Borda
        g2.setColor(Color.WHITE);
        g2.drawRect(x, y, width, height);

        // Texto
        g2.setFont(g2.getFont().deriveFont(20F));
        g2.setColor(Color.WHITE);
        g2.drawString("Vida: " + gp.player.vida + " / " + gp.player.vidaMaxima, x + 5, y + 17);
    }

    private void drawPauseMenu(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(arial_40);
        g2.setColor(Color.WHITE);
        g2.drawString("PAUSA", gp.screenWidth / 2 - 70, gp.tileSize * 3);

        g2.setFont(g2.getFont().deriveFont(24F));
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawString("Aperte espaço para escolher", gp.screenWidth / 2 - 140, gp.tileSize * 4);

        g2.setFont(g2.getFont().deriveFont(32F));
        for (int i = 0; i < pauseOptions.length; i++) {
            g2.setColor(i == selectedOption ? Color.YELLOW : Color.WHITE);
            g2.drawString(pauseOptions[i], gp.screenWidth / 2 - 100, gp.tileSize * (6 + i));
        }
    }
}
