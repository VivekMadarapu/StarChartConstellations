import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class HashBucket {

    LinkedList[] table;
    int size;

    public HashBucket(){
        table = new LinkedList[101];
        size = 0;
    }

    public HashBucket(int initCap){
        table = new LinkedList[initCap + 1];
        size = 0;
    }

    public Object put(Object key, Object value){
        if(size == table.length){
            throw new IllegalStateException("Hashbucket is full");
        }
        int hash = key.hashCode();
        int index = Math.abs(hash%table.length);
        LinkedList prev = table[index];
        if(prev == null){
            table[index] = new LinkedList();
            table[index].addLast(new Entry(key, value));
            size++;
            return null;
        }
        else if(!((Entry) prev.getFirst()).removed){
            if(((Entry) prev.getFirst()).key.equals(key)) {
                table[index] = new LinkedList();
                table[index].addLast(new Entry(key, value));
                return ((Entry) prev.peekFirst()).value;
            }
            else{
                if(prev.contains(new Entry(key, value))){
                    for (Object o : table[index]) {
                        if(((Entry) o).key.equals(key)){
                            Object val = ((Entry) o).value;
                            o = new Entry(key, value);
                            return val;
                        }
                    }
                }
                prev.addLast(new Entry(key, value));
                size++;
                return null;
            }
        }
        return ((Entry) prev.peekFirst()).value;
    }

    public Object get(Object key){
        int hash = key.hashCode();
        int index = Math.abs(hash%table.length);
        if(table[index] == null){
            return null;
        }
        else if(!((Entry) table[index].getFirst()).removed && ((Entry) table[index].getFirst()).key.equals(key)){
            return ((Entry) table[index].getFirst()).value;
        }
        for (Object o : table[index]) {
            if(((Entry) o).key.equals(key)){
                return ((Entry) o).value;
            }
        }
        return null;
    }

    public Object remove(Object key){
        int hash = key.hashCode();
        int index = hash%table.length;
        if(table[index] == null){
            return null;
        }
        else if(!((Entry) table[index].getFirst()).removed){
            ((Entry) table[index].getFirst()).removed = true;
            size--;
            return ((Entry) table[index].getFirst()).value;
        }
        return null;
    }

    public double calcAverageListSize(){

        double average = 0;
        int num = 0;
        for (LinkedList linkedList : table) {
            if(linkedList != null){
                average += linkedList.size();
                num++;
            }
        }
        return average/num;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (LinkedList entry:table) {
            out.append(entry).append("\n");
        }
        return out.toString();
    }

    private static class Entry {
        Object key;
        Object value;
        boolean removed;

        Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
            removed = false;
        }

        @Override
        public boolean equals(Object o) {
            if(o.getClass().equals(Entry.class)){
                return ((Entry) o).key.equals(key);
            }
            return false;
        }

        @Override
        public String toString() {
            if(removed){
                return "dummy";
            }
            else {
                return key + " : " + value;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        File output = new File("results.csv");
        int version = 0;
        while (output.exists()){
            version++;
            output = new File("results" + version + ".csv");
        }
        output.createNewFile();
        FileWriter writer = new FileWriter(output);

        ArrayList<Double> putTimeAverages = new ArrayList<>();
        ArrayList<Double> getSuccessfulTimeAverages = new ArrayList<>();
        ArrayList<Double> getUnsuccessfulTimeAverages = new ArrayList<>();
        ArrayList<Double> averageListSizes = new ArrayList<>();

        for(double i = 0.1;i < 1.0;i+=0.1) {
            Scanner input = new Scanner(new File("sampledata500k.txt"));
            HashBucket table = new HashBucket((int) Math.round((500000/i)));
            ArrayList<String> inputData = new ArrayList<>();
            while (input.hasNext()) {
                inputData.add(input.nextLine());
            }

            System.out.print("Load factor: ");
            System.out.printf("%.1f", i);
            System.out.println();
            long start = System.currentTimeMillis();
            for (String in:inputData) {
                table.put(Integer.parseInt(in.substring(0, 8).trim()), in.substring(8).trim());
            }
            long end = System.currentTimeMillis();
            System.out.println("Time to put: " + ((end - start)/(500000/i)) + " ms");
            System.out.println("Table Items: " + table.size);
            System.out.println("Average list size: " + table.calcAverageListSize());
            putTimeAverages.add((end - start)/(500000/i));
            averageListSizes.add(table.calcAverageListSize());

            start = System.currentTimeMillis();
            for (String in:inputData) {
                table.get(Integer.parseInt(in.substring(0, 8).trim()));
            }
            end = System.currentTimeMillis();
            System.out.println("Time to get successful: " + ((end - start)/(500000/i)) + " ms");
            getSuccessfulTimeAverages.add((end - start)/(500000/i));

            start = System.currentTimeMillis();
            for (String in:inputData) {
                table.get(Integer.parseInt(in.substring(0, 8).trim())*100);
            }
            end = System.currentTimeMillis();
            System.out.println("Time to get unsuccessful: " + ((end - start)/(500000/i)) + " ms");
            getUnsuccessfulTimeAverages.add((end - start)/(500000/i));
            System.out.println();

        }

        writer.write("Load Factor,Put Time\n");
        writer.flush();
        for (int i = 0;i < putTimeAverages.size();i++) {
            writer.write((((double) i+1.0)/10) + "," + putTimeAverages.get(i)+"\n");
            writer.flush();
        }
        writer.write("\n\n\n\n\n\nLoad Factor,Get Successful Time\n");
        writer.flush();
        for (int i = 0;i < getSuccessfulTimeAverages.size();i++) {
            writer.write((((double) i+1.0)/10) + "," + getSuccessfulTimeAverages.get(i)+"\n");
            writer.flush();
        }
        writer.write("\n\n\n\n\n\nLoad Factor,Get Unsuccessful Time\n");
        writer.flush();
        for (int i = 0;i < getUnsuccessfulTimeAverages.size();i++) {
            writer.write((((double) i+1.0)/10) + "," + getUnsuccessfulTimeAverages.get(i)+"\n");
            writer.flush();
        }
        writer.write("\n\n\n\n\n\nLoad Factor,Average List Size\n");
        writer.flush();
        for (int i = 0;i < averageListSizes.size();i++) {
            writer.write((((double) i+1.0)/10) + "," + averageListSizes.get(i)+"\n");
            writer.flush();
        }
    }
}