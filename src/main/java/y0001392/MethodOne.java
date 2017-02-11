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
    public int guess() {
        IntUnaryOperator run = j -> {
            // get password and passcode
            int rand1 = ThreadLocalRandom.current().nextInt(0, attackPasswords.size());
            String attemptPassword = attackPasswords.get(rand1);

            int rand2 = ThreadLocalRandom.current().nextInt(0, attackPasscodes.size());
            String attemptPasscode = attackPasscodes.get(rand2);

            List<Integer> passcodeIndices = ThreadLocalRandom.current()
                    .ints(0, n2)
                    .distinct()
                    .limit(3)
                    .boxed()
                    .collect(toList());

            String password = getWeightedRandom(customerPasswords);
            String passcode = getWeightedRandom(customerPasscodes);

            return (password.equals(attemptPassword) && passcodeIndices.stream()
                    .allMatch(k -> attemptPasscode.charAt(k) == passcode.charAt(k)))
                    ? 1 : 0;
        };

        return IntStream.range(0, m).parallel().map(run).sum();
    }
}
