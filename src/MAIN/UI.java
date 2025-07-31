package MAIN;

import Telas.GameOverScreen;
import object.OBJ_Key;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {

    // Constantes de configuração
    private static final int MESSAGE_DURATION      = 120;
    private static final int LIFE_BAR_TILES_WIDTH = 4;
    private static final int LIFE_BAR_HEIGHT       = 20;
    private static final int HELP_PANEL_PADDING    = 50;
    private static final int HELP_PANEL_HEIGHT     = 200;

    // Fontes pré-derivadas
    private final Font fontArial40      = new Font("Arial", Font.PLAIN, 40);
    private final Font fontArial32      = fontArial40.deriveFont(32F);
    private final Font fontArial30      = fontArial40.deriveFont(30F);
    private final Font fontArial24      = fontArial40.deriveFont(24F);
    private final Font fontArial20      = fontArial40.deriveFont(20F);
    private final Font fontArialBold20  = new Font("Arial", Font.BOLD, 20);

    // Estado geral
    public boolean gameOverOn = false;
    public GameOverScreen gameOverScreen;

    // Referência ao painel principal
    private GamePanel gp;
    private BufferedImage keyImage;

    // Mensagem temporária
    public boolean messageOn = false;
    private String message = "";
    private int messageCounter = 0;

    // Menu de pausa
    public boolean pauseMenuOn = false;
    public String[] pauseOptions = { "Retornar", "Reiniciar", "Sair", "Ajuda" };
    public int selectedOption = 0;

    // Controle do painel de ajuda
    private boolean showHelpPanel = false;

    public UI(GamePanel gp) {
        this.gp = gp;
        OBJ_Key key = new OBJ_Key();
        keyImage = key.image;
        gameOverScreen = new GameOverScreen(gp);
    }

    /** Chama para alternar (mostrar/ocultar) o painel de ajuda */
    public void toggleHelpPanel() {
        showHelpPanel = !showHelpPanel;
    }

    /** Se sair do menu de pausa, garante painel de ajuda fechado */
    public void resetHelpPanel() {
        showHelpPanel = false;
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
        messageCounter = 0;
    }

    public void draw(Graphics2D g2) {
        g2.setFont(fontArial40);
        g2.setColor(Color.WHITE);

        if (gameOverOn) {
            gameOverScreen.draw(g2);
            return;
        }

        drawHUD(g2);

        if (messageOn) drawMessage(g2);

        if (pauseMenuOn) drawPauseMenu(g2);
    }

    private void drawHUD(Graphics2D g2) {
        // Chave
        g2.drawImage(keyImage,
                gp.tileSize / 2,
                gp.tileSize / 2,
                gp.tileSize,
                gp.tileSize,
                null);
        g2.drawString("x " + gp.player.hasKey, 75, 65);

        // Barra de vida
        drawLifeBar(g2);
    }

    private void drawMessage(Graphics2D g2) {
        g2.setFont(fontArial30);
        g2.drawString(message,
                gp.tileSize / 2,
                gp.tileSize * 6);

        messageCounter++;
        if (messageCounter > MESSAGE_DURATION) {
            messageOn = false;
            messageCounter = 0;
        }
    }

    private void drawLifeBar(Graphics2D g2) {
        int width  = gp.tileSize * LIFE_BAR_TILES_WIDTH;
        int x      = gp.screenWidth - width - 10;
        int y      = gp.tileSize + 10;

        // Texto de nível
        g2.setFont(fontArial20);
        g2.setColor(Color.CYAN);
        g2.drawString("Nível: " + gp.player.nivel, x + 5, y - 10);

        // Fundo da barra
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(x, y, width, LIFE_BAR_HEIGHT);

        // Preenchimento proporcional
        double ratio = (double) gp.player.vida / gp.player.vidaMaxima;
        int fillW = (int) (width * ratio);

        if (ratio > 0.6)      g2.setColor(Color.GREEN);
        else if (ratio > 0.3) g2.setColor(Color.ORANGE);
        else                  g2.setColor(Color.RED);

        g2.fillRect(x, y, fillW, LIFE_BAR_HEIGHT);

        // Borda e texto
        g2.setColor(Color.WHITE);
        g2.drawRect(x, y, width, LIFE_BAR_HEIGHT);

        g2.setFont(fontArial20);
        g2.drawString("Vida: " + gp.player.vida + " / " + gp.player.vidaMaxima,
                x + 5,
                y + LIFE_BAR_HEIGHT - 3);
    }

    private void drawPauseMenu(Graphics2D g2) {
        // Overlay escuro
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Título e instrução
        g2.setFont(fontArial40);
        g2.setColor(Color.WHITE);
        g2.drawString("PAUSA",
                gp.screenWidth / 2 - 70,
                gp.tileSize * 3);

        g2.setFont(fontArial24);
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawString("Aperte espaço para escolher",
                gp.screenWidth / 2 - 140,
                gp.tileSize * 4);

        // Opções de menu
        g2.setFont(fontArial32);
        for (int i = 0; i < pauseOptions.length; i++) {
            g2.setColor(i == selectedOption ? Color.YELLOW : Color.WHITE);
            g2.drawString(pauseOptions[i],
                    gp.screenWidth / 2 - 100,
                    gp.tileSize * (6 + i));
        }

        // Painel de ajuda (só se showHelpPanel == true)
        if (showHelpPanel) {
            int panelW = gp.screenWidth - HELP_PANEL_PADDING * 2;
            int panelH = HELP_PANEL_HEIGHT;
            int panelX = HELP_PANEL_PADDING;
            int panelY = gp.screenHeight - panelH - HELP_PANEL_PADDING;

            g2.setColor(new Color(30, 30, 30, 220));
            g2.fillRoundRect(panelX, panelY, panelW, panelH, 20, 20);

            g2.setFont(fontArial20);
            g2.setColor(Color.WHITE);
            int textX = panelX + 20;
            int textY = panelY + 35;

            g2.drawString("→ W/A/S/D: mover o personagem",        textX, textY);
            g2.drawString("→ Espaço: disparar flechas contra inimigos", textX, textY + 30);
            g2.drawString("→ P: pausar/despausar o jogo",            textX, textY + 60);

            g2.setFont(fontArialBold20);
            g2.drawString("Objetivo:", textX, textY + 100);

            g2.setFont(fontArial20);
            g2.drawString("Colete chaves para abrir caminhos e",     textX, textY + 130);
            g2.drawString("chegue ao baú no mapa. Cuidado com monstros!", textX, textY + 160);
        }
    }
}
