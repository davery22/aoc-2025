package advent.of.code;

public class Day01 {
    void main() throws Exception {
        String input = FileIO.read("/day01.txt");
        part1(input);
        part2(input);
    }
    
    void part1(String input) {
        int pos = 50;
        int zeros = 0;
        for (String line : input.lines().toList()) {
            int step = parseStep(line);
            int nextPos = Math.floorMod(pos + step, 100);
            if (nextPos == 0) {
                zeros++;
            }
            pos = nextPos;
        }
        IO.println(zeros);
    }
    
    void part2(String input) {
        int pos = 50;
        int zeros = 0;
        for (String line : input.lines().toList()) {
            int step = parseStep(line);
            int nextPos = Math.floorMod(pos + step, 100);
            // Add full rotations, +1 if remaining rotation crosses / lands on zero
            zeros += Math.abs(step) / 100;
            if (step > 0 && nextPos < pos) {
                zeros++;
            }
            if (pos != 0 && step < 0 && (nextPos == 0 || nextPos > pos)) {
                zeros++;
            }
            pos = nextPos;
        }
        IO.println(zeros);
    }
    
    int parseStep(String line) {
        return (line.charAt(0) == 'R' ? 1 : -1) * Integer.parseInt(line.substring(1));
    }
}
