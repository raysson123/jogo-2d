package Entity;

import MAIN.GamePanel;
import util.Spritesheet;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Lobo extends Inimigo {

    private BufferedImage[] upCaminhado, downCaminhado, leftCaminhado, rightCaminhado;
    private BufferedImage[] upCorrendo, downCorrendo, leftCorrendo, rightCorrendo;
    private BufferedImage[] upParado, downParado, leftParado, rightParado;

    private int frameIndex = 0, frameCounter = 0;
    private static final int FRAMES_PER_UPDATE = 10;
    private static final int SPRITE_COUNT = 3;

    private static final int TEMPO_DESCANSO = 120;
    private boolean descansando = false;
    private int contadorDescanso = 0;

    private String[] direcoesDescanso = new String[2];
    private int contadorTrocaDirecao = 0;
    private static final int INTERVALO_TROCA_DIRECAO = 30;

    private boolean estaCorrendo = false;

    private boolean patrulhouCima = false;
    private boolean patrulhouBaixo = false;
    private boolean patrulhouEsquerda = false;
    private boolean patrulhouDireita = false;

    public Lobo(GamePanel gp) {
        super(gp);
        carregarSprites();
        setValoresIniciais();
    }

    @Override
    public void setValoresIniciais() {
        super.setValoresIniciais();
        speed = 2;
        vida = 5;
        descansando = false;
        contadorDescanso = 0;
        contadorTrocaDirecao = 0;
        estaCorrendo = false;
        resetDirecoesPatrulhadas();
    }

    private void carregarSprites() {
        upCaminhado    = carregarSet("up", "caminhado");
        downCaminhado  = carregarSet("down", "caminhado");
        leftCaminhado  = carregarSet("left", "caminhado");
        rightCaminhado = carregarSet("right", "caminhado");

        upCorrendo     = carregarSet("up", "correndo");
        downCorrendo   = carregarSet("down", "correndo");
        leftCorrendo   = carregarSet("left", "correndo");
        rightCorrendo  = carregarSet("right", "correndo");

        upParado       = carregarSet("up", "parado");
        downParado     = carregarSet("down", "parado");
        leftParado     = carregarSet("left", "parado");
        rightParado    = carregarSet("right", "parado");
    }

    private BufferedImage[] carregarSet(String direcao, String tipo) {
        BufferedImage[] set = new BufferedImage[SPRITE_COUNT];
        for (int i = 0; i < SPRITE_COUNT; i++) {
            set[i] = Spritesheet.getSprite(String.format("wolf_%s_%s_%d", direcao, tipo, i + 1));
        }
        return set;
    }

    @Override
    public void update() {
        if (!ativo || morrendo) return;

        int dx = gp.player.worldX - worldX;
        int dy = gp.player.worldY - worldY;
        int distX = Math.abs(dx) / gp.tileSize;
        int distY = Math.abs(dy) / gp.tileSize;

        if (distX <= 3 && distY <= 3) {
            estaCorrendo = true;
            speed = 4;
            descansando = false;
            contadorDescanso = 0;
            contadorTrocaDirecao = 0;
            resetDirecoesPatrulhadas();
            perseguirJogador();
        } else {
            estaCorrendo = false;
            speed = 2;

            if (descansando) {
                contadorDescanso++;
                contadorTrocaDirecao++;

                if (contadorTrocaDirecao >= INTERVALO_TROCA_DIRECAO) {
                    directin = directin.equals(direcoesDescanso[0]) ? direcoesDescanso[1] : direcoesDescanso[0];
                    contadorTrocaDirecao = 0;
                }

                if (contadorDescanso >= TEMPO_DESCANSO) {
                    descansando = false;
                    contadorDescanso = 0;
                    contadorTrocaDirecao = 0;
                    resetPatrol();
                }

            } else {
                int etapa = patrulhar();
                if (etapa == 1) {
                    switch (directin) {
                        case "up"    -> patrulhouCima = true;
                        case "down"  -> patrulhouBaixo = true;
                        case "left"  -> patrulhouEsquerda = true;
                        case "right" -> patrulhouDireita = true;
                    }

                    if (patrulhouCima && patrulhouBaixo && patrulhouEsquerda && patrulhouDireita) {
                        descansando = true;
                        contadorDescanso = 0;
                        contadorTrocaDirecao = 0;
                        resetDirecoesPatrulhadas();

                        String[] direcoesPossiveis = {"up", "down", "left", "right"};
                        direcoesDescanso[0] = direcoesPossiveis[(int)(Math.random() * 4)];
                        String outraDirecao;
                        do {
                            outraDirecao = direcoesPossiveis[(int)(Math.random() * 4)];
                        } while (outraDirecao.equals(direcoesDescanso[0]));
                        direcoesDescanso[1] = outraDirecao;

                        directin = direcoesDescanso[0];
                    }
                }
            }
        }

        collisiOn = false;
        gp.cChecker.checkTile(this);

        if (!collisiOn && !descansando) {
            switch (directin) {
                case "up"    -> worldY -= speed;
                case "down"  -> worldY += speed;
                case "left"  -> worldX -= speed;
                case "right" -> worldX += speed;
            }
        } else if (!descansando && speed == 2) {
            mudarDirecaoAposColisao();
        }

        frameCounter++;
        if (frameCounter >= FRAMES_PER_UPDATE) {
            frameCounter = 0;
            frameIndex = (frameIndex + 1) % SPRITE_COUNT;
        }
    }

    private void mudarDirecaoAposColisao() {
        switch (directin) {
            case "right" -> directin = "down";
            case "down"  -> directin = "left";
            case "left"  -> directin = "up";
            case "up"    -> directin = "right";
        }
    }

    private void resetDirecoesPatrulhadas() {
        patrulhouCima = false;
        patrulhouBaixo = false;
        patrulhouEsquerda = false;
        patrulhouDireita = false;
    }

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage sprite;

        if (descansando) {
            sprite = switch (directin) {
                case "up"    -> upParado[frameIndex];
                case "down"  -> downParado[frameIndex];
                case "left"  -> leftParado[frameIndex];
                case "right" -> rightParado[frameIndex];
                default      -> null;
            };
        } else {
            sprite = switch (directin) {
                case "up"    -> estaCorrendo ? upCorrendo[frameIndex] : upCaminhado[frameIndex];
                case "down"  -> estaCorrendo ? downCorrendo[frameIndex] : downCaminhado[frameIndex];
                case "left"  -> estaCorrendo ? leftCorrendo[frameIndex] : leftCaminhado[frameIndex];
                case "right" -> estaCorrendo ? rightCorrendo[frameIndex] : rightCaminhado[frameIndex];
                default      -> null;
            };
        }

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (sprite != null && isVisivel() && ativo) {
            g2.drawImage(sprite, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}
