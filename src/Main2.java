import java.util.List;

public class Main2 {

    public static void main(String[] args) {
        System.out.println(grade(116 + 295));
    }

    public static double grade(int points) {
        double rawGrade = 0.875 + 5.25 * (points / 500d);
        return Math.rint(rawGrade * 4) / 4;
    }

    public static void add0 (List <Integer> list )  {

        if (list.contains(0))
            return ;
        else list.add(0);

    }
}
