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

    // ⏳ Tempo total de exibição (10 segundos a 60 FPS)
    private final int DURACAO_TOTAL = 600;

    // 🕒 Tempo até a mensagem desaparecer (8 segundos)
    private final int DURACAO_MENSAGEM = 480;

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
        // 🖼️ Desenha o fundo
        g2.drawImage(background, 0, 0, gp.screenWidth, gp.screenHeight, null);

        // 📝 Só desenha a mensagem se ainda estiver dentro do tempo
        if (tempoExibicao < DURACAO_MENSAGEM) {
            Font fonte = new Font("Arial", Font.BOLD, 28);
            g2.setFont(fonte);
            g2.setColor(Color.WHITE);

            String[] mensagem = {
                    "O oráculo de Oneiros me avisou que você viria,",
                    "ele me pediu pra te entregar isto.",
                    "Parabéns!",
                    "Você chegou ao fim do capítulo Prequel.",
                    "Aguarde novas atualizações para o próximo capítulo.",
                    "Att: Herandy Alexsander & Raysson Lucas"
            };

            FontMetrics fm = g2.getFontMetrics(fonte);
            int alturaLinha = fm.getHeight();
            int alturaTotal = mensagem.length * alturaLinha;
            int y = (gp.screenHeight - alturaTotal) / 2 + fm.getAscent();

            for (String linha : mensagem) {
                int x = getCenteredX(g2, linha);
                g2.drawString(linha, x, y);
                y += alturaLinha;
            }
        }
    }

    public void update() {
        if (!active) return;

        tempoExibicao++;

        // ⏳ Após 10 segundos, encerra o jogo
        if (tempoExibicao >= DURACAO_TOTAL) {
            System.exit(0);
        }
    }

    // 📏 Calcula a posição X centralizada para uma string
    private int getCenteredX(Graphics2D g2, String text) {
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        return (gp.screenWidth - textWidth) / 2;
    }

    // 🚀 Ativa a tela de fim e reinicia o contador
    public void ativar() {
        active = true;
        tempoExibicao = 0;
    }
}
