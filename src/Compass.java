/**
 * Created by Alastair on 23/10/2016.
 */

import java.util.Random;

public class Compass {

    public static void main(String[] args) {
    }

    public enum compassDir {
        N("N"), E("E"), S("S"), W("W");
private final String dir;

        private compassDir(String s){
            dir = s;
        }
        public String toString(){
         return this.dir;
        }
    }
    public static compassDir getCompass() {
        int pick = new Random().nextInt(compassDir.values().length);
        return compassDir.values()[pick];
    }
}
