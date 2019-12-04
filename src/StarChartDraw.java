import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class StarChartDraw {



    public static double[] coordsToPixels(double x, double y, int size){
        return new double[]{(int) ((size/2 * x) + size/2), (int)((size/2 * -y) + size/2)};
    }

    public static void plotSquares(){

    }

    static class Star{

        double x;
        double y;
        double z;
        int hd;
        double magnitude;
        int hr;
        String[] names;

        public Star(double x, double y, double z, int hd, double magnitude, int hr, String[] names) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.hd = hd;
            this.magnitude = magnitude;
            this.hr = hr;
            this.names = names;
        }
    }



    public static void main(String[] args) throws FileNotFoundException {
        Scanner file = new Scanner(new File("stars.txt"));

        ArrayList<Star> stars = new ArrayList<>();

        while(file.hasNext()){
            Scanner line = new Scanner(file.nextLine());
            stars.add(new Star(line.nextDouble(), line.nextDouble(), line.nextDouble(), line.nextInt(), line.nextDouble(), line.nextInt(), line.nextLine().split("; ")));

        }





    }

}
