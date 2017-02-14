package y0001392;

import org.apache.commons.math3.util.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Main {

	public static void main(String[] args) throws IOException {
		if (args.length != 4) {
			throw new IllegalArgumentException("Must provide 4 arguments in form of: number_of_runs m n1 n2");
		}
		int runs = Integer.parseInt(args[0]);
		int m = Integer.parseInt(args[1]);
		int n1 = Integer.parseInt(args[2]);
		int n2 = Integer.parseInt(args[3]);

		List<String> attackSource = Files.readAllLines(Paths.get("500-worst-passwords-processed.txt"));
		List<Pair<String, Integer>> customerSource = parse(Files.readAllLines(Paths.get("rockyou-withcount-processed.txt")));

		// m  -- number of guesses
		// n1 -- length of password (n1 or more chars)
		// n2 -- length of passcode (exactly n2 chars)

        System.out.printf("Using parameters: %d %d %d for %d runs\n", m, n1, n2, runs);

        final long start = System.nanoTime();

        Guesser guesser = new Simultaneous(m, n1, n2, attackSource, customerSource);
        List<String> data = IntStream.range(0, runs)
                .mapToObj(operand -> guesser.guess())
                .map(p -> String.format("%d,%d,", p.getFirst(), p.getSecond()))
                .collect(toList());

        final long end = System.nanoTime() - start;
        System.out.println(end / 1E9);

        String timestamp = String.valueOf(System.currentTimeMillis());
        String filename = String.format("result_%d_%d_%s.txt", n1, n2, timestamp);
        System.out.printf("A file named %s has been written with the data.\n", filename);
        Files.write(Paths.get(filename), data);
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
