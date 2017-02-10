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

    private class Attempt {
		private final String password;
		private final String passcode;

		Attempt(String password, String passcode) {
			this.password = password;
			this.passcode = passcode;
		}

		int run(int j) {
			// 2.1 get password from 500 passwords
			int rand1 = ThreadLocalRandom.current().nextInt(0, attackPasswords.size());
			String attemptPassword = attackPasswords.get(rand1);

			// 2.1.1 get passcode from 500
			int rand2 = ThreadLocalRandom.current().nextInt(0, attackPasscodes.size());
			String attemptPasscode = attackPasscodes.get(rand2);

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

			boolean passwordIndicesMatch = passwordIndices.stream()
					.allMatch(i -> attemptPassword.charAt(i) == password.charAt(i));

			boolean passcodeIndicesMatch = passcodeIndices.stream()
					.allMatch(i -> attemptPasscode.charAt(i) == passcode.charAt(i));

			return (passwordIndicesMatch && passcodeIndicesMatch) ? 1 : 0;
		}
	}

    @Override
    public Triple<Integer, String, String> guess() {
        String password = getWeightedRandom(customerPasswords);
        String passcode = getWeightedRandom(customerPasscodes);

//		System.out.printf("Cracking %s and %s\n", password, passcode);

//        IntUnaryOperator run = j -> {

//        };

		Attempt attempt = new Attempt(password, passcode);
		// 2. for m times
		int matches = IntStream.range(0, m).parallel().map(attempt::run).sum();
		return new Triple<>(matches, password, passcode);
    }
}
