package faim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        String string = "";
//        string = "15\n" +
//                "Rice Krispies 3 11 2\n" +
//                "Ficello 6 15 4\n" +
//                "Cafe Keurig 1 2 9";

        Scanner scan = new Scanner(System.in);

        while (scan.hasNextLine()) {
            string += scan.nextLine() + "\n";
        }
        Solution solution = new Solution(string);
    }

    private ArrayList<Product> products = new ArrayList<>();
    private int N = 0;
    private int base = 28;


    public Solution(String string) {

        String[] lineList = string.split("\n");
        N = Integer.parseInt(lineList[0]);
        for (int i = 1; i < lineList.length; i++) {
            products.add(new Product(lineList[i]));
        }


        Integer bestOption[] = new Integer[products.size()];
        int max = 0;




        for (int j = 0; j < Math.pow(base, products.size()); j++) {
            Integer array[] = toArray(j);
            if (checkPossibility(array)) {
                if (calculate(array) > max) {
                    bestOption = array;
                    max = calculate(array);
                }
            }
        }


        //order

        boolean ordered = false;
        while (!ordered) {
            ordered = true;
            for (int i = 0; i < products.size() - 1; i++) {
                if (bestOption[i] < bestOption[i + 1]) {
                    int temp = bestOption[i];
                    bestOption[i] = bestOption[i + 1];
                    bestOption[i + 1] = temp;

                    Collections.swap(products, i, i + 1);
                    ordered = false;
                }
                else if (bestOption[i] == bestOption[i + 1]) {
                    if (products.get(i).getName().charAt(0) > products.get(i + 1).getName().charAt(0)) {
                        int temp = bestOption[i];
                        bestOption[i] = bestOption[i + 1];
                        bestOption[i + 1] = temp;

                        Collections.swap(products, i, i + 1);
                        ordered = false;
                    }
                }

            }
        }

        //print

        for (int i = 0; i < products.size(); i++) {
            if (bestOption[i] != 0) {
                System.out.println(bestOption[i] + " " + products.get(i).getName());

            }
        }


    }

    public String intArrayToString(Integer[] num) {
        String string = "";
        for (Integer number : num) {
            string += "[" + String.valueOf(number) + "]";

        }
        return string;
    }

    public boolean checkPossibility(Integer array[]) {

        //testQuantities

        for (int i = 0; i < array.length; i++) {
            Integer number = array[i];
            Product product = products.get(i);
            if (product.quant < number) {
                return false;
            }
        }


        //testMoney
        if (!(moneyRemaining(array) >= 0)) {
            return false;
        }


        return true;

    }


    public Integer[] toArray(int number) {
        String string = Integer.toString(number, base) + "";

        while (string.length() != products.size()) {
            string = "0" + string;
        }

        Integer result[] = new Integer[string.length()];
        for (int i = 0; i < string.length(); i++) {
            result[i] = Character.getNumericValue(string.charAt(i));
        }
        return result;
    }

    public int calculate(Integer quantities[]) {
        int result = 0;
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            result += product.getSaveur() * quantities[i];
        }
        return result;
    }

    public int moneyRemaining(Integer quantities[]) {
        int result = N;
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            Integer quantity = quantities[i];
            result -= product.getPrice() * quantity;
        }
        return result;
    }

    public class Product {
        private String name = "";
        private int price = 0, saveur = 0, quant = 0;

        public Product(String string) {
            int step = 0;
            String number = "";
            for (int i = 0; i < string.length(); i++) {
                char character = string.charAt(i);
                if (step == 0) {
                    if (Character.isDigit(character)) {
                        this.name = this.getName().substring(0, getName().length() - 1);
                        step++;
                    }
                    else {
                        this.name += "" + character;
                    }
                }

                if (Character.isDigit(character)) {
                    number += "" + character;
                }
                else {
                    if (!number.equals("")) {

                        switch (step) {
                            case 1:
                                this.price = Integer.parseInt(number);
                                break;
                            case 2:
                                this.saveur = Integer.parseInt(number);
                                break;
                        }
                        step++;
                        number = "";
                    }
                }

            }
            this.quant = Integer.parseInt(number);
        }


        public Product(String name, int price, int saveur, int quant) {
            this.name = name;
            this.price = price;
            this.saveur = saveur;
            this.quant = quant;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getSaveur() {
            return saveur;
        }

        public void setSaveur(int saveur) {
            this.saveur = saveur;
        }

        public int getQuant() {
            return quant;
        }

        public void setQuant(int quant) {
            this.quant = quant;
        }

        @Override
        public String toString() {
            return "Product{" +
                    "name='" + name + '\'' +
                    ", price=" + price +
                    ", saveur=" + saveur +
                    ", quant=" + quant +
                    '}';
        }
    }
}
