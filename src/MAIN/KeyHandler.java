package MAIN;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean spacePressed; // ✅ novo campo para disparar flecha

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

        // ✅ Navegação na tela inicial
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

            if (code == KeyEvent.VK_SPACE) {
                gp.telaInicial.select();
            }

            return; // ✅ Impede que outras teclas afetem o jogo enquanto na tela inicial
        }

        // ✅ Abrir ou fechar menu de pausa com ENTER
        if (code == KeyEvent.VK_ENTER) {
            gp.ui.pauseMenuOn = !gp.ui.pauseMenuOn;
        }

        // ✅ Navegação no menu de pausa
        if (gp.ui.pauseMenuOn) {
            if (code == KeyEvent.VK_W) {
                gp.ui.selectedOption--;
                if (gp.ui.selectedOption < 0) gp.ui.selectedOption = gp.ui.pauseOptions.length - 1;
            }

            if (code == KeyEvent.VK_S) {
                gp.ui.selectedOption++;
                if (gp.ui.selectedOption >= gp.ui.pauseOptions.length) gp.ui.selectedOption = 0;
            }

            if (code == KeyEvent.VK_SPACE) {
                switch (gp.ui.selectedOption) {
                    case 0 -> gp.ui.pauseMenuOn = false;
                    case 1 -> gp.restartGame();
                    case 2 -> System.exit(0);
                    case 3 -> gp.ui.showMessage("Use W/S para navegar e espaço para escolher.");
                }
            }

            return; // ✅ Impede que outras teclas afetem o jogo enquanto no menu de pausa
        }

        // ✅ Movimentação do jogador
        if (code == KeyEvent.VK_W) upPressed = true;
        if (code == KeyEvent.VK_S) downPressed = true;
        if (code == KeyEvent.VK_A) leftPressed = true;
        if (code == KeyEvent.VK_D) rightPressed = true;

        // ✅ Disparo de flecha
        if (code == KeyEvent.VK_SPACE) spacePressed = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;
        if (code == KeyEvent.VK_SPACE) spacePressed = false; // ✅ libera espaço
    }
}
