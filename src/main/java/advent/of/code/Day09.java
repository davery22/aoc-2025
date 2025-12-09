package advent.of.code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Day09 {
    void main() throws Exception {
        String input = FileIO.read("/day09.txt");
        part1(input);
        part2(input);
    }
    
    void part1(String input) {
        long[][] tiles = input.lines()
            .map(line -> Arrays.stream(line.split(",")).mapToLong(Long::parseLong).toArray())
            .toArray(long[][]::new);
        long maxArea = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = i+1; j < tiles.length; j++) {
                long area = (Math.abs(tiles[i][0] - tiles[j][0]) + 1) * (Math.abs(tiles[i][1] - tiles[j][1]) + 1);
                maxArea = Math.max(maxArea, area);
            }
        }
        IO.println(maxArea);
    }
    
    void part2(String input) {
        record Rect(int i, int j, long area) {}
        long[][] tiles = input.lines()
            .map(line -> Arrays.stream(line.split(",")).mapToLong(Long::parseLong).toArray())
            .toArray(long[][]::new);
        List<Rect> candidates = new ArrayList<>();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = i+1; j < tiles.length; j++) {
                long area = (Math.abs(tiles[i][0] - tiles[j][0]) + 1) * (Math.abs(tiles[i][1] - tiles[j][1]) + 1);
                candidates.add(new Rect(i, j, area));
            }
        }
        candidates.sort(Comparator.comparing(Rect::area, Comparator.reverseOrder()));
        search: for (Rect rect : candidates) {
            // If rectangle is within the loop, its area is our answer.
            // Rectangle is within the loop if no other edges intersect its edges.
            // Assumption: The loop does not contain adjacent edges (parallel with no space between),
            // as that would allow edges to intersect our rectangle in a way that stays inside the loop.
            long[] a = tiles[rect.i], c = tiles[rect.j], b = new long[]{ a[0], c[1] }, d = new long[]{ c[0], a[1] };
            long[][][] sides = new long[][][]{{a,b}, {b,c}, {c,d}, {d,a}};
            for (int k = 0; k < tiles.length; k++) {
                long[][] edge = new long[][]{ tiles[k], tiles[(k+1) % tiles.length] };
                for (long[][] side : sides) {
                    boolean edgeV = edge[0][0] == edge[1][0];
                    boolean sideV = side[0][0] == side[1][0];
                    if (edgeV == sideV) continue;
                    // TODO: Currently, an edge that extends from a side counts as intersecting - not always true.
                    // TODO: Currently, we may incorrectly accept a rectangle that lies outside the loop.
                    if (edgeV) {
                        // intersects if edge.x between side.x1 and side.x2, and side.y between edge.y1 and edge.y2
                        if (   Math.min(side[0][0], side[1][0]) <  edge[0][0] && edge[0][0] <  Math.max(side[0][0], side[1][0])
                            && Math.min(edge[0][1], edge[1][1]) <= side[0][1] && side[0][1] <= Math.max(edge[0][1], edge[1][1])
                        ) {
                            continue search;
                        }
                    }
                    else {
                        // intersects if edge.y between side.y1 and side.y2, and side.x between edge.x1 and edge.x2
                        if (   Math.min(side[0][1], side[1][1]) <  edge[0][1] && edge[0][1] <  Math.max(side[0][1], side[1][1])
                            && Math.min(edge[0][0], edge[1][0]) <= side[0][0] && side[0][0] <= Math.max(edge[0][0], edge[1][0])
                        ) {
                            continue search;
                        }
                    }
                }
            }
            IO.println(rect.area);
            return;
        }
    }
}
