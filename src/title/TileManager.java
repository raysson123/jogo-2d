package title;

import MAIN.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp; // Referência ao painel do jogo para acessar suas configurações
    public Tile[] tile; // Array para armazenar os diferentes tipos de tiles (imagens)
   public int mapTileNum[][]; // Matriz que representa o mapa do jogo (números dos tiles)

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[10]; // Ex: Supondo que você terá 10 tipos diferentes de tiles
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow]; // O mapa terá o tamanho do mundo (não da tela!)

        getTileImage(); // Carrega as imagens dos tiles
        loadMap("/maps/world01.txt"); // Carrega o mapa a partir de um arquivo (crie a pasta 'maps' e o arquivo 'world01.txt')
    }

    public void getTileImage() {
        try {
            // ----- Configuração dos Tiles -----
            // Para este exemplo, vamos carregar tiles de placeholder.
            // Em um jogo real, você substituiria isso pelas suas próprias imagens.

            // Tile 0: Grama (pode ser uma imagem verde)
            tile[0] = new Tile();
            // URL de placeholder para um quadrado verde: https://placehold.co/48x48/78C04A/000000?text=Grama
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));

            // Tile 1: Parede (pode ser uma imagem cinza/marrom)
            tile[1] = new Tile();
            // URL de placeholder para um quadrado cinza: https://placehold.co/48x48/808080/000000?text=Parede
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
            tile[1].collision = true; // Define como colisão verdadeira para evitar passar por essa tile

            // Tile 2: Água (pode ser uma imagem azul)
            tile[2] = new Tile();
            // URL de placeholder para um quadrado azul: https://placehold.co/48x48/66CCFF/000000?text=%C3%81gua
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
            tile[2].collision = true; // Define como colisão verdadeira para evitar passar por essa tile

            // Tile 3: Chão (pode ser uma imagem marrom)
            tile[3] = new Tile();
            // URL de placeholder para um quadrado marrom: https://placehold.co/48x48/996633/000000?text=Ch%C3%A3o
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/earth.png"));

            // Tile 4: Árvore (pode ser uma imagem verde com folhas)
            tile[4] = new Tile();
            // URL de placeholder para um quadrado verde com folhas: https://placehold.co/48x48/78C04A/000000?text=%C3%81rvore
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));
            tile[4].collision = true; // Define como colisão verdadeira para evitar passar por essa tile

            tile[5] = new Tile();
            // URL de placeholder para um quadrado verde com folhas: https://placehold.co/48x48/78C04A/000000?text=%C3%81rvore
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/sand.png"));



            // URL de placeholder para um quadrado verde com folhas: https://placehold.co/48x48/78C04A/00000
            // Você pode adicionar mais tiles conforme necessário (tile[3], tile[4], etc.)

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar imagens dos tiles: " + e.getMessage());
            // Opcional: Criar uma imagem de fallback para tiles que falharem no carregamento
        }
    }

    public void loadMap(String filePath) {
        try {;
            // Abre o arquivo do mapa a partir dos recursos do projeto
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            // Loop para ler o arquivo linha por linha e preencher a matriz do mapa
            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine(); // Lê uma linha do arquivo

                while (col < gp.maxWorldCol) {
                    String numbers[] = line.split(" "); // Divide a linha em números (separados por espaço)

                    int num = Integer.parseInt(numbers[col]); // Converte o número para inteiro

                    mapTileNum[col][row] = num; // Armazena o número na matriz do mapa
                    col++;
                }
                if (col == gp.maxWorldCol) {
                    col = 0; // Reseta a coluna para a próxima linha
                    row++; // Avança para a próxima linha do mapa
                }
            }
            br.close(); // Fecha o leitor do arquivo

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar o mapa: " + e.getMessage());
        }
    }

    public void draw(Graphics2D g2) {
        // Itera sobre cada tile possível no mundo
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = mapTileNum[worldCol][worldRow]; // Pega o número do tile no mapa

            // Calcula a posição do tile no mundo
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;

            // Calcula a posição do tile na tela (relativo à câmera/jogador)
            // A câmera está centrada no jogador.
            // ScreenX = posição Mundial - (posição Mundial do Jogador - posição da tela do Jogador)
            int screenX = worldX - gp.player.worldX + gp.player.screenX; // playerScreenX é a posição do jogador na tela (geralmente centro)
            int screenY = worldY - gp.player.worldY + gp.player.screenY; // playerScreenY é a posição do jogador na tela (geralmente centro)

            // SOMENTE desenha os tiles que estão visíveis na tela
            // Isso é uma otimização crucial para performance.

            g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            worldCol++;

            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}