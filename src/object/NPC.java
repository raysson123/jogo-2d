package object;

import javax.imageio.ImageIO;
import java.io.IOException;

public class NPC extends SuperObject{
    public NPC(){

     name = "NPC";
    try{
        image = ImageIO.read(getClass().getResourceAsStream("/npc/rei-1.png"));
    }catch(IOException e){
        e.printStackTrace();
    }
    collision = true;
}

}