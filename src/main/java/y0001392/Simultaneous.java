package y0001392;

import org.apache.commons.math3.util.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * Bulk-guessing of two methods simultaneously.
 */
public class Simultaneous extends Guesser {

    Simultaneous(int m, int n1, int n2, List<String> attackSource, List<Pair<String, Integer>> consumerSource) {
        super(m, n1, n2, attackSource, consumerSource);
    }

    /**
     * Using the given maximal index, finds a password of the required length.
     * @param maxIndex maximal index to match against
     * @param password starting password. If this password is of sufficient length, it is returned
     * @return A password or sufficient length or nothing, if one cannot be found.
     */
    private Optional<String> passwordOfCorrectLength(int maxIndex, String password) {
        int requiredLength = maxIndex + 1;
        if (password.length() < requiredLength) {
            if (attackSourceByLength.containsKey(requiredLength)) {
                List<String> passwords = attackSourceByLength.get(requiredLength);
                return Optional.of(passwords.get(ThreadLocalRandom.current().nextInt(0, passwords.size())));
            }

            return Optional.empty();
        }

        return Optional.of(password);
    }

    private Pair<Integer, Integer> run(int j) {
        // 1. pick customer's actual password and passcode
        String password = customerPasswords.sample();
        String passcode = customerPasscodes.sample();

        // 2. pick password and passcode we will use for attacking
        String attemptPassword = attackPasswords.get(ThreadLocalRandom.current().nextInt(0, attackPasswords.size()));
        String attemptPasscode = attackPasscodes.get(ThreadLocalRandom.current().nextInt(0, attackPasscodes.size()));

        // 3. pick 3 distinct indices for password and passcode.
        // password indices are only used for method one, while passcode indices are used for both.
        // since n1 is a lower bound, we need to use the length of the actual password instead of n1
        List<Integer> passwordIndices = pickIndices(password.length());
        List<Integer> passcodeIndices = pickIndices(n2);

        // 4.1 attempt method one
        boolean methodOne = password.equals(attemptPassword) &&
                passcodeIndices.stream().allMatch(i -> attemptPasscode.charAt(i) == passcode.charAt(i));

        // 4.2 attempt method two.
        // At this point we may have chosen a password that is too short.
        // While we don't know the actual length of the password, we can clearly see the maximal index required.
        // For instance, if the authentication method asks for characters 1, 4 and 6 (1-indexed),
        // we just need a password of length 6, even if the password is actually of length 8.
        // In order to have better chances, we simply
        int maxIndex = Collections.max(passwordIndices);
        // ensure length is at least (maxIndex + 1)
        boolean methodTwo =  passwordOfCorrectLength(maxIndex, attemptPassword)
                .map(actualAttemptPassword ->
                        passwordIndices.stream().allMatch(i -> actualAttemptPassword.charAt(i) == password.charAt(i)) &&
                                passcodeIndices.stream().allMatch(i -> attemptPasscode.charAt(i) == passcode.charAt(i)))
                .orElse(false);

        return Pair.create(methodOne ? 1 : 0, methodTwo ? 1 : 0);
    }

    @Override
    public Pair<Integer, Integer> guess() {
        return IntStream.range(0, m)
                .parallel()
                .mapToObj(this::run)
                .reduce(Pair.create(0, 0), (p1, p2) -> Pair.create(p1.getFirst() + p2.getFirst(), p1.getSecond() + p2.getSecond()));
    }
}
