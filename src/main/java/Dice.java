import java.util.Arrays;
import java.util.Random;

public class Dice {
    private final int size;
    private static final int[] validDiceSizes = new int[]{1,2, 3, 4, 6, 8, 10, 12, 20, 100};

    public Dice(int size){
        if(Arrays.stream(validDiceSizes).noneMatch(x->x ==size)) {
            throw new IllegalArgumentException("Dice size must be greater than 0");
        }
        this.size =size;
    }

    public int getSize() {
        return size;
    }

    public int roll(int number){
        int sum = 0;
        Random random = new Random();
        for (int i=0; i<number; i++){
            int temp = random.nextInt(1, size+1);
            System.out.println("Rolled d"+size+"! Result: "+ temp);
            sum += temp;
        }
        return sum;
    }
}
