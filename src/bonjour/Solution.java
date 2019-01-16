package bonjour;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        String input;



        input = "{\n" +
                "    \"bascule\" : [\"IN1\", \"IN2\", \"OUT1\", \"OUT2\"],\n" +
                "    \"logique\" : [\n" +
                "        {\"nom\" : \"F1\",   \"delais\" : 12},\n" +
                "        {\"nom\" : \"F2\",   \"delais\" : 12},\n" +
                "        {\"nom\" : \"F3\",   \"delais\" : 12},\n" +
                "        {\"nom\" : \"F4\",   \"delais\" : 12},\n" +
                "        {\"nom\" : \"XOR1\", \"delais\" :  4},\n" +
                "        {\"nom\" : \"XOR2\", \"delais\" :  4},\n" +
                "        {\"nom\" : \"XOR3\", \"delais\" :  4},\n" +
                "        {\"nom\" : \"XOR4\", \"delais\" :  4}\n" +
                "    ],\n" +
                "    \"fils\" : [\n" +
                "        {\"in\" : \"IN1\",  \"out\" : \"F1\",   \"delais\" : 1},\n" +
                "        {\"in\" : \"IN2\",  \"out\" : \"XOR1\", \"delais\" : 1},\n" +
                "        {\"in\" : \"F1\",   \"out\" : \"XOR1\", \"delais\" : 1},\n" +
                "        {\"in\" : \"XOR1\", \"out\" : \"F2\",   \"delais\" : 1},\n" +
                "        {\"in\" : \"IN1\",  \"out\" : \"XOR2\", \"delais\" : 1},\n" +
                "        {\"in\" : \"F2\",   \"out\" : \"XOR2\", \"delais\" : 1},\n" +
                "        {\"in\" : \"XOR2\", \"out\" : \"F3\",   \"delais\" : 1},\n" +
                "        {\"in\" : \"XOR1\", \"out\" : \"XOR3\", \"delais\" : 1},\n" +
                "        {\"in\" : \"F3\",   \"out\" : \"XOR3\", \"delais\" : 1},\n" +
                "        {\"in\" : \"XOR3\", \"out\" : \"F4\",   \"delais\" : 1},\n" +
                "        {\"in\" : \"XOR2\", \"out\" : \"XOR4\", \"delais\" : 1},\n" +
                "        {\"in\" : \"F4\",   \"out\" : \"XOR4\", \"delais\" : 1},\n" +
                "        {\"in\" : \"XOR3\", \"out\" : \"OUT1\", \"delais\" : 1},\n" +
                "        {\"in\" : \"XOR4\", \"out\" : \"OUT2\", \"delais\" : 1}\n" +
                "    ]\n" +
                "}";


