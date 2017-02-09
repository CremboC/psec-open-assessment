package y0001392;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.stream.IntStream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class Main {

	private static List<Pair<String, Integer>> parse(List<String> passwords) {
		return passwords.stream()
				.map(p -> {
					String[] s = p.replaceFirst("^ *", "").split(" ", 2);
					if (s.length == 1) {
						return new Pair<>("", Integer.parseInt(s[0]));
					}
					return new Pair<>(s[1], Integer.parseInt(s[0]));
				})
				.collect(toList());
	}


	private static void MethodOne() {

	}

	// several from password + several from passcode
	private static void MethodTwo() {

	}

	public static void main(String[] args) throws IOException {
		int m = 50_000_000;
		int n1 = 6;
		int n2 = 6;
		final int passcodePicks = 3;

		List<String> worstpasswords = Files.readAllLines(Paths.get("500-worst-passwords-processed.txt"));
		List<Pair<String, Integer>> rockyou = parse(Files.readAllLines(Paths.get("rockyou-withcount-processed.txt")));

//		List<Pair<String, Double>> normalised = normalise(rockyou);

		//		List<Integer> rockyouCounts = rockyou.getLeft();
//		List<String> rockyouPasswords = rockyou.getRight();

		// m  -- number of guesses
		// n1 -- length of password (n1 or more chars)
		// n2 -- length of passcode (exactly n2 chars)

		// n1 and n2 are known
		// use passwords and passcodes of right size randomly drawn from 500-worst-passwords-processed.txt
		// len(password) >= n1, len(passcode) == n2
//		List<String> attackPasswords = worstpasswords.stream()
//				.filter(s -> s.length() >= n1)
//				.collect(toList());
//
//		List<String> attackPasscodes = worstpasswords.stream()
//				.filter(s -> s.length() == n2)
//				.collect(toList());
//
//
//		List<Pair<String, Double>> requiredLengthPasswords = normalise(rockyou.stream()
//				.filter(p -> p.getLeft().length() >= n1)
//				.collect(toList()));
//
//
//		List<Pair<String, Double>> requiredLengthPasscodes = normalise(rockyou.stream()
//				.filter(p -> p.getLeft().length() == n2)
//				.collect(toList()));

		MethodOne methodOne = new MethodOne(m, n1, n2, worstpasswords, rockyou);
		int total1 = IntStream.range(0, 200).map(i -> methodOne.guess() ? 1 : 0).sum();
		System.out.println(total1);


//		MethodTwo methodTwo = new MethodTwo(m, n1, n2, worstpasswords, rockyou);
//		int total2 = IntStream.range(0, 100).parallel().map(i -> methodTwo.guess() ? 1 : 0).sum();
//		System.out.println(total2);


//		int total = IntStream.range(0, 100).reduce(0, (acc, i) -> {
//			// 1. pick password from rockyou
//
//
//			return acc + matches;
//		});


//		int total = IntStream.range(0, 100).map(i -> {
			// 1. pick password from rockyou

//			String password = getWeightedRandom(requiredLengthPasswords);
//			String passcode = getWeightedRandom(requiredLengthPasscodes);
//
//			List<Integer> passcodeIndices = Arrays.asList(
//					ThreadLocalRandom.current().nextInt(0, n2),
//					ThreadLocalRandom.current().nextInt(0, n2),
//					ThreadLocalRandom.current().nextInt(0, n2)
//			);
//
//			IntPredicate methodOne = j -> {
//				// 2.1 get password from 500 passwords
//				int rand1 = ThreadLocalRandom.current().nextInt(0, attackPasswords.size());
//				String attemptPassword = attackPasswords.get(rand1);
//
//				// 2.1.1 get passcode from 500
//				int rand2 = ThreadLocalRandom.current().nextInt(0, attackPasscodes.size());
//				String attemptPasscode = attackPasscodes.get(rand2);
//
//				// 2.2 test password
//				// 2.3 test passcode
//				return Objects.equals(password, attemptPassword) &&
//						passcodeIndices.stream().allMatch(k -> attemptPasscode.charAt(k) == passcode.charAt(k));
//
//			};
//
//			// 2. for m times
//			int matches = IntStream.range(0, m).parallel().filter(methodOne).boxed().collect(toList()).size();

//			return (matches == 0) ? 0 : 1;
//		}).sum();
//
//		System.out.println(total);


//		System.out.println(matches);
//
//		List<Integer> passwordIndices = Arrays.asList(
//				ThreadLocalRandom.current().nextInt(0, n1),
//				ThreadLocalRandom.current().nextInt(0, n1),
//				ThreadLocalRandom.current().nextInt(0, n1)
//		);
//
//		IntPredicate methodTwo = j -> {
//			// 2.1 get password from 500 passwords
//			int rand1 = ThreadLocalRandom.current().nextInt(0, attackPasswords.size());
//			String attemptPassword = attackPasswords.get(rand1);
//
//			// 2.1.1 get passcode from 500
//			int rand2 = ThreadLocalRandom.current().nextInt(0, attackPasscodes.size());
//			String attemptPasscode = attackPasscodes.get(rand2);
//
//
//			boolean passwordIndicesMatch = passwordIndices.stream()
//					.allMatch(i -> attemptPassword.charAt(i) == password.charAt(i));
//
//			boolean passcodeIndicesMatch = passcodeIndices.stream()
//					.allMatch(i -> attemptPasscode.charAt(i) == passcode.charAt(i));
//
//			return passwordIndicesMatch && passcodeIndicesMatch;
//
//		};
//
//		int matches2 = IntStream.range(0, m).parallel().filter(methodTwo).boxed().collect(toList()).size();
//
//		System.out.println(matches2);
	}
}
