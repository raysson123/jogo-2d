package Telas;

import MAIN.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class TelaFim {
    GamePanel gp;
    BufferedImage background;
    public boolean active = false;
    private int tempoExibicao = 0; // ⏱️ contador de frames

    public TelaFim(GamePanel gp) {
        this.gp = gp;
        try {
            InputStream is = getClass().getResourceAsStream("/telas/telaFim.png");
            background = ImageIO.read(is);
        } catch (IOException e) {
            System.err.println("Erro ao carregar imagem de fim de história: " + e.getMessage());
        }
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(background, 0, 0, gp.screenWidth, gp.screenHeight, null);

        g2.setFont(new Font("Arial", Font.BOLD, 32));
        g2.setColor(Color.WHITE);

        String[] mensagem = {
                "Parabéns!",
                "Você chegou ao fim do capítulo Prequel.",
                "Aguarde novas atualizações para o próximo capítulo.",
                "Att: Herandy Alexsander & Raysson Lucas"
        };

        int y = gp.tileSize * 4;
        for (String linha : mensagem) {
            int x = getCenteredX(g2, linha);
            g2.drawString(linha, x, y);
            y += gp.tileSize * 2;
        }
    }

    public void update() {
        if (!active) return;

        tempoExibicao++;

        if (tempoExibicao >= 300) { // ⏳ 5 segundos a 60 FPS
            System.exit(0);
        }
    }

    private int getCenteredX(Graphics2D g2, String text) {
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        return (gp.screenWidth - textWidth) / 2;
    }

    public void ativar() {
        active = true;
        tempoExibicao = 0;
    }
}
