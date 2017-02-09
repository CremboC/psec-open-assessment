package y0001392;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleToIntFunction;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;


public class MethodTwo extends Guess {
    MethodTwo(int m, int n1, int n2, List<String> attackSource, List<Pair<String, Integer>> consumerSource) {
        super(m, n1, n2, attackSource, consumerSource);
    }

    @Override
    public boolean guess() {
        String password = getWeightedRandom(consumerPasswords);
        String passcode = getWeightedRandom(consumerPasscodes);

        List<Integer> passwordIndices = Arrays.asList(
                ThreadLocalRandom.current().nextInt(0, n1),
                ThreadLocalRandom.current().nextInt(0, n1),
                ThreadLocalRandom.current().nextInt(0, n1)
        );

        List<Integer> passcodeIndices = Arrays.asList(
                ThreadLocalRandom.current().nextInt(0, n2),
                ThreadLocalRandom.current().nextInt(0, n2),
                ThreadLocalRandom.current().nextInt(0, n2)
        );

        IntUnaryOperator run = j -> {
            // 2.1 get password from 500 passwords
            int rand1 = ThreadLocalRandom.current().nextInt(0, attackPasswords.size());
            String attemptPassword = attackPasswords.get(rand1);

            // 2.1.1 get passcode from 500
            int rand2 = ThreadLocalRandom.current().nextInt(0, attackPasscodes.size());
            String attemptPasscode = attackPasscodes.get(rand2);


            boolean passwordIndicesMatch = passwordIndices.stream()
                    .allMatch(i -> attemptPassword.charAt(i) == password.charAt(i));

            boolean passcodeIndicesMatch = passcodeIndices.stream()
                    .allMatch(i -> attemptPasscode.charAt(i) == passcode.charAt(i));

            return (passwordIndicesMatch && passcodeIndicesMatch) ? 1 : 0;
        };

        // 2. for m times
        int matches = IntStream.range(0, m).parallel().map(run).sum();
        return matches != 0;
    }
}
