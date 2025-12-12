package advent.of.code;

import java.util.Arrays;

public class Day12 {
    void main() throws Exception {
        String input = FileIO.read("/day12.txt");
        part1(input);
    }
    
    void part1(String input) {
        int count = 0;
        for (String line : input.lines().toList()) {
            if (!line.contains("x")) continue;
            String[] parts = line.split(":? ");
            int[] dims = Arrays.stream(parts[0].split("x")).mapToInt(Integer::parseInt).toArray();
            int n = Arrays.stream(parts, 1, parts.length).mapToInt(Integer::parseInt).sum();
            int maxFit = dims[0]/3 * dims[1]/3;
            if (n <= maxFit) {
                count++;
            }
        }
        IO.println(count);
    }
}
