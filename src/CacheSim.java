public class CacheSim {
    static int MM[] = new int[2048];
    static int cache[][] = new int[0x10][0x19];
    static String cacheHead[] = new String[]{"slot ", "dirty ", "valid ", "tag ", "data"};
    static int[] addresses = new int[]{0x005, 0x006, 0x007, 0x14c, 0x14d, 0x14e, 0x14f, 0x150, 0x151, 0x3a6, 0x4c3, 0x000, 0x14c, 0x63b, 0x582, 0x000, 0x348, 0x03f, 0x000, 0x14b, 0x14c, 0x63f, 0x083,0x000};
    static int a = 0;
    static int[] writeArray = {0x99, 0x7};
    static int w = 0;
    static char[] directionsArray = new char[]{'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'D', 'W', 'W', 'R', 'D', 'R', 'R', 'D', 'R', 'R', 'R', 'R', 'D'};
    static int d = 0;
    static int slot = 0;
    static int offset = 0;
    static int tag = 0;
    static int blockStart = 0;
    static int j;
    static int dirty = 0;
    static int valid = 0;
    static String hitOrMiss = "miss";


    public static void main(String[] args) {

        fillMainMemory();

        for (a = 0; a < directionsArray.length; a++) {
            runProgram();
        }
    }


    public static void fillMainMemory() {
        int mmFillWholeList = 0x00;
        //This part fills in 0-ff 7 times in MM*************************************
        for (int repeatedFill = 0; repeatedFill <= 0x7; repeatedFill++) {
            for (int i = 0x00; i <= 0xff; i++) {//if I make this 0x7ff it will show the address of the whole thing in hex******
                MM[mmFillWholeList] = i;
                mmFillWholeList++;
            }
        }
    }


    public static void runProgram() {
        switch (directionsArray[d]) {
            case 'R':
                read();
                break;

            case 'D':
                displayCache();
                break;

            case 'W':
                read();
                write();
                break;
        }
        d++;
    }


    public static void read() {
        tag = (addresses[a] & 0xf00) >>> 8;
        slot = (addresses[a] & 0x0f0) >>> 4;
        offset = (addresses[a] & 0x00f);
        blockStart = (addresses[a] & 0xff0);
        if (cache[slot][2] == 1) {//if valid bit is one
            if (cache[slot][3] == tag) {
                hitOrMiss = "hit";
                displayInformation();
            } else {//if (cache[slot][3] != tag) {
                hitOrMiss = "miss";
                 if (cache[slot][1] == 1) {//if dirty bit is 1
                     copyDirtyCacheToMMBlock();
                     cache[slot][1] = 0;
                 }
                copyMMBlockToCache();
                cache[slot][3] = tag;//set tag to new value
                displayInformation();

            }
        } else {
            tag = (addresses[a] & 0xf00) >>> 8;
            slot = (addresses[a] & 0x0f0) >>> 4;
            offset = (addresses[a] & 0x00f);
            blockStart = (addresses[a] & 0xff0);
            hitOrMiss = "miss";
            copyMMBlockToCache();
            cache[slot][2] = 1;//valid bit to 1
            cache[slot][3] = tag;//set tag to new value
            displayInformation();
        }
    }


    public static void write() {
        cache[slot][offset+4] = writeArray[w];
        cache[slot][1] = 1;
        System.out.print("Wrote the value ");
        System.out.printf("%x", writeArray[w]);
        System.out.print(" to address ");
        System.out.printf("%x", addresses[a]);
        System.out.println();
        System.out.println();
        w++;
    }

    public static void displayCache() {
        printHeader();
        printCache();
    }


    public static void printHeader() {
        for (int h = 0; h <= 4; h++) {
            System.out.print(cacheHead[h]);
            System.out.print("\t");
        }
        System.out.println();
    }


    public static void printCache() {
        cache[slot][0] = slot;
        cache[slot][1] = dirty;
        cache[slot][2] = valid;
        cache[slot][3] = tag;


        for (int i = 0x0; i <= 0xf; i++) {
            for (int j = 0x0; j <= 0x13; j++) {
                System.out.printf("%x", cache[i][j]);
                System.out.print("\t");
                System.out.print("\t");
            }
            System.out.println();
            System.out.println();
        }
    }


    public static void displayInformation() {
        System.out.print("The byte at address ");
        System.out.printf("%x", addresses[a]);
        System.out.print(" is the value ");
        System.out.printf("%x", cache[slot][offset+4]);
        System.out.println(" (Cache " + hitOrMiss + ")");
        System.out.println();
    }


    public static void copyDirtyCacheToMMBlock() {
        System.out.println("cache to memory ");
        for (j = 4; j < 0x14; j++) {
            cache[slot][j] = MM[blockStart];
            System.out.printf("%x", MM[blockStart]);
            blockStart++;

        }
        System.out.println("");
    }

    public static void copyMMBlockToCache() {
        System.out.println("memory to cache");
        for (j = 4; j < 0x14; j++) {
            cache[slot][j] = MM[blockStart];
            System.out.printf("%x", MM[blockStart]);
            blockStart++;
        }
        System.out.println("");
    }
}


