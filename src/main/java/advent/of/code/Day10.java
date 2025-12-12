package advent.of.code;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Day10 {
    void main() throws Exception {
        String input = FileIO.read("/day10.txt");
        part1(input);
        part2(input);
    }
    
    void part1(String input) {
        record Machine(int target, int[] buttons) {}
        List<Machine> machines = input.lines()
            .map(line -> {
                String[] parts = line.split(" ");
                int target = IntStream.range(1, parts[0].length()-1)
                    .map(i -> parts[0].charAt(i) == '#' ? (1 << (i-1)) : 0)
                    .sum();
                int[] buttons = Arrays.stream(parts, 1, parts.length-1)
                    .mapToInt(btn -> Arrays.stream(btn.substring(1, btn.length()-1).split(","))
                        .mapToInt(Integer::parseInt)
                        .map(i -> (1 << i))
                        .sum()
                    )
                    .toArray();
                return new Machine(target, buttons);
            })
            .toList();
        
        record State(int state, int steps) {}
        int totalSteps = 0;
        
        for (Machine machine : machines) {
            Deque<State> states = new ArrayDeque<>();
            states.offer(new State(0, 0));
            loop: for (;;) {
                State state = states.poll();
                for (int btn : machine.buttons) {
                    int nextState = state.state ^ btn;
                    int nextSteps = state.steps + 1;
                    if (nextState == machine.target) {
                        totalSteps += nextSteps;
                        break loop;
                    }
                    states.offer(new State(nextState, nextSteps));
                }
            }
        }
        
        IO.println(totalSteps);
    }
    
    void part2(String input) {
        //AtomicInteger i = new AtomicInteger();
        int totalPresses = input.lines().parallel()
            .mapToInt(line -> {
                int[][] data = Arrays.stream(line.split(" "))
                    .skip(1)
                    .map(s -> Arrays.stream(s.substring(1, s.length()-1).split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray()
                    )
                    .toArray(int[][]::new);
                int[] joltages = data[data.length-1];
                int[][] buttons = Arrays.copyOfRange(data, 0, data.length-1);
                //IO.println(i.getAndIncrement());
                return rec(buttons, joltages, 0, Integer.MAX_VALUE);
            })
            .sum();
        IO.println(totalPresses);
    }
    
    int rec(int[][] buttons, int[] joltages, int usedButtonMask, int minPresses) {
        record Cmp(int i, int joltage, long nButtons) { }
        Cmp min = IntStream.range(0, joltages.length)
            .filter(i -> joltages[i] > 0)
            .mapToObj(i -> new Cmp(
                i, -joltages[i],
                IntStream.range(0, buttons.length)
                    .filter(b -> (usedButtonMask & (1 << b)) == 0 && Arrays.binarySearch(buttons[b], i) >= 0)
                    .count()
            ))
            .min(Comparator.comparingLong(Cmp::nButtons).thenComparingInt(Cmp::joltage))
            .orElse(null);
        if (min == null) {
            return 0;
        }
        int joltage = -min.joltage;
        if (min.nButtons == 0 || joltage >= minPresses) {
            return Integer.MAX_VALUE;
        }
        int[] unusedButtons = IntStream.range(0, buttons.length)
            .filter(b -> (usedButtonMask & (1 << b)) == 0 && Arrays.binarySearch(buttons[b], min.i) >= 0)
            .toArray();
        int nextUsedButtonMask = usedButtonMask;
        for (int b : unusedButtons) {
            nextUsedButtonMask |= (1 << b);
        }
        int[] presses = new int[unusedButtons.length+1];
        int[] sum_right = new int[unusedButtons.length+1];
        presses[0] = joltage+1;
        presses[1] = -1;
        var partitioner = new Object(){
            boolean next() {
                if (presses[unusedButtons.length] > 0) {
                    return false;
                }
                if (presses[0] > 0) {
                    presses[0]--;
                    presses[1]++;
                }
                else {
                    for (int i = 1; i < unusedButtons.length; i++) {
                        presses[i] = 0;
                        if (presses[i+1] < joltage - sum_right[i+1]) {
                            presses[i+1]++;
                            presses[0] = joltage - (++sum_right[i]);
                            Arrays.fill(sum_right, 1, i, sum_right[i]);
                            break;
                        }
                    }
                }
                return true;
            }
        };
        
        loop: while (partitioner.next()) {
            int[] nextJoltages = joltages.clone();
            for (int i = 0; i < unusedButtons.length; i++) {
                if (presses[i] == 0) {
                    continue;
                }
                for (int jj : buttons[unusedButtons[i]]) {
                    if (nextJoltages[jj] < presses[i]) {
                        continue loop;
                    }
                    nextJoltages[jj] -= presses[i];
                }
            }
            int remainingPresses = rec(buttons, nextJoltages, nextUsedButtonMask, minPresses);
            if (remainingPresses != Integer.MAX_VALUE) {
                minPresses = Math.min(minPresses, joltage + remainingPresses);
            }
        }
        
        return minPresses;
    }
}
