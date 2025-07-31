package util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Spritesheet {

    // Sprites do Player (Arqueiro)
    public static BufferedImage playerCima1, playerCima2;
    public static BufferedImage playerBaixo1, playerBaixo2;
    public static BufferedImage playerEsq1, playerEsq2;
    public static BufferedImage playerDir1, playerDir2;

    // Sprites das Flechas
    public static BufferedImage flechaCima;
    public static BufferedImage flechaBaixo;
    public static BufferedImage flechaEsq;
    public static BufferedImage flechaDir;

    // Sprites do Lobo (legacy)
    public static BufferedImage loboCima1, loboCima2;
    public static BufferedImage loboBaixo1, loboBaixo2;
    public static BufferedImage loboEsq1, loboEsq2;
    public static BufferedImage loboDir1, loboDir2;

    // Novo sistema de sprites por nome
    private static final Map<String, BufferedImage> sprites = new HashMap<>();

    public static void carregarSprites() {
        try {
            // Player
            playerCima1 = carregar("/player/arqueiroCima-1.png");
            playerCima2 = carregar("/player/arqueiroCima-2.png");
            playerBaixo1 = carregar("/player/arqueiroBai-1.png");
            playerBaixo2 = carregar("/player/arqueiroBai-2.png");
            playerEsq1 = carregar("/player/arqueiroEsq-1.png");
            playerEsq2 = carregar("/player/arqueiroEsq-2.png");
            playerDir1 = carregar("/player/arqueiroDir-1.png");
            playerDir2 = carregar("/player/arqueiroDir-2.png");

            // Flechas
            flechaCima = carregar("/flechas/flechaCima.png");
            flechaBaixo = carregar("/flechas/flechaBai.png");
            flechaEsq = carregar("/flechas/flechaEsq.png");
            flechaDir = carregar("/flechas/flechaDir.png");

            // Lobo (antigo)
            loboCima1 = carregar("/inimigos/LOBOCIMA1.png");
            loboCima2 = carregar("/inimigos/LOBOCIMA2.png");
            loboBaixo1 = carregar("/inimigos/LOBOBAI1.png");
            loboBaixo2 = carregar("/inimigos/LOBOBAI2.png");
            loboEsq1 = carregar("/inimigos/LOBOESQ1.png");
            loboEsq2 = carregar("/inimigos/LOBOESQ2.png");
            loboDir1 = carregar("/inimigos/LOBODIR1.png");
            loboDir2 = carregar("/inimigos/LOBODIR2.png");

            // Novo: carregar sprites do lobo dinamicamente
            String[] estados = {"caminhado", "correndo", "parado"};
            String[] direcoes = {"up", "down", "left", "right"};

            for (String estado : estados) {
                for (String direcao : direcoes) {
                    for (int i = 1; i <= 3; i++) {
                        String nome = "wolf_" + direcao + "_" + estado + "_" + i;
                        String caminho = "/inimigos/" + nome.toUpperCase() + ".png";
                        sprites.put(nome, carregar(caminho));
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Erro ao carregar sprites: " + e.getMessage());
        }
    }

    private static BufferedImage carregar(String path) throws IOException {
        var is = Spritesheet.class.getResourceAsStream(path);
        if (is == null) {
            throw new IOException("Imagem não encontrada: " + path);
        }
        return ImageIO.read(is);
    }

    public static BufferedImage getSprite(String nome) {
        BufferedImage sprite = sprites.get(nome);
        if (sprite == null) {
            System.err.println("Sprite não encontrado: " + nome);
        }
        return sprite;
    }
}
