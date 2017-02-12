package y0001392;

import org.apache.commons.math3.util.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Main {

	enum Method {
		one, two
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 5) {
			throw new IllegalArgumentException("Must provide 4 arguments in form of: m n1 n2 (one,two)");
		}
		int runs = Integer.parseInt(args[0]);
		int m = Integer.parseInt(args[1]);
		int n1 = Integer.parseInt(args[2]);
		int n2 = Integer.parseInt(args[3]);
		Method method = Method.valueOf(args[4]);

		System.out.printf("Using parameters: %d %d %d %s\n", m, n1, n2, method.toString());

		List<String> worstpasswords = Files.readAllLines(Paths.get("500-worst-passwords-processed.txt"));
		List<Pair<String, Integer>> rockyou = parse(Files.readAllLines(Paths.get("rockyou-withcount-processed.txt")));

		// m  -- number of guesses
		// n1 -- length of password (n1 or more chars)
		// n2 -- length of passcode (exactly n2 chars)

		// n1 and n2 are known
		// use passwords and passcodes of right size randomly drawn from 500-worst-passwords-processed.txt
		// len(password) >= n1, len(passcode) == n2

		Guess guesser = null;
		switch (method) {
		case one:
			guesser = new MethodOne(m, n1, n2, worstpasswords, rockyou);
			break;
		case two:
			guesser = new MethodTwo(m, n1, n2, worstpasswords, rockyou);
			break;
		}

        final long start = System.nanoTime();

        Guess finalGuesser = guesser;
        List<String> data = IntStream.range(0, runs)
                .map(operand -> finalGuesser.guess())
                .boxed()
                .map(matches -> String.format("%d,", matches))
                .collect(toList());
        final long end = System.nanoTime() - start;
        System.out.println(end / 1E9);


        String timestamp = String.valueOf(System.currentTimeMillis());
        Files.write(Paths.get(String.format("result_%s_%d_%d_%s.txt", method.toString(), n1, n2, timestamp)), data);
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
				.collect(toList());
	}
}
