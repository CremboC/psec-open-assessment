package y0001392;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

// whole password + several from passcode
public class MethodOne extends Guess {
    MethodOne(int m, int n1, int n2, List<String> attackPasswords, List<Pair<String, Integer>> consumerPasswords) {
        super(m, n1, n2, attackPasswords, consumerPasswords);
    }

    @Override
    public Triple<Integer, String, String> guess() {
//        System.out.println();

        String password = getWeightedRandom(customerPasswords);
        String passcode = getWeightedRandom(customerPasscodes);

//        System.out.printf("Cracking %s and %s\n", password, passcode);

        IntUnaryOperator run = j -> {
            // 2.1 get password from 500 passwords
            int rand1 = ThreadLocalRandom.current().nextInt(0, attackPasswords.size());
            String attemptPassword = attackPasswords.get(rand1);

            // 2.1.1 get passcode from 500
            int rand2 = ThreadLocalRandom.current().nextInt(0, attackPasscodes.size());
            String attemptPasscode = attackPasscodes.get(rand2);

            List<Integer> passcodeIndices = ThreadLocalRandom.current()
                    .ints(0, n2)
                    .distinct()
                    .limit(3)
                    .boxed()
                    .collect(toList());

            // 2.2 test password
            boolean passwordsFullyMatch = password.equals(attemptPassword);
            // 2.3 test passcode
            boolean passcodeIndicesMatch = passcodeIndices.stream()
                    .allMatch(k -> attemptPasscode.charAt(k) == passcode.charAt(k));

            return (passwordsFullyMatch && passcodeIndicesMatch) ? 1 : 0;
        };

        // 2. for m times
        int matches = IntStream.range(0, m).parallel().map(run).sum();
        return new Triple<>(matches, password, passcode);
    }
}
