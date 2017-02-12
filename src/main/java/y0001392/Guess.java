package y0001392;


import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntToDoubleFunction;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public abstract class Guess {
    final int m;
    final int n1;
    final int n2;

    public final int PASSCODE_INDICES = 3;

    final List<String> attackPasswords;
    final List<String> attackPasscodes;

    final EnumeratedDistribution<String> customerPasscodes;
    final EnumeratedDistribution<String> customerPasswords;

    Guess(int m, int n1, int n2, List<String> attackSource, List<Pair<String, Integer>> consumerSource) {
        this.m = m;
        this.n1 = n1;
        this.n2 = n2;

        attackPasswords = attackSource.stream()
                .filter(s -> s.length() >= n1)
                .collect(toList());

        attackPasscodes = attackSource.stream()
                .filter(s -> s.length() == n2)
                .collect(toList());

        List<Pair<String, Double>> normalisedPasswords = normalise(consumerSource.stream()
                .filter(p -> p.getFirst().length() >= n1)
                .collect(toList()));

        customerPasswords = new EnumeratedDistribution<>(normalisedPasswords);

        List<Pair<String, Double>> normalisedPasscodes = normalise(consumerSource.stream()
                .filter(p -> p.getFirst().length() == n2)
                .collect(toList()));

        customerPasscodes = new EnumeratedDistribution<>(normalisedPasscodes);
    }

    private <E> List<Pair<E, Double>> normalise(List<Pair<E, Integer>> items) {
        int maxVal = items.stream().max(comparing(Pair::getSecond)).map(Pair::getSecond).orElseThrow(IllegalStateException::new);
        int minVal = items.stream().min(comparing(Pair::getSecond)).map(Pair::getSecond).orElseThrow(IllegalStateException::new);

        IntToDoubleFunction normalise = n -> ((double) (n - minVal) / (maxVal - minVal));
        return items.stream()
                .map(i -> new Pair<>(i.getFirst(), normalise.applyAsDouble(i.getSecond())))
                .collect(toList());
    }

    List<Integer> pickIndices(int bound) {
        List<Integer> numbers = new ArrayList<>(bound);
        for (int i = 0; i < PASSCODE_INDICES; i++) {
            int v;
            do {
                v = ThreadLocalRandom.current().nextInt(0, bound);
            } while (numbers.contains(v));
            numbers.add(v);
        }

        return numbers;
    }

    public abstract int guess();
    public abstract Pair<Integer, Integer> guess2();
}
