package y0001392;

import org.apache.commons.math3.util.Pair;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

public class MethodTwo extends Guess {
    MethodTwo(int m, int n1, int n2, List<String> attackSource, List<Pair<String, Integer>> consumerSource) {
        super(m, n1, n2, attackSource, consumerSource);
    }

    @Override
    public int guess() {
        IntUnaryOperator run = j -> {
			// get password and passcode
			int rand1 = ThreadLocalRandom.current().nextInt(0, attackPasswords.size());
			String attemptPassword = attackPasswords.get(rand1);

			int rand2 = ThreadLocalRandom.current().nextInt(0, attackPasscodes.size());
			String attemptPasscode = attackPasscodes.get(rand2);

			String password = customerPasswords.sample();
			String passcode = customerPasscodes.sample();

			List<Integer> passwordIndices = pickIndices(n1);
			List<Integer> passcodeIndices = pickIndices(n2);

			return (passwordIndices.stream().allMatch(i -> attemptPassword.charAt(i) == password.charAt(i)) &&
                    passcodeIndices.stream().allMatch(i -> attemptPasscode.charAt(i) == passcode.charAt(i)))
                    ? 1 : 0;
		};

		return IntStream.range(0, m).parallel().map(run).sum();
    }
}
