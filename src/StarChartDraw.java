import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class StarChartDraw {

    private static final int SIZE = 1000;
    private static List<Star> stars = new ArrayList<>();

    private static double coordToPixel(double coordinate){
        return (SIZE/2 * coordinate) + SIZE/2;
    }

    static class Star{
        double x;
        double y;
        double z;
        int hd;
        double magnitude;
        int hr;
        String[] names;
    }

    public static void main(String[] args) throws FileNotFoundException {

        //Loading star data
        Scanner file = new Scanner(new File("stars.txt"));
        while(file.hasNext()){
            Scanner line = new Scanner(file.nextLine());
            Star star = new Star();
            star.x = line.nextDouble();
            star.y = line.nextDouble();
            star.z = line.nextDouble();
            star.hd = line.nextInt();
            star.magnitude = line.nextDouble();
            star.hr = line.nextInt();
            if(line.hasNext()) {
                star.names = line.nextLine().trim().split("; ");
            }
            stars.add(star);
        }

        //Plotting Stars
        StdDraw.setCanvasSize(SIZE, SIZE);
        StdDraw.setScale(0, 1000);
        StdDraw.filledSquare(500, 500, 550);
        StdDraw.setPenColor(StdDraw.WHITE);
        for (Star star : stars) {
            StdDraw.filledCircle(coordToPixel(star.x), coordToPixel(star.y), 2);
        }

        //Loading name data to a HashBucket
        HashBucket data = new HashBucket(1000);
        for (Star star : stars) {
            if(star.names != null) {
                for (String name : star.names) {
                    data.put(name, star);
                }
            }
        }

        //Drawing Constellations
        StdDraw.setPenColor(StdDraw.YELLOW);
        Scanner constReader = new Scanner(new File("Constellations.txt"));
        while (constReader.hasNext()){
            String[] stars = constReader.nextLine().split(",");
            StdDraw.line(coordToPixel(((Star) data.get(stars[0])).x),coordToPixel(((Star) data.get(stars[0])).y) ,coordToPixel(((Star) data.get(stars[1])).x) ,coordToPixel(((Star) data.get(stars[1])).y));
        }
    }
}