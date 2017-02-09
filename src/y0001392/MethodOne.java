package y0001392;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

// whole password + several from passcode
public class MethodOne extends Guess {
    public MethodOne(int m, int n1, int n2, List<String> attackPasswords, List<Pair<String, Integer>> consumerPasswords) {
        super(m, n1, n2, attackPasswords, consumerPasswords);
    }

    @Override
    public boolean guess() {
        String password = getWeightedRandom(consumerPasswords);
        String passcode = getWeightedRandom(consumerPasscodes);

        List<Integer> passcodeIndices = Arrays.asList(
                ThreadLocalRandom.current().nextInt(0, n2),
                ThreadLocalRandom.current().nextInt(0, n2),
                ThreadLocalRandom.current().nextInt(0, n2)
        );

        IntPredicate methodOne = j -> {
            // 2.1 get password from 500 passwords
            int rand1 = ThreadLocalRandom.current().nextInt(0, attackPasswords.size());
            String attemptPassword = attackPasswords.get(rand1);

            // 2.1.1 get passcode from 500
            int rand2 = ThreadLocalRandom.current().nextInt(0, attackPasscodes.size());
            String attemptPasscode = attackPasscodes.get(rand2);

            // 2.2 test password
            boolean passwordsFullyMatch = Objects.equals(password, attemptPassword);
            // 2.3 test passcode
            boolean passcodeIndicesMatch = passcodeIndices.stream()
                    .allMatch(k -> attemptPasscode.charAt(k) == passcode.charAt(k));

            return passwordsFullyMatch && passcodeIndicesMatch;
        };

        // 2. for m times
        int matches = IntStream.range(0, m).parallel().filter(methodOne).boxed().collect(toList()).size();
        return matches != 0;
    }
}
