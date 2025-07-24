package Entity;

import MAIN.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Flecha extends Entity {

    public boolean ativa = true;

    public int tempoAceleracao = 0;
    public final int tempoMaximoAntesDeAcelerar = 40;   // tepode  de retado de rederisaçãom

    public Flecha(GamePanel gp, int startX, int startY, String direction) {
        super(gp);
        this.worldX = startX;
        this.worldY = startY;
        this.directin = direction;
        this.speed = 1;

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 8;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        carregarSprites();
    }


    private void carregarSprites() {
        try {
            up1 = carregarImagem("/flechas/flechaCima.png");
            down1 = carregarImagem("/flechas/flechaBai.png");
            left1 = carregarImagem("/flechas/flechaEsq.png");
            right1 = carregarImagem("/flechas/flechaDir.png");

            up2 = up1;
            down2 = down1;
            left2 = left1;
            right2 = right1;
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Erro ao carregar sprites da flecha: " + e.getMessage());
        }
    }

    private BufferedImage carregarImagem(String path) throws IOException {
        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) {
            throw new IllegalArgumentException("Imagem não encontrada: " + path);
        }
        return ImageIO.read(is);
    }

    public void update() {
        if (!ativa) return;
        if (tempoAceleracao < tempoMaximoAntesDeAcelerar) {
            tempoAceleracao++;
        } else {
            speed = 4; // Acelera após X frames
        }
        collisiOn = false; // ✅ Corrigido (antes: collisiOn)
        gp.cChecker.checkTile(this);

        collisiOn = false;
        gp.cChecker.checkTile(this);

        // ✅ Verifica colisão com inimigos
        int index = gp.cChecker.checkInimigo(this);
        if (index != -1) {
            gp.inimigos[index].sofrerDano(1); // tira 1 de vida
            if (gp.inimigos[index].vida <= 0) {
                gp.player.xp += 3; // dá 3 de XP ao jogador
            }
            ativa = false; // flecha desaparece após atingir
            return;
        }

        if (!collisiOn) {
            switch (directin) {
                case "up" -> worldY -= speed;
                case "down" -> worldY += speed;
                case "left" -> worldX -= speed;
                case "right" -> worldX += speed;
            }
        } else {
            ativa = false;
        }

        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }
    public void draw(Graphics2D g2) {
        if (!ativa) return;

        BufferedImage image = switch (directin) {
            case "up" -> up1;
            case "down" -> down1;
            case "left" -> left1;
            case "right" -> right1;
            default -> null;
        };

        if (image == null) return; // evita erro se imagem não carregou

        // Calcula posição relativa ao jogador
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}