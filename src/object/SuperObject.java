package object;

import MAIN.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperObject {
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX = 0; // Valor padrão para a área sólida do objeto.
    public int solidAreaDefaultY = 0;
    public void draw(Graphics2D g2, GamePanel gp){
        int screenX = worldX - gp.player.worldX + gp.player.screenX; // playerScreenX é a posição do jogador na tela (geralmente centro)
        int screenY = worldY - gp.player.worldY + gp.player.screenY; // playerScreenY é a posição do jogador na tela (geralmente centro)

        // SOMENTE desenha os tiles que estão visíveis na tela
        // Isso é uma otimização crucial para performance.
        if( worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            // Desenha o tile na tela
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}
