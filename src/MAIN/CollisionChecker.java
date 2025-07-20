package MAIN;

import Entity.Entity;

public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        int entityLefWorldx = entity.worldX + entity.solidArea.x;
        int entityRightWorldx = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldy = entity.worldY + entity.solidArea.y;
        int entityBottomWorldy = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLefWorldx / gp.tileSize;
        int entityRightCol = entityRightWorldx / gp.tileSize;
        int entityTopRow = entityTopWorldy / gp.tileSize;
        int entityBottomRow = entityBottomWorldy / gp.tileSize;

        int tileNum1, tileNum2;
        switch (entity.directin) {
            case "up":
                entityTopRow = (entityTopWorldy - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisiOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldy + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisiOn = true;
                }
            break;



            case "left":
                entityLeftCol = (entityLefWorldx - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisiOn = true;
                }

                break;

            case "right":
                entityRightCol = (entityRightWorldx - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisiOn = true;
                }
                break;

        }
    }
}