package Entity;

import MAIN.GamePanel;
import MAIN.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    // Posição fixa do jogador na tela (centro)
    public final int screenX;
    public final int screenY;

    // Contador de chaves coletadas
    public int hasKey = 0;

    // Construtor do jogador
    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.gp = gp;
        this.keyH = keyH;

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
        loadPlayerImages();
    }

    public void setValoresIniciais() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        directin = "down"; // Corrigido
        hasKey = 0;
    }

    private void loadPlayerImages() {
        try {
            up1 = loadImage("/player/arqueiroCima-1.png");
            up2 = loadImage("/player/arqueiroCima-2.png");
            down1 = loadImage("/player/arqueiroBai-1.png");
            down2 = loadImage("/player/arqueiroBai-2.png");
            left1 = loadImage("/player/arqueiroEsq-1.png");
            left2 = loadImage("/player/arqueiroEsq-3.png");
            right1 = loadImage("/player/arqueiroDir-1.png");
            right2 = loadImage("/player/arqueiroDir-3.png");
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Erro ao carregar as imagens do jogador: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private BufferedImage loadImage(String path) throws IOException {
        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) {
            throw new IllegalArgumentException("Imagem não encontrada: " + path);
        }
        return ImageIO.read(is);
    }

    public void update() {
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {

            if (keyH.upPressed) directin = "up";
            else if (keyH.downPressed) directin = "down";
            else if (keyH.leftPressed) directin = "left";
            else if (keyH.rightPressed) directin = "right";

            collisiOn = false;
            gp.cChecker.checkTile(this);

            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            if (!collisiOn) {
                switch (directin) {
                    case "up" -> worldY -= speed;
                    case "down" -> worldY += speed;
                    case "right" -> worldX += speed;
                    case "left" -> worldX -= speed;
                }
            }

            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }

        // Dispara flecha ao pressionar espaço
        if (keyH.spacePressed) {
            dispararFlecha();
            keyH.spacePressed = false;
        }
    }

    // ✅ Flecha nasce exatamente na mesma posição do jogador
    public void dispararFlecha() {
        int flechaX = worldX;
        int flechaY = worldY;

        for (int i = 0; i < gp.flechas.length; i++) {
            if (gp.flechas[i] == null || !gp.flechas[i].ativa) {
                gp.flechas[i] = new Flecha(gp, flechaX, flechaY, directin);
                gp.playSE(4);
                break;
            }
        }
    }

    public void pickUpObject(int i) {
        if (i != 999) {
            String objectName = gp.obj[i].name;
            switch (objectName) {
                case "Key" -> {
                    gp.playSE(1);
                    hasKey++;
                    gp.obj[i] = null;
                    gp.ui.showMessage("Você tem uma chave!");
                }
                case "Door" -> {
                    if (hasKey > 0) {
                        gp.playSE(3);
                        gp.obj[i] = null;
                        hasKey--;
                        gp.ui.showMessage("Você abre a porta!");
                    } else {
                        gp.ui.showMessage("Você precisa de uma chave!");
                    }
                }
                case "Boots" -> {
                    gp.playSE(2);
                    speed += 1;
                    gp.obj[i] = null;
                    gp.ui.showMessage("Você recebe os botas!");
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (directin) {
            case "up" -> image = (spriteNum == 1) ? up1 : up2;
            case "down" -> image = (spriteNum == 1) ? down1 : down2;
            case "left" -> image = (spriteNum == 1) ? left1 : left2;
            case "right" -> image = (spriteNum == 1) ? right1 : right2;
        }

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}
