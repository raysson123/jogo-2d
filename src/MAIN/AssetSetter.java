package MAIN;

import Entity.Inimigo;
import Entity.Lobo;
import object.OBJ_Boots;
import object.OBJ_Chest;
import object.OBJ_Door;
import object.OBJ_Key;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        OBJ_Key key1 = new OBJ_Key();
        key1.worldX = gp.tileSize * 23;
        key1.worldY = gp.tileSize * 7;
        gp.obj.add(key1);

        OBJ_Key key2 = new OBJ_Key();
        key2.worldX = gp.tileSize * 23;
        key2.worldY = gp.tileSize * 40;
        gp.obj.add(key2);

        OBJ_Key key3 = new OBJ_Key();
        key3.worldX = gp.tileSize * 38;
        key3.worldY = gp.tileSize * 7;
        gp.obj.add(key3);

        OBJ_Door door1 = new OBJ_Door();
        door1.worldX = gp.tileSize * 10;
        door1.worldY = gp.tileSize * 11;
        gp.obj.add(door1);

        OBJ_Door door2 = new OBJ_Door();
        door2.worldX = gp.tileSize * 8;
        door2.worldY = gp.tileSize * 28;
        gp.obj.add(door2);

        OBJ_Door door3 = new OBJ_Door();
        door3.worldX = gp.tileSize * 12;
        door3.worldY = gp.tileSize * 22;
        gp.obj.add(door3);

        OBJ_Chest chest = new OBJ_Chest();
        chest.worldX = gp.tileSize * 10;
        chest.worldY = gp.tileSize * 7;
        gp.obj.add(chest);
        OBJ_Chest chest2 = new OBJ_Chest();
        chest2.worldX = gp.tileSize * 25;
        chest2.worldY = gp.tileSize * 23;
        gp.obj.add(chest2);

        OBJ_Boots boots = new OBJ_Boots();
        boots.worldX = gp.tileSize * 37;
        boots.worldY = gp.tileSize * 42;
        gp.obj.add(boots);
    }

    public void setInimigos() {
        Inimigo inimigo1 = new Inimigo(gp);
        inimigo1.worldX = gp.tileSize * 25; // 23 + 2
        inimigo1.worldY = gp.tileSize * 7;
        gp.inimigos.add(inimigo1);

        Inimigo inimigo2 = new Inimigo(gp);
        inimigo2.worldX = gp.tileSize * 23;
        inimigo2.worldY = gp.tileSize * 42; // 40 + 2
        gp.inimigos.add(inimigo2);

        Inimigo inimigo3 = new Inimigo(gp);
        inimigo3.worldX = gp.tileSize * 38;
        inimigo3.worldY = gp.tileSize * 8; // 6 + 2
        gp.inimigos.add(inimigo3);

        Inimigo inimigo4 = new Inimigo(gp);
        inimigo4.worldX = gp.tileSize * 11;
        inimigo4.worldY = gp.tileSize * 30; // 28 + 2
        gp.inimigos.add(inimigo4);

        Inimigo inimigo5 = new Inimigo(gp);
        inimigo5.worldX = gp.tileSize * 10;
        inimigo5.worldY = gp.tileSize * 30; // 28 + 2
        gp.inimigos.add(inimigo5);

        Lobo lobo = new Lobo(gp);
        lobo.worldX = gp.tileSize * 13; // 11 + 2
        lobo.worldY = gp.tileSize * 32; // 30 + 2
        gp.inimigos.add(lobo);
    }
}
