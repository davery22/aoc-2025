package advent.of.code;

import java.util.*;

public class Day08 {
    void main() throws Exception {
        String input = FileIO.read("/day08.txt");
        part1(input);
        part2(input);
    }
    
    record Box(long x, long y, long z) {}
    record Connection(int i, int j, double distance) {}
    
    void part1(String input) {
        int pairs = 1000;
        List<Box> boxes = parseBoxes(input);
        List<Connection> connections = buildConnections(boxes);
        Map<Integer, Integer> boxToGroup = new HashMap<>();
        for (int i = 0; i < boxes.size(); i++) {
            union(boxToGroup, i, i);
        }
        for (var conn : connections.subList(0, pairs)) {
            union(boxToGroup, conn.i, conn.j);
        }
        Map<Integer, Integer> groupToCount = new HashMap<>();
        for (int i = 0; i < boxes.size(); i++) {
            groupToCount.merge(find(boxToGroup, i), 1, Integer::sum);
        }
        long product = groupToCount.values().stream()
            .sorted(Comparator.reverseOrder())
            .mapToLong(i -> i)
            .limit(3)
            .reduce(1, (x,y) -> x*y);
        IO.println(product);
    }
    
    void part2(String input) {
        List<Box> boxes = parseBoxes(input);
        List<Connection> connections = buildConnections(boxes);
        Map<Integer, Integer> boxToGroup = new HashMap<>();
        for (int i = 0; i < boxes.size(); i++) {
            union(boxToGroup, i, i);
        }
        loop: for (var conn : connections) {
            union(boxToGroup, conn.i, conn.j);
            int g0 = find(boxToGroup, 0);
            for (int i = 1; i < boxes.size(); i++) {
                if (g0 != find(boxToGroup, i)) {
                    continue loop;
                }
            }
            IO.println(boxes.get(conn.i).x * boxes.get(conn.j).x);
            return;
        }
    }
    
    List<Box> parseBoxes(String input) {
        return input.lines()
            .map(line -> Arrays.stream(line.split(",")).mapToLong(Long::parseLong).toArray())
            .map(cs -> new Box(cs[0], cs[1], cs[2]))
            .toList();
    }
    
    List<Connection> buildConnections(List<Box> boxes) {
        List<Connection> connections = new ArrayList<>();
        for (int i = 0; i < boxes.size(); i++) {
            for (int j = i+1; j < boxes.size(); j++) {
                connections.add(new Connection(i, j, dist(boxes.get(i), boxes.get(j))));
            }
        }
        connections.sort(Comparator.comparing(Connection::distance));
        return connections;
    }
    
    double dist(Box a, Box b) {
        return Math.sqrt(Math.powExact(a.x-b.x, 2) + Math.powExact(a.y-b.y, 2) + Math.powExact(a.z-b.z, 2));
    }
    
    int find(Map<Integer, Integer> set, int id) {
        int parentId = set.computeIfAbsent(id, _ -> id);
        if (parentId != id) {
            set.put(id, parentId = find(set, parentId));
        }
        return parentId;
    }
    
    void union(Map<Integer, Integer> set, int a, int b) {
        a = find(set, a);
        b = find(set, b);
        set.put(Math.max(a, b), Math.min(a, b));
    }
}
