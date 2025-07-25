package Telas;

import MAIN.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class GameOverScreen {
    GamePanel gp;
    BufferedImage background;
    public String[] options = {"Reiniciar", "Sair do Jogo"};
    public int selectedOption = 0;

    public GameOverScreen(GamePanel gp) {
        this.gp = gp;
        try {
            InputStream is = getClass().getResourceAsStream("/telas/gameOver.png");
            background = ImageIO.read(is);
        } catch (IOException e) {
            System.err.println("Erro ao carregar imagem de Game Over: " + e.getMessage());
        }
    }

    public void draw(Graphics2D g2) {
        // 🖼️ Fundo
        g2.drawImage(background, 0, 0, gp.screenWidth, gp.screenHeight, null);

        // 📝 Título
        g2.setFont(new Font("Arial", Font.BOLD, 48));
        g2.setColor(Color.RED);
        String titulo = "Você perdeu!";
        int tituloX = getCenteredX(g2, titulo);
        g2.drawString(titulo, tituloX, gp.tileSize * 3);

        // 🧭 Opções
        g2.setFont(new Font("Arial", Font.PLAIN, 32));
        for (int i = 0; i < options.length; i++) {
            String texto = options[i];
            int x = getCenteredX(g2, texto);
            int y = gp.tileSize * (5 + i * 2);

            g2.setColor(i == selectedOption ? Color.YELLOW : Color.WHITE);
            g2.drawString(texto, x, y);
        }
    }

    // 📐 Centraliza texto horizontalmente
    private int getCenteredX(Graphics2D g2, String text) {
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        return (gp.screenWidth - textWidth) / 2;
    }
}
