package advent.of.code;

import java.util.stream.Stream;

public class Day02 {
    void main() throws Exception {
        String input = FileIO.read("/day02.txt");
        part1(input);
        part2(input);
    }
    
    void part1(String input) {
        var ranges = Stream.of(input.split(","))
            .map(range -> range.split("-"))
            .toArray(String[][]::new);
        long sum = 0;
        for (var range : ranges) {
            // Consider that every invalid id can be sequenced based on its first half
            //   eg: 1 -> 11, 2 -> 22, ... 9 -> 99, 10 -> 1010, 11 -> 1111, ...
            // We can use this to enumerate all possible invalid ids in the range, and sum them
            long seq = nextInvalidSeq(range[0]);
            long end = Long.parseLong(range[1]);
            for (;; seq++) {
                long id = Long.parseLong(seq + "" + seq);
                if (id > end) break;
                sum += id;
            }
        }
        IO.println(sum);
    }
    
    void part2(String input) {
        var ranges = Stream.of(input.split(","))
            .map(range -> range.split("-"))
            .toArray(String[][]::new);
        long sum = 0;
        for (var range : ranges) {
            // Less clever than part 1: Just go through every number in the range and test it.
            // To test, check whether the full number can be made by repeatedly concatenating a prefix, of any length.
            // To speed things up, we use power of 10 modulo so we can compare ints rather than substrings.
            long beg = Long.parseLong(range[0]);
            long end = Long.parseLong(range[1]);
            for (long id = beg; id <= end; id++) {
                test: for (long mod = 10; mod < id; mod *= 10) {
                    long i = id;
                    long seq = i % mod;
                    if (seq < mod/10) { // Check if sequence is 0-padded; ids do not have leading zeros.
                        continue;
                    }
                    while ((i /= mod) > 0) {
                        if (i % mod != seq) {
                            continue test;
                        }
                    }
                    sum += id;
                    break;
                }
            }
        }
        IO.println(sum);
    }
    
    long nextInvalidSeq(String id) {
        int len = id.length();
        if (len % 2 == 0) {
            long head = Long.parseLong(id.substring(0, len/2));
            long tail = Long.parseLong(id.substring(len/2, len));
            return tail > head ? head+1 : head;
        }
        return Math.powExact(10, len/2);
    }
}
