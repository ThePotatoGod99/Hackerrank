package conflicts;

import java.util.*;


public class Solution {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        String string = "";
        while (scan.hasNextLine()) {
            string += scan.nextLine() + "\n";
        }



//        String string = "2\n" +
//                "INF1005C Lundi 12:45 Lundi 13:35\n" +
//                "INF1500 Mardi 14:45 Mardi 17:35";
//        string = "7\n" +
//                "Allo Mercredi 13:12 Mercredi 17:29\n" +
//                "Sddff Mercredi 10:10 Mercredi 16:11\n"+
//                "INF1005C Lundi 12:45 Lundi 13:45\n" +
//                "INF1500 Lundi 13:45 Lundi 14:45\n" +
//                "INF1040 Lundi 13:00 Lundi 14:00\n" +
//                "INF2010 Mardi 13:00 Mardi 15:00\n" +
//                "INF2020 Mardi 13:00 Mardi 16:00";
        Solution conflitHoraire = new Solution(string);
    }
    public Solution(String string){




        String[] lineList = string.split("\n");
        int N = Integer.parseInt(lineList[0]);
        ArrayList<Cours> courseList = new ArrayList<>();


        for (int i = 1; i <= N; i++) {
            String[] lineSeparated = lineList[i].split(" ");

            String startString[] = {lineSeparated[1], lineSeparated[2]};
            String endString[] = {lineSeparated[3], lineSeparated[4]};

            Time startTime = new Time(startString);
            Time endTime = new Time(endString);


            Cours courseTime = new Cours(startTime, endTime);
            courseList.add(courseTime);
        }


        boolean hasConflict = false;

        ArrayList<Cours> conflictList = new ArrayList<>();

        for (int i = 0; i < courseList.size(); i++) {
            Cours courseTime = courseList.get(i);
            int start1 = courseTime.getStart().getTimeNb();
            int end1 = courseTime.getEnd().getTimeNb();
            for (int j = i + 1; j < courseList.size(); j++) {
                Cours courseTime2 = courseList.get(j);

                int start2 = courseTime2.getStart().getTimeNb();
                int end2 = courseTime2.getEnd().getTimeNb();
                Cours conflict = new Cours();



                if (start1 >= start2 && start1 <= end2) {
                    hasConflict = true;
                    conflict.setStart(start1);

                }
                else if (start2 >= start1 && start2 <= end1) {
                    hasConflict = true;
                    conflict.setStart(start2);
                }

                if (end1 >= start2 && end1 <= end2) {
                    hasConflict = true;
                    conflict.setEnd(end1);

                }
                else if (end2 >= start1 && end2 <= end1) {
                    hasConflict = true;
                    conflict.setEnd(end2);
                }


                if (!conflict.isEmpty()){
                    conflictList.add(conflict);
                }

            }
        }





        for (int i = 0; i < conflictList.size(); i++) {
            Cours conflict1 = conflictList.get(i);
            for (int j = 0; j < conflictList.size() - 1; j++) {
                Cours conflict2 = conflictList.get(j);
                if (conflict1.getEnd().equals(conflict2.getStart())) {
                    Cours newConflict = new Cours(conflict1.getStart(), conflict2.getEnd());
                    conflictList.remove(i);
                    conflictList.remove(i);
                    conflictList.add(newConflict);
                }
                else if (conflict1.getStart().equals(conflict2.getEnd())){
                    Cours newConflict = new Cours(conflict2.getStart(), conflict1.getEnd());
                    conflictList.remove(i);
                    conflictList.remove(i);
                    conflictList.add(newConflict);

                }


            }


        }


        Collections.sort(conflictList);


        if (hasConflict) {

            printConfList(conflictList);
        }
        else {
            System.out.println("OK");
        }

    }







    public static void printConfList(ArrayList<Cours> conflictList) {
        for (int k = 0; k < conflictList.size(); k++) {
            String confli1[] = conflictList.get(k).getStart().getStringTime();//toStringTime(conflictList.get(k)[0]);
            String confli2[] = conflictList.get(k).getEnd().getStringTime();//toStringTime(conflictList.get(k)[1]);
            System.out.print(confli1[0] + " " + confli1[1] + " ");
            System.out.println(confli2[0] + " " + confli2[1]);

        }
    }


    public class Cours implements  Comparable{
        private Time start = new Time(), end = new Time();


        public Cours(){

        }
        public Cours(Time start, Time end) {
            this.start = start;
            this.end = end;
        }

        public Cours(int start, int end) {
            this(new Time(start), new Time(end));
        }
        boolean isEmpty(){
            return start.equals(end);
        }


        public Time getStart() {
            return start;
        }

        public void setStart(int start){
            setStart(new Time(start));
        }
        public void setStart(Time start) {
            this.start = start;
        }

        public Time getEnd() {
            return end;
        }

        public void setEnd(Time end) {
            this.end = end;
        }

        public void setEnd(int end) {
            setEnd(new Time(end));


        }

        @Override
        public boolean equals(Object o) {
            return getEnd().equals(getStart());
        }

        @Override
        public int compareTo(Object o) {
            Cours b = (Cours) o;


            return this.getStart().compareTo(b.getStart());
        }
    }

    public class Time implements  Comparable{
        private int timeNb = 0;

        public Time(){

        }
        public Time(int timeNb){
            setTimeNb(timeNb);
        }

        public Time(String timeStr[]) {
            setTimeNb(timeStr);
        }



        public int setTimeNb(String timeString[]) {
            int time = 0;
            String day = timeString[0];
            String hourMin[] = timeString[1].split(":");
            int hour = Integer.parseInt(hourMin[0]);
            int min = Integer.parseInt(hourMin[1]);

            if (day.equals("Mardi")) {
                time += 24 * 60;

            }
            else if (day.equals("Mercredi")) {
                time += 24 * 60 * 2;
            }
            else if (day.equals("Jeudi")) {
                time += 24 * 60 * 3;

            }
            else if (day.equals("Vendredi")) {
                time += 24 * 60 * 4;

            }
            else if (day.equals("Samedi")) {
                time += 24 * 60 * 5;

            }
            else if (day.equals("Dimanche")) {
                time += 24 * 60 * 6;
            }

            time += min;
            time += hour * 60;

            setTimeNb(time);
            return time;
        }

        public String[] getStringTime(){
            return toStringTime(getTimeNb());
        }
        public String[] toStringTime(int time) {
            int day = time / (24 * 60);

            int timeInDay = time - (24 * 60 * day);
            String dayString = "";

            switch (day) {
                case 0:
                    dayString = "Lundi";
                    break;
                case 1:
                    dayString = "Mardi";
                    break;
                case 2:
                    dayString = "Mercredi";
                    break;
                case 3:
                    dayString = "Jeudi";
                    break;
                case 4:
                    dayString = "Vendredi";
                    break;
                case 5:
                    dayString = "Samedi";
                    break;
                case 6:
                    dayString = "Dimanche";
                    break;
            }

            int hour = timeInDay / (60);
            int min = timeInDay - (hour * 60);

            String res[] = {dayString, hour + ":" + min};
            if(min == 0){
                res[1] += "0";
            }
            if(hour == 0){
                res[1] = "0" + res[1];
            }
            return res;

        }




        public int getTimeNb() {
            return timeNb;
        }

        public void setTimeNb(int timeNb) {
            this.timeNb = timeNb;
        }

        @Override
        public boolean equals(Object o) {
            return this.getTimeNb() == ((Time) o).getTimeNb();
        }

        @Override
        public int compareTo(Object o) {
            Integer a = getTimeNb();
            Integer b = ((Time) o).getTimeNb();

            return a.compareTo(b);
        }
    }
}
