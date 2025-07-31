package Entity;

import MAIN.GamePanel;
import util.Spritesheet;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Flecha extends Entity {

    public boolean ativa = true;
    public int tempoAceleracao = 0;
    public final int tempoMaximoAntesDeAcelerar = 40;

    public Flecha(GamePanel gp, int startX, int startY, String direction) {
        super(gp);
        this.worldX = startX;
        this.worldY = startY;
        this.directin = direction;
        this.speed = 6;

        // 🎯 Ajusta a hitbox conforme a direção da flecha
        solidArea = new Rectangle();
        switch (directin) {
            case "left", "right" -> {
                solidArea.x = 8;
                solidArea.y = 14;
                solidArea.width = 32;
                solidArea.height = 12;
            }
            case "up", "down" -> {
                solidArea.x = 14;
                solidArea.y = 8;
                solidArea.width = 12;
                solidArea.height = 32;
            }
            default -> {
                solidArea.x = 8;
                solidArea.y = 8;
                solidArea.width = 32;
                solidArea.height = 32;
            }
        }
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public void update() {
        if (!ativa) return;

        collisiOn = false;
        gp.cChecker.checkTile(this);

        // Verifica colisão com inimigos
        int index = gp.cChecker.checkInimigo(this);
        if (index != -1) {
            Inimigo inimigo = (Inimigo) gp.inimigos.get(index);
            if (inimigo.ativo) {
                inimigo.sofrerDano(1);
                if (inimigo.vida <= 0) {
                    inimigo.ativo = false;
                    gp.player.ganharXP(9);
                }
                ativa = false;
                return;
            }
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
            case "up" -> Spritesheet.flechaCima;
            case "down" -> Spritesheet.flechaBaixo;
            case "left" -> Spritesheet.flechaEsq;
            case "right" -> Spritesheet.flechaDir;
            default -> null;
        };

        if (image == null) return;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // 🎯 Desenha a imagem da flecha
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

        // 🟥⬜ Desenha a hitbox da flecha
       // int hitboxX = screenX + solidArea.x;
       // int hitboxY = screenY + solidArea.y;

       // g2.setColor(new Color(255, 255, 255, 100));
      //  g2.fillRect(hitboxX, hitboxY, solidArea.width, solidArea.height);
      //  g2.setColor(Color.RED);
     //   g2.drawRect(hitboxX, hitboxY, solidArea.width, solidArea.height);
    }
}
