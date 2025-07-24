package MAIN;

import object.OBJ_Key;
import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {
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
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
        messageCounter = 0;
    }

    public void draw(Graphics2D g2) {
        g2.setFont(arial_40);
        g2.setColor(Color.white);

        // Exibe chave do jogador
        g2.drawImage(keyImage, gp.tileSize / 2, gp.tileSize / 2, gp.tileSize, gp.tileSize, null);
        g2.drawString("x " + gp.player.hasKey, 75, 65);

        // Mensagem temporária
        if (messageOn) {
            g2.setFont(g2.getFont().deriveFont(30F));
            g2.drawString(message, gp.tileSize / 2, gp.tileSize * 6);

            messageCounter++;
            if (messageCounter > 120) {
                messageCounter = 0;
                messageOn = false;
            }
        }

        // Menu de pausa
        if (pauseMenuOn) {
            drawPauseMenu(g2);
        }
    }

    private void drawPauseMenu(Graphics2D g2) {
        // Fundo escuro semitransparente
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Título
        g2.setFont(arial_40);
        g2.setColor(Color.WHITE);
        g2.drawString("PAUSA", gp.screenWidth / 2 - 70, gp.tileSize * 3);

        // Instrução
        g2.setFont(g2.getFont().deriveFont(24F));
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawString("Aperte espaço para escolher", gp.screenWidth / 2 - 140, gp.tileSize * 4);

        // Opções
        g2.setFont(g2.getFont().deriveFont(32F));
        for (int i = 0; i < pauseOptions.length; i++) {
            if (i == selectedOption) {
                g2.setColor(Color.YELLOW);
            } else {
                g2.setColor(Color.WHITE);
            }
            g2.drawString(pauseOptions[i], gp.screenWidth / 2 - 100, gp.tileSize * (6 + i));
        }
    }
}
