package Online_banking_management;

import java.util.ArrayList;
import java.util.Random;

public class AccountNumberGenerator {
    static ArrayList<String> accNumbers = new ArrayList<>();
    public static void main(String[] args) {
        String acc = generateAccountNumber();
        generateUniqueAccountNumber(acc);
    }


    public static String generateAccountNumber() {
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        String uniquePart = String.valueOf(random.nextInt(1000000));
        return "AC" + uniquePart;
    }

   
    private static String generateUniqueAccountNumber(String accNum) {
        try {
            if (!accNumbers.contains(accNum)) {
                System.out.println(accNum);
                accNumbers.add(accNum);
            } else {
               
                generateUniqueAccountNumber(generateAccountNumber());
            }
           
        } catch (Exception e) {
            e.printStackTrace();
        }
		return accNum;
    }
}
