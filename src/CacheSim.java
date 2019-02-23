public class CacheSim {
    static int MM[] = new int[2048];
    static int cache[][]= new int[0x10][0x15];
    //static String cacheHead[]= new String []{"slot ", "dirty ", "valid ", "tag ", "data"};
    static int [] readArray= new int[18];
    static int [] writeArray= new int[5];
    static char [] directionsArray = new char [25];




    public static void main(String[] args) {
        setUpArrays();
fillMainMemory();
printHeader();
//printCache();



}


    public static void fillMainMemory() {


        //This part fills in 0-ff 7 times in MM*************************************
        for (int slot = 0; slot <= 7; slot++) {
            for (int i = 0; i <= 0xff; i++) {//if I make this 0x7ff it will show the address of the whole thing in hex******
                MM[i] = i;

            }
        }
    }


public static void setUpArrays() {
        int[] readArray = {0x005, 0x006, 0x007, 0x14c, 0x14d, 0x14e, 0x14f, 0x150, 0x151, 0x3a6, 0x4c3, 0x582, 0x348, 0x3f, 0x14b, 0x14c, 0x63f, 0x83};
        int[] writeArray = {0x14c, 0x99, 0x63B, 0x7};
        char[] directionsArray = {'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'D', 'W', 'W', 'R', 'D', 'R', 'R', 'D', 'R', 'R', 'R', 'R', 'D'};
        String[] cacheHead = {"slot ", "dirty ", "valid ", "tag ", "data"};
    }

    public static void printHeader() {
        for (int h = 0; h <= 4; h++) {
            System.out.print(cacheHead[h]);
        }
        System.out.println();

    }

public static void printCache() {

    for (int i = 0x0; i <= 0xf; i++) {

        for (int j = 0x0; j <= 0x14; j++) {
            System.out.printf("%2x", cache[i][j]);


        }
        System.out.println();


    }}}


