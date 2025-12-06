package advent.of.code;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Day06 {
    void main() throws Exception {
        String input = FileIO.read("/day06.txt");
        part1(input);
        part2(input);
    }
    
    void part1(String input) {
        List<String> lines = input.lines().toList();
        String[] opsLine = lines.getLast().trim().split("\\s+");
        int h = lines.size()-1;
        int w = opsLine.length;
        List<List<Long>> problems = IntStream.range(0, w).mapToObj(_ -> (List<Long>) new ArrayList<Long>()).toList();
        
        // Proceed row-by-row, parsing each number left-to-right
        for (String line : lines.subList(0, h)) {
            String[] parts = line.trim().split("\\s+");
            for (int i = 0; i < w; i++) {
                long num = Long.parseLong(parts[i]);
                problems.get(i).add(num);
            }
        }
        
        IO.println(checksum(problems, opsLine));
    }
    
    void part2(String input) {
        List<String> lines = input.lines().toList();
        String[] opsLine = lines.getLast().trim().split("\\s+");
        int h = lines.size()-1;
        int w = opsLine.length;
        List<List<Long>> problems = IntStream.range(0, w).mapToObj(_ -> (List<Long>) new ArrayList<Long>()).toList();
        
        // Proceed column-by-column, parsing each number top-to-bottom
        for (int i = 0, x = 0; i < w; x++) {
            long num = 0;
            boolean isEmptyColumn = true;
            for (String line : lines.subList(0, h)) {
                int cell = x < line.length() ? line.charAt(x) : ' ';
                if (cell != ' ') {
                    isEmptyColumn = false;
                    num = num * 10 + (cell - '0');
                }
            }
            if (isEmptyColumn) {
                i++;
            }
            else {
                problems.get(i).add(num);
            }
        }
        
        IO.println(checksum(problems, opsLine));
    }
    
    long checksum(List<List<Long>> problems, String[] ops) {
        long sum = 0;
        for (int i = 0; i < problems.size(); i++) {
            sum += switch (ops[i]) {
                case "*" -> problems.get(i).stream().mapToLong(n -> n).reduce(1, (x, y) -> x*y);
                case "+" -> problems.get(i).stream().mapToLong(n -> n).sum();
                default -> throw new IllegalStateException();
            };
        }
        return sum;
    }
}
