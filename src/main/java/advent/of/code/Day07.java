package advent.of.code;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.IntStream;

public class Day07 {
    void main() throws Exception {
        String input = FileIO.read("/day07.txt");
        part1(input);
        part2(input);
    }
    
    record Pos(int y, int x) {}
    
    void part1(String input) {
        var grid = input.lines().map(line -> line.chars().toArray()).toArray(int[][]::new);
        int x0 = IntStream.range(0, grid[0].length).filter(x -> grid[0][x] == 'S').findFirst().orElseThrow();
        SequencedSet<Pos> queue = new LinkedHashSet<>();
        queue.add(new Pos(0, x0));
        int count = 0;
        while (!queue.isEmpty()) {
            Pos pos = queue.removeFirst();
            int ny = pos.y+1, x = pos.x;
            if (ny < grid.length) {
                int cell = grid[ny][x];
                if (cell == '^') {
                    count++;
                    if (x > 0) {
                        queue.add(new Pos(ny, x-1));
                    }
                    if (x < grid[0].length-1) {
                        queue.add(new Pos(ny, x+1));
                    }
                }
                else {
                    queue.add(new Pos(ny, x));
                }
            }
        }
        IO.println(count);
    }
    
    void part2(String input) {
        var grid = input.lines().map(line -> line.chars().toArray()).toArray(int[][]::new);
        int x0 = IntStream.range(0, grid[0].length).filter(x -> grid[0][x] == 'S').findFirst().orElseThrow();
        SequencedMap<Pos, Long> queue = new LinkedHashMap<>();
        queue.put(new Pos(0, x0), 1L);
        long count = 0;
        while (!queue.isEmpty()) {
            Entry<Pos, Long> e = queue.pollFirstEntry();
            int ny = e.getKey().y+1, x = e.getKey().x;
            long c = e.getValue();
            if (ny < grid.length) {
                int cell = grid[ny][x];
                if (cell == '^') {
                    if (x > 0) {
                        queue.merge(new Pos(ny, x-1), c, Long::sum);
                    }
                    if (x < grid[0].length-1) {
                        queue.merge(new Pos(ny, x+1), c, Long::sum);
                    }
                }
                else {
                    queue.merge(new Pos(ny, x), c, Long::sum);
                }
            }
            else {
                count += c;
            }
        }
        IO.println(count);
    }
}
