package MAIN;

import Entity.Inimigo;
import object.OBJ_Boots;
import object.OBJ_Chest;
import object.OBJ_Door;
import object.OBJ_Key;

public class AssetSetter {
    GamePanel gp;
    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }
    
    
    
    public void setObject(){
        gp.obj[0] = new OBJ_Key();
        gp.obj[0].worldX = gp.tileSize * 23;
        gp.obj[0].worldY = gp.tileSize * 7;

        gp.obj[1] = new OBJ_Key();
        gp.obj[1].worldX = gp.tileSize * 23;
        gp.obj[1].worldY = gp.tileSize * 40;
        
        

        gp.obj[2] = new OBJ_Key();
        gp.obj[2].worldX = gp.tileSize * 38;
        gp.obj[2].worldY = gp.tileSize * 7;

        gp.obj[3] = new OBJ_Door();
        gp.obj[3].worldX = gp.tileSize * 10;
        gp.obj[3].worldY = gp.tileSize * 11;

        gp.obj[4] = new OBJ_Door();
        gp.obj[4].worldX = gp.tileSize * 8;
        gp.obj[4].worldY = gp.tileSize * 28;

        gp.obj[5] = new OBJ_Door();
        gp.obj[5].worldX = gp.tileSize * 12;
        gp.obj[5].worldY = gp.tileSize * 22;

        gp.obj[6] = new OBJ_Chest();
        gp.obj[6].worldX = gp.tileSize * 10;
        gp.obj[6].worldY = gp.tileSize * 7;

        gp.obj[7] = new OBJ_Boots();
        gp.obj[7].worldX = gp.tileSize * 37;
        gp.obj[7].worldY = gp.tileSize * 42;


    }
    
    public void setInimigos() {
        // Inimigo perto da chave 1 (tile 23,7) -> colocamos 2 tiles à direita (X + 2)
        gp.inimigos[0] = new Inimigo(gp);
        gp.inimigos[0].worldX = gp.tileSize * (23 + 2); // 25
        gp.inimigos[0].worldY = gp.tileSize * 7;

        // Inimigo perto da chave 2 (tile 23,40) -> colocamos 2 tiles abaixo (Y + 2)
        gp.inimigos[1] = new Inimigo(gp);
        gp.inimigos[1].worldX = gp.tileSize * 23;
        gp.inimigos[1].worldY = gp.tileSize * (40 + 2); // 42


        // Inimigo perto da chave 2 (tile 23,40) -> colocamos 2 tiles abaixo (Y + 2)
        gp.inimigos[2] = new Inimigo(gp);
        gp.inimigos[2].worldX = gp.tileSize * 38;
        gp.inimigos[2].worldY = gp.tileSize * (6 + 2); // 42

        // Inimigo perto da chave 2 (tile 23,40) -> colocamos 2 tiles abaixo (Y + 2)
        gp.inimigos[3] = new Inimigo(gp);
        gp.inimigos[3].worldX = gp.tileSize * 11;
        gp.inimigos[3].worldY = gp.tileSize * (28 + 2); // 42

        // Inimigo perto da chave 2 (tile 23,40) -> colocamos 2 tiles abaixo (Y + 2)
        gp.inimigos[4] = new Inimigo(gp);
        gp.inimigos[4].worldX = gp.tileSize * 10;
        gp.inimigos[4].worldY = gp.tileSize * (28 + 2); // 42

    }
}
