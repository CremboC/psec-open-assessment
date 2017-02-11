package y0001392;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

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

			String password = getWeightedRandom(customerPasswords);
			String passcode = getWeightedRandom(customerPasscodes);

			List<Integer> passwordIndices = ThreadLocalRandom.current()
					.ints(0, n2)
					.distinct()
					.limit(3)
					.boxed()
					.collect(toList());

			List<Integer> passcodeIndices = ThreadLocalRandom.current()
					.ints(0, n2)
					.distinct()
					.limit(3)
					.boxed()
					.collect(toList());

			return (passwordIndices.stream().allMatch(i -> attemptPassword.charAt(i) == password.charAt(i)) &&
                    passcodeIndices.stream().allMatch(i -> attemptPasscode.charAt(i) == passcode.charAt(i)))
                    ? 1 : 0;
		};

		return IntStream.range(0, m).parallel().map(run).sum();
    }
}
