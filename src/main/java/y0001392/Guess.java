package y0001392;


import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntToDoubleFunction;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public abstract class Guess {
    final int m;
    final int n1;
    final int n2;

    final List<String> attackPasswords;
    final List<String> attackPasscodes;

    final List<Pair<String, Double>> customerPasscodes;
    final List<Pair<String, Double>> customerPasswords;


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

        customerPasswords = normalise(consumerSource.stream()
                .filter(p -> p.getLeft().length() >= n1)
                .collect(toList()));

        customerPasscodes = normalise(consumerSource.stream()
                .filter(p -> p.getLeft().length() == n2)
                .collect(toList()));
    }

    // from http://stackoverflow.com/questions/6737283/weighted-randomness-in-java
    <E> E getWeightedRandom(List<Pair<E, Double>> weights) {
        return weights
                .stream()
                .map(e -> new Pair<>(e.getLeft(), -Math.log(ThreadLocalRandom.current().nextDouble()) / e.getRight()))
                .min(comparing(Pair::getRight))
                .orElseThrow(IllegalArgumentException::new).getLeft();
    }

    private <E> List<Pair<E, Double>> normalise(List<Pair<E, Integer>> items) {
        int maxVal = items.stream().max(comparing(Pair::getRight)).map(Pair::getRight).orElseThrow(IllegalStateException::new);
        int minVal = items.stream().min(comparing(Pair::getRight)).map(Pair::getRight).orElseThrow(IllegalStateException::new);

        IntToDoubleFunction normalise = n -> ((double) (n - minVal) / (maxVal - minVal));
        return items.stream()
                .map(i -> new Pair<>(i.getLeft(), normalise.applyAsDouble(i.getRight())))
                .collect(toList());
    }

    public abstract Triple<Integer, String, String> guess();
}
