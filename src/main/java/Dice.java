import java.util.Random;

public class Dice {
    private final int size;
    public Dice(int size){
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
