package advent.of.code;

import java.util.Scanner;
import java.util.TreeMap;

public class Day05 {
    void main() throws Exception {
        String input = FileIO.read("/day05.txt");
        part1(input);
        part2(input);
    }
    
    void part1(String input) {
        TreeMap<Long, Long> ranges = new TreeMap<>();
        Scanner scanner = new Scanner(input);
        for (String line; !(line = scanner.nextLine()).isEmpty(); ) {
            String[] parts = line.split("-");
            insertRange(ranges, Long.parseLong(parts[0]), Long.parseLong(parts[1])+1);
        }
        int count = 0;
        while (scanner.hasNextLong()) {
            if (includes(ranges, scanner.nextLong())) {
                count++;
            }
        }
        IO.println(count);
    }
    
    void part2(String input) {
        TreeMap<Long, Long> ranges = new TreeMap<>();
        Scanner scanner = new Scanner(input);
        for (String line; !(line = scanner.nextLine()).isEmpty(); ) {
            String[] parts = line.split("-");
            insertRange(ranges, Long.parseLong(parts[0]), Long.parseLong(parts[1])+1);
        }
        long count = 0;
        for (var range : ranges.entrySet()) {
            count += range.getValue() - range.getKey();
        }
        IO.println(count);
    }
    
    void insertRange(TreeMap<Long, Long> ranges, long start, long end) {
        var prev = ranges.floorEntry(start);
        var next = ranges.floorEntry(end);
        if (prev != null && start <= prev.getValue()) start = prev.getKey();
        if (next != null && end <= next.getValue()) end = next.getValue();
        ranges.subMap(start, end+1).clear();
        ranges.put(start, end);
    }
    
    boolean includes(TreeMap<Long, Long> ranges, long value) {
        var range = ranges.floorEntry(value);
        return range != null && value < range.getValue();
    }
}
