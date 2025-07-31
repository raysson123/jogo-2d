package MAIN;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    private final GamePanel gp;
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

        // 1) Tela Inicial
        if (gp.telaInicial.active) {
            if (code == KeyEvent.VK_W) {
                gp.telaInicial.selectedOption =
                        (gp.telaInicial.selectedOption - 1 + gp.telaInicial.options.length)
                                % gp.telaInicial.options.length;
            }
            if (code == KeyEvent.VK_S) {
                gp.telaInicial.selectedOption =
                        (gp.telaInicial.selectedOption + 1) % gp.telaInicial.options.length;
            }
            if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
                gp.telaInicial.select();
            }
            return;
        }

        // 2) Tela de Game Over
        if (gp.gameState == GamePanel.GAME_OVER_STATE) {
            if (code == KeyEvent.VK_W) {
                gp.gameOverScreen.selectedOption =
                        (gp.gameOverScreen.selectedOption - 1 + gp.gameOverScreen.options.length)
                                % gp.gameOverScreen.options.length;
            }
            if (code == KeyEvent.VK_S) {
                gp.gameOverScreen.selectedOption =
                        (gp.gameOverScreen.selectedOption + 1) % gp.gameOverScreen.options.length;
            }
            if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
                switch (gp.gameOverScreen.selectedOption) {
                    case 0 -> gp.restartGame();    // Reiniciar
                    case 1 -> System.exit(0);      // Sair
                }
            }
            return;
        }

        // 3) Menu de Pausa
        if (gp.ui.pauseMenuOn) {
            if (code == KeyEvent.VK_W) {
                gp.ui.selectedOption =
                        (gp.ui.selectedOption - 1 + gp.ui.pauseOptions.length)
                                % gp.ui.pauseOptions.length;
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.selectedOption =
                        (gp.ui.selectedOption + 1) % gp.ui.pauseOptions.length;
            }
            if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
                switch (gp.ui.selectedOption) {
                    case 0 -> { // Retornar
                        gp.ui.resetHelpPanel();
                        gp.ui.pauseMenuOn = false;
                    }
                    case 1 -> { // Reiniciar
                        gp.ui.resetHelpPanel();
                        gp.restartGame();
                    }
                    case 2 -> System.exit(0);     // Sair
                    case 3 -> gp.ui.toggleHelpPanel(); // Ajuda
                }
            }
            return;
        }

        // 4) Abrir/fechar pausa com ENTER
        if (code == KeyEvent.VK_ENTER) {
            gp.ui.pauseMenuOn = !gp.ui.pauseMenuOn;
            if (!gp.ui.pauseMenuOn) {
                gp.ui.resetHelpPanel();
            }
        }

        // 5) Controles do jogador
        if (code == KeyEvent.VK_W)      upPressed = true;
        if (code == KeyEvent.VK_S)      downPressed = true;
        if (code == KeyEvent.VK_A)      leftPressed = true;
        if (code == KeyEvent.VK_D)      rightPressed = true;
        if (code == KeyEvent.VK_SPACE)  spacePressed = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W)     upPressed = false;
        if (code == KeyEvent.VK_S)     downPressed = false;
        if (code == KeyEvent.VK_A)     leftPressed = false;
        if (code == KeyEvent.VK_D)     rightPressed = false;
        if (code == KeyEvent.VK_SPACE) spacePressed = false;
    }
}
