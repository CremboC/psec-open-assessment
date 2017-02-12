package y0001392;

import org.apache.commons.math3.util.Pair;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Simultaneous extends Guess {
    Simultaneous(int m, int n1, int n2, List<String> attackSource, List<Pair<String, Integer>> consumerSource) {
        super(m, n1, n2, attackSource, consumerSource);
    }

    @Override
    public int guess() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Pair<Integer, Integer> guess2() {
        IntFunction<Pair<Integer, Integer>> run = j -> {
            // get password and passcode
            List<Integer> passwordIndices = pickIndices(n1);
            String attemptPassword = attackPasswords.get(ThreadLocalRandom.current().nextInt(0, attackPasswords.size()));

            List<Integer> passcodeIndices = pickIndices(n2);
            String attemptPasscode = attackPasscodes.get(ThreadLocalRandom.current().nextInt(0, attackPasscodes.size()));

            String password = customerPasswords.sample();
            String passcode = customerPasscodes.sample();

            boolean methodOne =
                    password.equals(attemptPassword) &&
                    passcodeIndices.stream().allMatch(k -> attemptPasscode.charAt(k) == passcode.charAt(k));

            boolean methodTwo =
                    passwordIndices.stream().allMatch(i -> attemptPassword.charAt(i) == password.charAt(i)) &&
                    passcodeIndices.stream().allMatch(i -> attemptPasscode.charAt(i) == passcode.charAt(i));

            return Pair.create(methodOne ? 1 : 0, methodTwo ? 1 : 0);
        };

        Pair<Integer, Integer> result = IntStream.range(0, m)
                .parallel()
                .mapToObj(run)
                .reduce(Pair.create(0, 0), (p1, p2) -> Pair.create(p1.getFirst() + p2.getFirst(), p1.getSecond() + p2.getSecond()));

        return result;
    }
}
