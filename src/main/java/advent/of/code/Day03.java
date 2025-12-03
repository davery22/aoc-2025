package advent.of.code;

public class Day03 {
    void main() throws Exception {
        String input = FileIO.read("/day03.txt");
        part1(input);
        part2(input);
    }
    
    void part1(String input) {
        int[][] banks = parseBanks(input);
        int joltage = 0;
        for (int[] bank : banks) {
            int i = 0, j = bank.length-1, iMax = i, jMax = j;
            while (++i < jMax) {
                if (bank[i] > bank[iMax]) iMax = i;
            }
            while (--j > iMax) {
                if (bank[j] > bank[jMax]) jMax = j;
            }
            joltage += 10 * bank[iMax] + bank[jMax];
        }
        IO.println(joltage);
    }
    
    void part2(String input) {
        int[][] banks = parseBanks(input);
        long joltage = 0;
        for (int[] bank : banks) {
            // Just generalizing part 1 to go from N=2 to N=12
            int n = 12;
            int[] indexes = new int[n];
            for (int i = 0; i < n; i++) {
                int from = i == 0 ? 0 : indexes[i-1]+1;
                int to = bank.length-n+1+i;
                indexes[i] = indexOfMax(bank, from, to);
            }
            long curr = 0;
            for (int index : indexes) {
                curr = curr * 10 + bank[index];
            }
            joltage += curr;
        }
        IO.println(joltage);
    }
    
    int indexOfMax(int[] arr, int from, int to) {
        int iMax = from;
        for (int i = from; i < to; i++) {
            if (arr[i] > arr[iMax]) iMax = i;
        }
        return iMax;
    }
    
    int[][] parseBanks(String input) {
        return input.lines()
            .map(line -> line.chars().map(c -> c - '0').toArray())
            .toArray(int[][]::new);
    }
}
