package MAIN;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class TelaInicial {

    GamePanel gp;
    public String[] options = {"Iniciar Jogo", "Ajuda", "Sobre", "Créditos", "Sair do jogo"};

    public int selectedOption = 0;
    public boolean active = true;
    public boolean showingHelp = false;
    private BufferedImage titleImage;

    public TelaInicial(GamePanel gp) {
        this.gp = gp;
        loadTitleImage();
    }

    private void loadTitleImage() {
        try {
            InputStream is = getClass().getResourceAsStream("/telas/telainicial.png");
            titleImage = ImageIO.read(is);
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem da tela inicial: " + e.getMessage());
        }
    }

    public void draw(Graphics2D g2) {
        if (!active) return;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // ✅ Desenha imagem centralizada na tela
        if (titleImage != null) {
            int imgX = gp.screenWidth / 2 - titleImage.getWidth() / 2;
            int imgY = gp.screenHeight / 2 - titleImage.getHeight() / 2;
            g2.drawImage(titleImage, imgX, imgY, null);
        }

        if (showingHelp) {
            drawHelp(g2);
            return;
        }

        g2.setFont(new Font("Arial", Font.PLAIN, 32));
        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                g2.setColor(Color.YELLOW);
            } else {
                g2.setColor(Color.WHITE);
            }
            g2.drawString(options[i], gp.screenWidth / 2 - 100, gp.tileSize * (6 + i));
        }

        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawString("Use W/S para navegar e espaço para escolher", gp.screenWidth / 2 - 180, gp.screenHeight - 40);
    }

    private void drawHelp(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.PLAIN, 28));
        g2.setColor(Color.WHITE);
        g2.drawString("Guia de Controles:", gp.tileSize * 2, gp.tileSize * 3);
        g2.setFont(new Font("Arial", Font.PLAIN, 22));
        g2.drawString("W/S/A/D - Movimentar o personagem", gp.tileSize * 2, gp.tileSize * 4);
        g2.drawString("Enter - Abrir menu de pausa", gp.tileSize * 2, gp.tileSize * 5);
        g2.drawString("Espaço - Confirmar opção no menu", gp.tileSize * 2, gp.tileSize * 6);
        g2.drawString("ESC - Sair do jogo (via menu)", gp.tileSize * 2, gp.tileSize * 7);
        g2.drawString("Pressione Espaço para voltar", gp.tileSize * 2, gp.tileSize * 9);
    }

    public void select() {
        if (showingHelp) {
            showingHelp = false;
            return;
        }

        switch (selectedOption) {
            case 0: active = false; break; // Iniciar Jogo
            case 1: showingHelp = true; break; // Ajuda
            case 2: gp.ui.showMessage("Jogo feito por Alexsander com muito estilo!"); break; // Sobre
            case 3: gp.ui.showMessage("Programação: Alexsander\nArte: Alexsander\nIdeia: Alexsander"); break; // Créditos
            case 4: System.exit(0); break; // ✅ Sair do jogo
        }
    }

}
