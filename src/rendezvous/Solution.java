package rendezvous;

import java.util.ArrayList;
import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {

        String string = "";

//
        Scanner scan = new Scanner(System.in);
        while (scan.hasNextLine()) {
            string += scan.nextLine() + "\n";
        }
//        string = "30, 15, 60, 75, 45, 15, 15, 45";
//        string = "30, 15, 60, 75, 45, 15, 15, 45";


        new Solution(string);
    }


    public Solution(String string) {
        ArrayList<Integer> list = stringToArray(string);
        int n = list.size();
        ArrayList<Integer> possibilities = generateAllPossibilites(n);


        int max = 0;

        for (Integer numb : possibilities) {
            String numString = Integer.toBinaryString(numb);
            while (numString.length() != n) {
                numString = "0" + numString;
            }
            int score = calcScore(list, numString);
            if (score >= max) {
                max = score;
            }

        }
        System.out.println(max);

    }

    public int calcScore(ArrayList<Integer> list, String poss) {
        int res = 0;
        for (int i = 0; i < list.size(); i++) {
            Integer num = list.get(i);
            res += num * Integer.parseInt(poss.charAt(i) + "");
        }
        return res;
    }

    public void printBinaryString(ArrayList<Integer> num) {
        for (Integer number : num) {
            System.out.print("[" + Integer.toBinaryString(number) + "]");
        }
        System.out.println();

    }


    public ArrayList<Integer> generateAllPossibilites(int n) {
        ArrayList<Integer> res = new ArrayList<>();
        for (int i = 0; i < Math.pow(2, n); i++) {
            if (isValid(i)) {
                res.add(i);
            }
        }
        return res;

    }


    public boolean isValid(Integer integer) {
        String string = Integer.toBinaryString(integer);
        boolean read1 = false;

        for (char character : string.toCharArray()) {

            if (character == '1') {
                if (read1) {
                    return false;
                }
                else {
                    read1 = true;
                }

            }
            else {
                read1 = false;
            }
        }
        return true;
    }


    public ArrayList<Integer> stringToArray(String string) {
        ArrayList<Integer> list = new ArrayList<>();

        String stringSplit[] = string.split(", ");


        for (String num : stringSplit) {
            list.add(Integer.parseInt(num));
        }

        return list;
    }
}