//
//        input = "";
//        Scanner scan = new Scanner(System.in);
//
//        while (scan.hasNextLine()) {
//            input += scan.nextLine() + "\n";
//        }

        new Solution(input);

    }

    public Solution(String in) {
        JSONObj obj1 = new JSONObj(in);
        JSONObj obj = obj1.getJSONObjectList().get(0);

        JSONArray basculeObj = (JSONArray) obj.getObjectByName("bascule");
        JSONArray logiqueObj = (JSONArray) obj.getObjectByName("logique");
        JSONArray filsObj = (JSONArray) obj.getObjectByName("fils");
        ArrayList<JSONObj> basculeList = (ArrayList<JSONObj>) basculeObj.getObject();
        ArrayList<JSONObj> filsList =  filsObj.getObject();
        ArrayList<JSONObj> logiqueList =  logiqueObj.getObject();

        //starts//
        ArrayList<JSONObj> startList = new ArrayList<JSONObj>();
        for (JSONObj fil : filsList) {
            for (Object bascule : basculeList) {
                Object inObject = fil.getObjectByName("in").getObject();
                if (bascule.equals(inObject)) {
                    startList.add(fil);
                }
            }
        }

        //ends//
        ArrayList<JSONObj> endList = new ArrayList<JSONObj>();
        for (JSONObj fil : filsList) {
            for (Object bascule : basculeList) {
                Object inObject = fil.getObjectByName("out").getObject();
                if (bascule.equals(inObject)) {
                    endList.add(fil);
                }
            }
        }


        double maxDelay = 0;
        for (JSONObj start : startList) {
            for (JSONObj end : endList) {
                double delay = calculateDelay(filsList, logiqueList, start, end);
                if (delay > maxDelay) {
                    maxDelay = delay;
                }
            }
        }

        int frequence = (int) ((1.0 / maxDelay) * 1000.0);
        System.out.println(frequence);
    }

    public int calculateDelay(ArrayList<JSONObj> filsList, ArrayList<JSONObj> logiqueList, JSONObj startingPoint, JSONObj endPoint) {
        int delay = 0;
        delay += (Integer) startingPoint.getObjectByName("delais").getObject();

        JSONObj logique = findLogiqueByName((String) startingPoint.getObjectByName("in").getObject(), logiqueList);
        if (logique != null) {
            delay += (Integer) logique.getObjectByName("delais").getObject();
        }

        String outName = (String) startingPoint.getObjectByName("out").getObject();

        ArrayList<JSONObj> inList = findInByName(outName, filsList);
        int maxDelay = 0;
        for (JSONObj in : inList) {
            int tempDelay = calculateDelay(filsList, logiqueList, in, endPoint);
            if (tempDelay > maxDelay) {
                maxDelay = tempDelay;
            }
        }

        delay += maxDelay;

        return delay;
    }

    public ArrayList<JSONObj> findInByName(String name, ArrayList<JSONObj> filsList) {
        ArrayList<JSONObj> inList = new ArrayList<>();
        for (JSONObj fil : filsList) {
            Object inObject = fil.getObjectByName("in").getObject();
            if (name.equals(inObject)) {
                inList.add(fil);
            }
        }
        return inList;
    }

    public JSONObj findLogiqueByName(String name, ArrayList<JSONObj> logiqueList) {
        for (JSONObj logique : logiqueList) {
            if (logique.getObjectByName("nom").getObject().equals(name)) {
                return logique;
            }
        }
        return null;

    }

    public static int findMatchingChar(char character, String in) {
        char matchingChar = 0;
        switch (character) {
            case '[':
                matchingChar = ']';
                break;
            case '{':
                matchingChar = '}';
                break;
            case '"':
                matchingChar = '"';
                break;
        }


        int nbChar = 0;
        for (int i = 0; i < in.length(); i++) {
            char readChar = in.charAt(i);
            if (readChar == matchingChar) {
                if (nbChar == 0) {
                    return i;
                }
                nbChar--;
            }
            else if (readChar == character) {
                nbChar++;
            }
        }

        return -1;
    }


    class JSONObj {

        private String name = "";
        private Object object = new Object();
        private ArrayList<JSONObj> JSONObjectList = new ArrayList<>();

        public JSONObj() {
        }

        public JSONObj(String name, String in) {
            this(name, (Object) in);
        }

        public JSONObj(String name, Object object) {
            setName(name);
            setObject(object);
        }


        public void addJSONObject(JSONObj jsonObj) {
            this.JSONObjectList.add(jsonObj);
        }


        public JSONObj getObjectByName(String objectName) {
            for (JSONObj object : getJSONObjectList()) {
                if (object.getName().equals(objectName)) {
                    return object;
                }
            }
            return null;
        }

        @Override
        public String toString() {

            if (getJSONObjectList().isEmpty()) {
                return "    Name: " + getName() + "\n  Class: " + getObject().getClass() + "\n    Obj: " + getObject().toString() + "\n\n";
            }
            else {
                String string = "objects:\n";

                for (JSONObj obj : getJSONObjectList()) {
                    string += obj.toString();
                }
                string += "======ENDOBJ\n";
                return string;
            }
        }

        public JSONObj(String in) {
            String itemName = "";
            String number = "";
            for (int i = 0; i < in.length(); i++) {
                char character = in.charAt(i);
                int j;

                switch (character) {
                    case '{':
                        i++;
                        j = i + findMatchingChar(character, in.substring(i));
                        this.addJSONObject(new JSONObj(in.substring(i, j)));
                        i = j + 1;
                        break;

                    case '"':
                        i++;
                        j = i + findMatchingChar(character, in.substring(i));
                        if (itemName.equals("")) {
                            itemName = in.substring(i, j);
                        }
                        else {
                            this.addJSONObject(new JSONObj(itemName, in.substring(i, j)));

                            itemName = "";
                        }
                        i = j + 1;

                        break;

                    case '[':
                        i++;
                        j = i + findMatchingChar(character, in.substring(i));
                        this.addJSONObject(new JSONArray(itemName, in.substring(i, j)));
                        itemName = "";
                        i = j + 1;

                        break;
                    default:

                        if (Character.isDigit(character)) {
                            number += "" + character;
                            if (i == in.length() - 1) {
                                this.addJSONObject(new JSONObj(itemName, Integer.parseInt(number)));
                            }
                        }
                        else if (!number.equals("") || i == in.length() - 1) {
                            this.addJSONObject(new JSONObj(itemName, Integer.parseInt(number)));
                            itemName = "";
                            number = "";
                        }
                        break;

                }

            }
        }


        public ArrayList<JSONObj> getJSONObjectList() {
            return JSONObjectList;
        }

        public void setJSONObjectList(ArrayList<JSONObj> JSONObjectList) {
            this.JSONObjectList = JSONObjectList;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }


    public class JSONArray extends JSONObj {

        public JSONArray(String name, String in) {
            setName(name);
            ArrayList<Object> list = new ArrayList<>();
            for (int i = 0; i < in.length(); i++) {
                char character = in.charAt(i);
                int j;
                switch (character) {
                    case '"':
                        i++;
                        j = i + findMatchingChar(character, in.substring(i));
                        list.add(in.subSequence(i, j));
                        i = j + 1;
                        break;
                    case '{':
                        i++;
                        j = i + findMatchingChar(character, in.substring(i));
                        list.add(new JSONObj(in.substring(i, j)));
                        i = j + 1;
                        break;
                }
            }
            setObject(list);
        }

        public ArrayList<JSONObj> getObject() {
            return (ArrayList<JSONObj>) super.getObject();
        }
    }

}

