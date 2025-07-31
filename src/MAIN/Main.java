package MAIN;

import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class  Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define a operação de fechar a janela quando o usuário clica no 'X'
        window.setResizable(false); // Impede que a janela seja redimensionada pelo usuário
        window.setTitle("Meu Jogo 2D"); // Define o título que aparece na barra superior da janela

        GamePanel gamePanel = new GamePanel(); // Cria uma instância do nosso painel de jogo personalizado
        window.add(gamePanel); // Adiciona o painel do jogo à janela

        window.pack(); // Ajusta o tamanho da janela para acomodar todos os componentes dentro dela (neste caso, o MAIN.GamePanel)

        window.setLocationRelativeTo(null); // Centraliza a janela na tela do usuário
        window.setVisible(true); // Torna a janela visível para o usuário

        gamePanel.setupGame(); // Configura o jogo antes de iniciá-lo
        gamePanel.startGameThread(); // Inicia a thread principal do jogo, onde a lógica e o desenho ocorrerão
    }
}