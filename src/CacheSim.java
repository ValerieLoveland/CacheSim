public class CacheSim {
    static int MM[] = new int[2048];
    static int cache[][] = new int[0x10][0x14];
    static String cacheHead[] = new String[]{"slot ", "dirty ", "valid ", "tag ", "data"};
    static int[] addresses = new int[]{0x005, 0x006, 0x007, 0x14c, 0x14d, 0x14e, 0x14f, 0x150, 0x151, 0x3a6, 0x4c3, 0x582, 0x348, 0x14c, 0x63B, 0x3f, 0x14b, 0x14c, 0x63f, 0x083};
    static int a = 0;
    static int[] writeArray = {0x99, 0x7};
    static int w = 0;
    static char[] directionsArray = new char[]{'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'D', 'W', 'W', 'R', 'D', 'R', 'R', 'D', 'R', 'R', 'R', 'R', 'D'};
    static int d = 0;
    static int slot = 0;
    static int offset = 0;
    static int tag = 0;
    static int blockStart = 0;
    static int j;
    static int dirty = 0;
    static int valid = 0;
    static String hitOrMiss = "miss";
    //static cache[slot][0]=slot;

        public static void main (String[]args){
            //for(a=0;a<0x13;a++)


        fillMainMemory();
        breakIntoTabSlotOffset();
        copyMMBlockToCache();
        printHeader();
        printCache();

    }


        public static void fillMainMemory () {

        int mmFillWholeList = 0x00;
        //This part fills in 0-ff 7 times in MM*************************************
        for (int repeatedFill = 0; repeatedFill <= 0x7; repeatedFill++) {
            for (int i = 0x00; i <= 0xff; i++) {//if I make this 0x7ff it will show the address of the whole thing in hex******
                MM[mmFillWholeList] = i;
                mmFillWholeList++;

            }
        }

    }

        public static void printHeader () {
        for (int h = 0; h <= 4; h++) {
            System.out.print(cacheHead[h]);
            System.out.print("\t");
        }
        System.out.println();

    }

        public static void printCache () {
        cache[slot][0] = slot;
        cache[slot][1] = dirty;
        cache[slot][2] = valid;
        cache[slot][3] = tag;

        for (int i = 0x0; i <= 0xf; i++) {

            for (int j = 0x0; j <= 0x13; j++) {
                System.out.printf("%2x", cache[i][j]);
                System.out.print("\t");
                System.out.print("\t");

            }
            System.out.println();


        }
    }

        public static void breakIntoTabSlotOffset () {

        int slot = (addresses[a] & 0x0f0) >>> 4;
        int tag = (addresses[a] & 0xf00) >>> 8;
        int offset = (addresses[a] & 0x00f);
        int blockStart = (addresses[a] & 0xff0);


    }

        public static void copyMMBlockToCache () {
        int slot = (addresses[a] & 0x0f0) >>> 4;
        int tag = (addresses[a] & 0xf00) >>> 8;
        int offset = (addresses[a] & 0x00f);
        int blockStart = (addresses[a] & 0xff0);
        cache[slot][0] = slot;
        cache[slot][1] = dirty;
        cache[slot][2] = valid;
        cache[slot][3] = tag;
        for (j = 4; j < 0x14; j++) {
            cache[slot][j] = MM[blockStart];
            blockStart++;
            //System.out.println(MM[blockStart]);
        }
    }

        public static void copyDirtyCacheToMMBlock () {
        int slot = (addresses[a] & 0x0f0) >>> 4;
        int tag = (addresses[a] & 0xf00) >>> 8;
        int offset = (addresses[a] & 0x00f);
        int blockStart = (addresses[a] & 0xff0);
        cache[slot][0] = slot;
        cache[slot][1] = dirty;
        cache[slot][2] = valid;
        cache[slot][3] = tag;
        for (j = 4; j < 0x14; j++) {
            cache[slot][j] = MM[blockStart];
            blockStart++;
            System.out.println(MM[blockStart]);
        }
    }
        public static void readOrWrite () {
        if (cache[slot][2] == 1) {//if valid bit is one
            if ((cache[slot][0] == slot) && (cache[slot][3] == tag) && (cache[slot][1] == 0)) {//and if tag and slot match and dirty=0
                hitOrMiss = "hit";
                hitOrMissPrint();
                if (directionsArray[d] == 'R') {
                    directionsRead();
                    d++;
                } else if (directionsArray[d] == 'W')
                    directionsWrite();
                w++;

                if ((cache[slot][1] == 0) && (cache[slot][3] != tag) || (cache[slot][0] != slot)) {//and if dirty is 0 and either slot or tag doesn't match
                    hitOrMiss = "miss";
                    hitOrMissPrint();
                    copyMMBlockToCache();

                }

                if ((cache[slot][dirty] == 1) && ((cache[slot][3] != tag) || (cache[slot][0] != slot))) {//if dirty is 1 and tag or slot doesn't match
                    copyDirtyCacheToMMBlock();
                    hitOrMiss = "miss";
                    copyMMBlockToCache();
                    if (directionsArray[d] == 'R') {
                        directionsRead();
                        d++;
                    } else if (directionsArray[d] == 'W')
                        directionsWrite();
                    w++;
                }

                if ((cache[slot][0] == slot) && (cache[slot][3] == tag) && (cache[slot][1] == 1)) {
                    hitOrMiss = "hit";
                    hitOrMissPrint();
                    copyDirtyCacheToMMBlock();
                    directionsReadOrWrite();//check on this why I put it this way without read at first
                }

            }

            //System.out.println(cache[slot][offset]);//for read
            if (cache[slot][2] == 0) {
                hitOrMiss = "miss";
                hitOrMissPrint();
                copyMMBlockToCache();
                cache[slot][2] = 1;
                directionsReadOrWrite();
            }
        }


    }
        public static void hitOrMissPrint () {//pulls the value of if it is a hit or miss and prints
        System.out.println("Cache " + hitOrMiss);
    }


        public static void directionsRead () {//prints what is in the index of the cache
        System.out.println(cache[slot][offset]);
        a++;
        d++;

    }

        public static void directionsWrite () {//writes the value in the writeArray index into the offset in the cache
        cache[slot][offset] = writeArray[w];
        cache[slot][1] = 1;
        w++;
        a++;
    }
        public static void directionsReadOrWrite () {
        if (directionsArray[d] == 'R') {
            directionsRead();

        } else if (directionsArray[d] == 'W')
            directionsWrite();

    }
    }


