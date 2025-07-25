package MAIN;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean spacePressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Não utilizado
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // ✅ Tela Inicial
        if (gp.telaInicial.active) {
            if (code == KeyEvent.VK_W) {
                gp.telaInicial.selectedOption--;
                if (gp.telaInicial.selectedOption < 0) {
                    gp.telaInicial.selectedOption = gp.telaInicial.options.length - 1;
                }
            }

            if (code == KeyEvent.VK_S) {
                gp.telaInicial.selectedOption++;
                if (gp.telaInicial.selectedOption >= gp.telaInicial.options.length) {
                    gp.telaInicial.selectedOption = 0;
                }
            }

            if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
                gp.telaInicial.select();
            }

            return;
        }

        // ✅ Tela de Game Over
        if (gp.gameState == GamePanel.GAME_OVER_STATE) {
            if (code == KeyEvent.VK_W) {
                gp.gameOverScreen.selectedOption--;
                if (gp.gameOverScreen.selectedOption < 0) {
                    gp.gameOverScreen.selectedOption = gp.gameOverScreen.options.length - 1;
                }
            }

            if (code == KeyEvent.VK_S) {
                gp.gameOverScreen.selectedOption++;
                if (gp.gameOverScreen.selectedOption >= gp.gameOverScreen.options.length) {
                    gp.gameOverScreen.selectedOption = 0;
                }
            }

            if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
                switch (gp.gameOverScreen.selectedOption) {
                    case 0 -> gp.restartGame(); // Reiniciar
                    case 1 -> System.exit(0);   // Sair
                }
            }

            return;
        }

        // ✅ Menu de Pausa
        if (gp.ui.pauseMenuOn) {
            if (code == KeyEvent.VK_W) {
                gp.ui.selectedOption--;
                if (gp.ui.selectedOption < 0) gp.ui.selectedOption = gp.ui.pauseOptions.length - 1;
            }

            if (code == KeyEvent.VK_S) {
                gp.ui.selectedOption++;
                if (gp.ui.selectedOption >= gp.ui.pauseOptions.length) gp.ui.selectedOption = 0;
            }

            if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
                switch (gp.ui.selectedOption) {
                    case 0 -> gp.ui.pauseMenuOn = false;
                    case 1 -> gp.restartGame();
                    case 2 -> System.exit(0);
                    case 3 -> gp.ui.showMessage("Use W/S para navegar e espaço para escolher.");
                }
            }

            return;
        }

        // ✅ Abrir/fechar pausa com ENTER
        if (code == KeyEvent.VK_ENTER) {
            gp.ui.pauseMenuOn = !gp.ui.pauseMenuOn;
        }

        // ✅ Controles do jogador
        if (code == KeyEvent.VK_W) upPressed = true;
        if (code == KeyEvent.VK_S) downPressed = true;
        if (code == KeyEvent.VK_A) leftPressed = true;
        if (code == KeyEvent.VK_D) rightPressed = true;
        if (code == KeyEvent.VK_SPACE) spacePressed = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;
        if (code == KeyEvent.VK_SPACE) spacePressed = false;
    }
}
