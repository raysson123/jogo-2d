package Entity;

import MAIN.GamePanel;


import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {

    GamePanel gp; // Referência ao GamePanel para acessar suas propriedades (como tileSize).

    // Posição e Velocidade da Entidade
    public int worldX, worldY; // Coordenadas X e Y da entidade.
    public int speed; // Velocidade de movimento da entidade.

    // Sprite da Entidade
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2 ;

    public String directin;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea;

    public boolean collisiOn = false;



    // Imagem (sprite) atual da entidade.




}