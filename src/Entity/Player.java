package Entity;

import MAIN.GamePanel;
import MAIN.KeyHandler;
import util.Spritesheet;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    // Posição fixa do jogador na tela (centro)
    public final int screenX;
    public final int screenY;

    // Atributos do jogador
    public int hasKey = 0;
    public int xp = 0;
    public int vida = 10;
    public int vidaMaxima = 10;
    public int nivel = 1;

    // Invulnerabilidade temporária após dano
    public boolean invulneravel = false;
    private int contadorInvulnerabilidade = 0;
    private final int duracaoInvulnerabilidade = 60;

    // Controle de disparo de flechas
    private int contadorCooldownFlecha = 0;
    private final int tempoCooldownFlecha = 60;
    private final int LIMITE_FLECHAS = 5;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.gp = gp;
        this.keyH = keyH;

        // Centraliza o jogador na tela
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        // Define a área de colisão
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        setValoresIniciais();
    }

    // Define os valores iniciais do jogador
    public void setValoresIniciais() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        directin = "down";
        hasKey = 0;
        xp = 0;
        vidaMaxima = 10;
        vida = vidaMaxima;
        nivel = 1;
        contadorCooldownFlecha = 0;
    }

    // Novo método para definir posição inicial ao trocar de mapa
    public void setPosicaoInicial(int col, int row) {
        worldX = col * gp.tileSize;
        worldY = row * gp.tileSize;
    }

    // Atualiza o estado do jogador a cada frame
    public void update() {
        // Movimento
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

            // Animação de sprite
            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }

        // Cooldown de flechas
        if (contadorCooldownFlecha > 0) {
            contadorCooldownFlecha--;
        }

        // Disparo de flecha
        if (keyH.spacePressed) {
            if (contadorCooldownFlecha == 0) {
                if (getFlechasAtivas() < LIMITE_FLECHAS) {
                    dispararFlecha();
                    contadorCooldownFlecha = tempoCooldownFlecha;
                } else {
                    gp.ui.showMessage("Você já tem muitas flechas na tela!");
                }
            } else {
                gp.ui.showMessage("Aguarde para disparar novamente!");
            }
            keyH.spacePressed = false;
        }

        // Invulnerabilidade temporária
        if (invulneravel) {
            contadorInvulnerabilidade++;
            if (contadorInvulnerabilidade >= duracaoInvulnerabilidade) {
                invulneravel = false;
                contadorInvulnerabilidade = 0;
            }
        }
    }

    // Conta flechas ativas na tela
    private int getFlechasAtivas() {
        int count = 0;
        for (Flecha f : gp.flechas) {
            if (f != null && f.ativa) count++;
        }
        return count;
    }

    // Cria uma nova flecha
    public void dispararFlecha() {
        int flechaX = worldX;
        int flechaY = worldY;

        for (int i = 0; i < gp.flechas.size(); i++) {
            Flecha f = gp.flechas.get(i);
            if (f == null || !f.ativa) {
                gp.flechas.set(i, new Flecha(gp, flechaX, flechaY, directin));
                gp.playSE(5);
                return;
            }
        }

        gp.flechas.add(new Flecha(gp, flechaX, flechaY, directin));
        gp.playSE(5);
    }

    // Interação com objetos
    public void pickUpObject(int i) {
        if (i >= 0 && i < gp.obj.size()) {
            if (gp.obj.get(i) == null) return;

            String objectName = gp.obj.get(i).name;

            switch (objectName) {
                case "Key" -> {
                    gp.playSE(1);
                    hasKey++;
                    gp.obj.set(i, null);
                    gp.ui.showMessage("Você tem uma chave!");
                }
                case "Door" -> {
                    if (hasKey > 0) {
                        gp.playSE(3);
                        gp.obj.set(i, null);
                        hasKey--;
                        gp.ui.showMessage("Você abre a porta!");
                    } else {
                        gp.ui.showMessage("Você precisa de uma chave!");
                    }
                }
                case "Boots" -> {
                    gp.playSE(2);
                    speed += 1;
                    gp.obj.set(i, null);
                    gp.ui.showMessage("Você recebeu as botas!");
                }
                case "Chest" -> {
                    gp.playSE(6); // Som de baú (se existir)
                    gp.ui.showMessage("Você abriu o baú e foi transportado!");
                    gp.trocarMapa("world02"); // Troca para o mapa 2
                }
                case "NPC" -> {

                    gp.telaFim.active = true;
                }

            }
        }
    }

    // Desenha o jogador na tela
    public void draw(Graphics2D g2) {
        BufferedImage image = switch (directin) {
            case "up" -> (spriteNum == 1) ? Spritesheet.playerCima1 : Spritesheet.playerCima2;
            case "down" -> (spriteNum == 1) ? Spritesheet.playerBaixo1 : Spritesheet.playerBaixo2;
            case "left" -> (spriteNum == 1) ? Spritesheet.playerEsq1 : Spritesheet.playerEsq2;
            case "right" -> (spriteNum == 1) ? Spritesheet.playerDir1 : Spritesheet.playerDir2;
            default -> null;
        };

        // Pisca o jogador se estiver invulnerável
        if (!invulneravel || (contadorInvulnerabilidade % 10 < 5)) {
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }

        // Hitbox opcional (comentado)
        /*
        int hitboxX = screenX + solidArea.x;
        int hitboxY = screenY + solidArea.y;
        g2.setColor(new Color(255, 255, 255, 100));
        g2.fillRect(hitboxX, hitboxY, solidArea.width, solidArea.height);
        g2.setColor(Color.RED);
        g2.drawRect(hitboxX, hitboxY, solidArea.width, solidArea.height);
        */
    }

    // Ganha XP e sobe de nível
    public void ganharXP(int quantidade) {
        xp += quantidade;
        gp.ui.showMessage("Você ganhou " + quantidade + " XP!");

        int xpParaProximoNivel = nivel * 15;
        while (xp >= xpParaProximoNivel) {
            xp -= xpParaProximoNivel;
            nivel++;
            vidaMaxima += 2;
            vida = vidaMaxima;
            gp.ui.showMessage("Você subiu para o nível " + nivel + "!");
            gp.playSE(4);
            xpParaProximoNivel = nivel * 15;
        }
    }
}
