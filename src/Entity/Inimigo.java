package Entity;

import MAIN.GamePanel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Inimigo extends Entity {

    public final int screenX;
    public final int screenY;

    int movimentoPatrulhaCounter = 0;
    String direcaoPatrulha = "right";

    private int vida = 3;
    private boolean morrendo = false;
    private int contadorDesaparecimento = 0;
    private boolean visivel = true;

    public Inimigo(GamePanel gp) {
        super(gp); // ✅ chama o construtor da superclasse Entity
        this.gp = gp;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        setValoresIniciais();
        carregarImagensInimigo();
    }

    public void setValoresIniciais() {
        worldX = gp.tileSize * 10;
        worldY = gp.tileSize * 10;
        speed = 1;
        directin = "down"; // ✅ corrigido
    }

    private void carregarImagensInimigo() {
        try {
            up1 = carregarImagem("/inimigos/cobra2-esq.png");
            up2 = carregarImagem("/inimigos/cobra1-esq.png");
            down1 = carregarImagem("/inimigos/cobra2-dir.png");
            down2 = carregarImagem("/inimigos/cobra1-dir.png");
            left1 = carregarImagem("/inimigos/cobra1-esq.png");
            left2 = carregarImagem("/inimigos/cobra2-esq.png");
            right1 = carregarImagem("/inimigos/cobra1-dir.png");
            right2 = carregarImagem("/inimigos/cobra2-dir.png");
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Erro ao carregar as imagens do inimigo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private BufferedImage carregarImagem(String caminho) throws IOException {
        InputStream is = getClass().getResourceAsStream(caminho);
        if (is == null) throw new IllegalArgumentException("Imagem não encontrada: " + caminho);
        return ImageIO.read(is);
    }

    public void sofrerDano(int dano) {
        if (!morrendo) {
            vida -= dano;
            if (vida <= 0) {
                morrendo = true;
                contadorDesaparecimento = 60;
            }
        }
    }

    public void update() {
        if (morrendo) {
            if (contadorDesaparecimento > 0) {
                contadorDesaparecimento--;
                if (contadorDesaparecimento % 10 == 0) {
                    visivel = !visivel;
                }
            } else {
                visivel = false;
            }
            return;
        }

        int dx = gp.player.worldX - worldX;
        int dy = gp.player.worldY - worldY;

        int distanciaTileX = Math.abs(dx) / gp.tileSize;
        int distanciaTileY = Math.abs(dy) / gp.tileSize;

        if (distanciaTileX <= 3 && distanciaTileY <= 3) {
            if (Math.abs(dx) > Math.abs(dy)) {
                directin = (dx > 0) ? "right" : "left";
            } else {
                directin = (dy > 0) ? "down" : "up";
            }

        } else {
            movimentoPatrulhaCounter++;
            if (movimentoPatrulhaCounter >= 60) {
                movimentoPatrulhaCounter = 0;
                switch (direcaoPatrulha) {
                    case "right" -> direcaoPatrulha = "down";
                    case "down" -> direcaoPatrulha = "left";
                    case "left" -> direcaoPatrulha = "up";
                    case "up" -> direcaoPatrulha = "right";
                }
            }
            directin = direcaoPatrulha;
        }

        collisiOn = false; // ✅ corrigido
        gp.cChecker.checkTile(this);

        if (!collisiOn) {
            switch (directin) {
                case "up" -> worldY -= speed;
                case "down" -> worldY += speed;
                case "left" -> worldX -= speed;
                case "right" -> worldX += speed;
            }
        }

        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        if (!visivel) return;

        BufferedImage imagem = switch (directin) {
            case "up" -> (spriteNum == 1) ? up1 : up2;
            case "down" -> (spriteNum == 1) ? down1 : down2;
            case "left" -> (spriteNum == 1) ? left1 : left2;
            case "right" -> (spriteNum == 1) ? right1 : right2;
            default -> null;
        };

        g2.drawImage(imagem,
            worldX - gp.player.worldX + gp.player.screenX,
            worldY - gp.player.worldY + gp.player.screenY,
            gp.tileSize, gp.tileSize, null);
    }
}
