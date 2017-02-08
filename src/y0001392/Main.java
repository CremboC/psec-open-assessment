package y0001392;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleToIntFunction;
import java.util.function.Function;
import java.util.function.IntToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

public class Main {

	static class Pair<K, V> {
		private final K left;
		private final V right;

		public Pair(K left, V right) {
			this.left = left;
			this.right = right;
		}

		public K getLeft() {
			return left;
		}

		public V getRight() {
			return right;
		}
	}

	private static List<String> exactly(List<String> passwords, int size) {
		return passwords.stream().filter(s -> s.length() == size).collect(Collectors.toList());
	}

	private static List<String> atleast(List<String> passwords, int size) {
		return passwords.stream().filter(s -> s.length() >= size).collect(Collectors.toList());
	}

	private static List<Pair<String, Integer>> parse(List<String> passwords) {
		return passwords.stream()
				.map(p -> {
					String[] s = p.replaceFirst("^ *", "").split(" ", 2);
					if (s.length == 1) {
						return new Pair<>("", Integer.parseInt(s[0]));
					}
					return new Pair<>(s[1], Integer.parseInt(s[0]));
				})
				.collect(Collectors.toList());
	}

	// from http://stackoverflow.com/questions/6737283/weighted-randomness-in-java
	public static <E> E getWeightedRandom(List<Pair<E, Double>> weights) {
		return weights
				.stream()
				.map(e -> new Pair<>(e.getLeft(), -Math.log(ThreadLocalRandom.current().nextDouble()) / e.getRight()))
				.min(comparing(Pair::getRight))
				.orElseThrow(IllegalArgumentException::new).getLeft();
	}

	public static <E> List<Pair<E, Double>> normalise(List<Pair<E, Integer>> items) {
		int maxVal = items.stream().max(comparing(Pair::getRight)).map(Pair::getRight).get();
		int minVal = items.stream().min(comparing(Pair::getRight)).map(Pair::getRight).get();

		IntToDoubleFunction normalise = n -> ((double) (n - minVal) / (maxVal - minVal));
		return items.stream()
				.map(i -> new Pair<>(i.getLeft(), normalise.applyAsDouble(i.getRight())))
				.collect(Collectors.toList());
	}

	// whole password + several from passcode
	private static void MethodOne() {

	}

	// several from password + several from passcode
	private static void MethodTwo() {

	}

	public static void main(String[] args) throws IOException {
		int m = 50_000_000;
		int n1 = 6;
		int n2 = 6;

		List<String> worstpasswords = Files.readAllLines(Paths.get("500-worst-passwords-processed.txt"));
		List<Pair<String, Integer>> rockyou = parse(Files.readAllLines(Paths.get("rockyou-withcount-processed.txt")));

		List<Pair<String, Double>> normalised = normalise(rockyou);

		//		List<Integer> rockyouCounts = rockyou.getLeft();
//		List<String> rockyouPasswords = rockyou.getRight();

		// m  -- number of guesses
		// n1 -- length of password (n1 or more chars)
		// n2 -- length of passcode (exactly n2 chars)

		// n1 and n2 are known
		// use passwords and passcodes of right size randomly drawn from 500-worst-passwords-processed.txt
		// len(password) >= n1, len(passcode) == n2
		List<String> attackPasswords = atleast(worstpasswords, n1);
		List<String> attackPasscodes = exactly(worstpasswords, n2);

		// actual passwords and passcodes randomly drawn from rockyou-withcount-processed.txt
		//		ThreadLocalRandom.current().nextInt(, max + 1);
//		List<String> actualPasswords = atleast(rockyouPasswords, n1);
//		List<String> actualPasscodes = exactly(rockyouPasswords, n2);

		// 1. pick password from rockyou
		String password = getWeightedRandom(normalised.stream().filter(p -> p.getLeft().length() == n1).collect(Collectors.toList()));
		// 2. for m times
		int matches = IntStream.range(0, m).filter(i -> {
			// 2.1 attempt password from 500
			int rand = ThreadLocalRandom.current().nextInt(0, attackPasswords.size());
			String attempt = attackPasswords.get(rand);
			return Objects.equals(password, attempt);
		}).boxed().collect(Collectors.toList()).size();

		System.out.println(matches);

		//		}


//
//		getWeightedRandom()

	}
}
