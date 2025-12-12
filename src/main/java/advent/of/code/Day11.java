package advent.of.code;

import java.util.*;
import java.util.stream.Collectors;

public class Day11 {
    void main() throws Exception {
        String input = FileIO.read("/day11.txt");
        part1(input);
        part2(input);
    }
    
    void part1(String input) {
        Map<String, Set<String>> graph = parseGraph(input);
        Map<String, Long> counts = new HashMap<>();
        dfs1("you", graph, counts);
        IO.println(counts.get("you"));
    }
    
    void part2(String input) {
        Map<String, Set<String>> graph = parseGraph(input);
        Map<String, State> states = new HashMap<>();
        dfs2("svr", graph, states);
        IO.println(states.get("svr").complete);
    }
    
    Map<String, Set<String>> parseGraph(String input) {
        return input.lines()
            .map(line -> line.split(":? "))
            .collect(Collectors.groupingBy(
                p -> p[0],
                Collectors.flatMapping(
                    p -> Arrays.stream(p, 1, p.length),
                    Collectors.toSet()
                )
            ));
    }
    
    void dfs1(String curr, Map<String, Set<String>> graph, Map<String, Long> counts) {
        // If we have been here before, do nothing
        // Else, init my count, then visit each of my children and add their counts to mine
        if (counts.get(curr) != null) {
            return;
        }
        if ("out".equals(curr)) {
            counts.put(curr, 1L);
            return;
        }
        counts.put(curr, 0L);
        for (String e : graph.getOrDefault(curr, Set.of())) {
            dfs1(e, graph, counts);
            counts.merge(curr, counts.get(e), Long::sum);
        }
    }
    
    static class State {
        long outOnly;
        long dacOutOnly;
        long fftOutOnly;
        long complete;
    }
    
    void dfs2(String curr, Map<String, Set<String>> graph, Map<String, State> states) {
        if (states.get(curr) != null) {
            return;
        }
        var state = new State();
        states.put(curr, state);
        if ("out".equals(curr)) {
            state.outOnly++;
            return;
        }
        for (String e : graph.getOrDefault(curr, Set.of())) {
            dfs2(e, graph, states);
            var childState = states.get(e);
            state.outOnly += childState.outOnly;
            state.dacOutOnly += childState.dacOutOnly;
            state.fftOutOnly += childState.fftOutOnly;
            state.complete += childState.complete;
        }
        if ("dac".equals(curr)) {
            state.dacOutOnly += state.outOnly;
            state.complete += state.fftOutOnly;
            state.outOnly = state.fftOutOnly = 0;
        }
        if ("fft".equals(curr)) {
            state.fftOutOnly += state.outOnly;
            state.complete += state.dacOutOnly;
            state.outOnly = state.dacOutOnly = 0;
        }
    }
}
