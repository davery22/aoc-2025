package advent.of.code;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.IntStream;

public class Day04 {
    void main() throws Exception {
        String input = FileIO.read("/day04.txt");
        part1(input);
        part2(input);
    }
    
    static final int[][] DYX = new int[][]{{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
    
    void part1(String input) {
        int[][] grid = parseGrid(input);
        int ylen = grid.length;
        int xlen = grid[0].length;
        int accessibleRolls = 0;
        for (int y = 0; y < ylen; y++) {
            loop: for (int x = 0; x < xlen; x++) {
                if (grid[y][x] != '@') continue;
                int adjacentRolls = 0;
                for (int[] dyx : DYX) {
                    int ny = y + dyx[0], nx = x + dyx[1];
                    if (ny >= 0 && ny < ylen && nx >= 0 && nx < xlen && grid[ny][nx] == '@' && ++adjacentRolls >= 4) {
                        continue loop;
                    }
                }
                accessibleRolls++;
            }
        }
        IO.println(accessibleRolls);
    }
    
    void part2(String input) {
        int[][] grid = parseGrid(input);
        int threshold = 4;
        int ylen = grid.length;
        int xlen = grid[0].length;
        int[][] adjacent = IntStream.range(0, ylen).mapToObj(_ -> new int[xlen]).toArray(int[][]::new);
        record Pos(int y, int x) {}
        Deque<Pos> queue = new ArrayDeque<>();
        
        // Fill in adjacency count for each roll, enqueueing rolls whose adjacency is below threshold
        for (int y = 0; y < ylen; y++) {
            for (int x = 0; x < xlen; x++) {
                if (grid[y][x] != '@') continue;
                for (int[] dyx : DYX) {
                    int ny = y + dyx[0], nx = x + dyx[1];
                    if (ny >= 0 && ny < ylen && nx >= 0 && nx < xlen && grid[ny][nx] == '@') {
                        adjacent[y][x]++;
                    }
                }
                if (adjacent[y][x] < threshold) {
                    queue.offer(new Pos(y, x));
                }
            }
        }
        
        // Remove each roll, enqueueing surrounding rolls as they fall below adjacency threshold
        int removed = 0;
        while (!queue.isEmpty()) {
            removed++;
            Pos pos = queue.poll();
            for (int[] dyx : DYX) {
                int ny = pos.y + dyx[0], nx = pos.x + dyx[1];
                if (ny >= 0 && ny < ylen && nx >= 0 && nx < xlen && adjacent[ny][nx]-- == threshold) {
                    queue.offer(new Pos(ny, nx));
                }
            }
        }
        
        IO.println(removed);
    }
    
    int[][] parseGrid(String input) {
        return input.lines()
            .map(line -> line.chars().toArray())
            .toArray(int[][]::new);
    }
}
