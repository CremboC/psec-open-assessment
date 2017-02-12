package y0001392;

import org.apache.commons.math3.util.Pair;

import java.util.Collections;
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
    public int guess() {
        IntUnaryOperator run = j -> {
            // get password and passcode
            String attemptPassword = attackPasswords.get(ThreadLocalRandom.current().nextInt(0, attackPasswords.size()));

            List<Integer> passcodeIndices = pickIndices(n2);
            String attemptPasscode = attackPasscodes.get(ThreadLocalRandom.current().nextInt(0, attackPasscodes.size()));

            String password = customerPasswords.sample();
            String passcode = customerPasscodes.sample();

            return (password.equals(attemptPassword) && passcodeIndices.stream()
                    .allMatch(k -> attemptPasscode.charAt(k) == passcode.charAt(k)))
                    ? 1 : 0;
        };

        return IntStream.range(0, m).parallel().map(run).sum();
    }
}
