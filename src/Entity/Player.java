package Entity;

import MAIN.GamePanel;
import MAIN.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Player extends Entity {

    GamePanel gp;       // Referência ao GamePanel.
    KeyHandler keyH;    // Referência ao KeyHandler.

    public final  int screenX;
    public final int screenY;
    int hasKey = 0;
    // Construtor da classe Player.
    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();   // Define posição, velocidade e direção inicial.
        loadPlayerImages();   // Carrega as imagens do personagem.
    }

    // Define valores iniciais para o jogador.
    public void setDefaultValues() {
        worldX = gp.tileSize*23;
        worldY = gp.tileSize*21;
        speed = 4;
        directin = "down"; // Corrigir o nome da variável para "direction" futuramente.
    }

    // Carrega os sprites do jogador para cada direção e animação.
    private void loadPlayerImages() {
        try {
            up1 = loadImage("/player/link1_up_1.png");
            up2 = loadImage("/player/link1_up_2.png");
            down1 = loadImage("/player/link1_down_1.png");
            down2 = loadImage("/player/link1_down_2.png");
            left1 = loadImage("/player/link1_left_1.png");
            left2 = loadImage("/player/link1_left_2.png");
            right1 = loadImage("/player/link1_right_1.png");
            right2 = loadImage("/player/link1_right_2.png");


        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Erro ao carregar as imagens do jogador: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método utilitário para carregar uma imagem a partir do caminho fornecido.
    private BufferedImage loadImage(String path) throws IOException {
        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) {
            throw new IllegalArgumentException("Imagem não encontrada: " + path);
        }
        return ImageIO.read(is);
    }

    // Atualiza a posição e o sprite do jogador com base nas teclas pressionadas.
    public void update() {
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {

            if (keyH.upPressed) {
                directin = "up";

            } else if (keyH.downPressed) {
                directin = "down";

            } else if (keyH.leftPressed) {
                directin = "left";

            } else if (keyH.rightPressed) {
                directin = "right";

            }
            //checj title collision
            collisiOn = false;
            gp.cChecker.checkTile(this);

            //checj object collision
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);


            //if collision is false, player can move
            if (collisiOn == false){
                switch (directin){
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "right": worldX += speed; break;
                    case "left": worldX -= speed; break;
                }
            }

            // Controle de animação
            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }
    public void pickUpObject(int i){
        if ( i != 999){
            String objectName = gp.obj[i].name;
            switch (objectName){
                case "Key":
                    hasKey++;
                    gp.obj[i] = null;
                    System.out.println("key:"+hasKey);
                    break;
                case "Door":
                    if (hasKey > 0){
                        gp.obj[i] = null;
                        hasKey--;
                    }
                    System.out.println("key:"+hasKey);

                    break;
            }
        }
    }


    // Desenha o jogador na tela com base na direção e no sprite atual.
    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (directin) {
            case "up":
                image = (spriteNum == 1) ? up1 : up2;
                break;
            case "down":
                image = (spriteNum == 1) ? down1 : down2;
                break;
            case "left":
                image = (spriteNum == 1) ? left1 : left2;
                break;
            case "right":
                image = (spriteNum == 1) ? right1 : right2;
                break;
        }

        g2.drawImage(image, screenX, screenY , gp.tileSize, gp.tileSize, null);
    }
}
