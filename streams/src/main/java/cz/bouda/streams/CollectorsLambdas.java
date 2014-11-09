package cz.bouda.streams;

import java.util.*;
import java.util.stream.*;

import cz.bouda.streams.domain.*;
import cz.bouda.streams.trigger.Trigger;

public class CollectorsLambdas {

	private static List<Person> persons = Arrays.asList(new Person("Max", 18), new Person("Peter", 23), new Person("Pamela", 23), new Person("David", 12));

	private static List<Foo> foos = new ArrayList<>();

	public static void main(String... args) {
		IntStream.range(1, 4).forEach(i -> foos.add(new Foo("Foo" + i)));

		foos.forEach(f -> IntStream.range(1, 4).forEach(i -> f.bars.add(new Bar("Bar" + i + " <- " + f.name))));

		Trigger.run(CollectorsLambdas.class);
	}

	/*
	 * We want to transform all persons of the stream into a single string
	 * consisting of all names in upper letters separated by the | pipe
	 * character. In order to achieve this we create a new collector via
	 * Collector.of(). We have to pass the four ingredients of a collector: a
	 * supplier, an accumulator, a combiner and a finisher.
	 */
	public static void single_upper_string() {
		Collector<Person, StringJoiner, String> personNameCollector = Collector.of(() -> new StringJoiner(" | "), // supplier
				(j, p) -> j.add(p.name.toUpperCase()), // accumulator
				(j1, j2) -> j1.merge(j2), // combiner
				StringJoiner::toString); // finisher

		String names = persons.stream().collect(personNameCollector);

		System.out.println(names);
	}

	public static void flat_map_basic() {
		foos.stream().flatMap(f -> f.bars.stream()).forEach(b -> System.out.println(b.name));
	}

	public static void flat_map_basic_2() {
		IntStream.range(1, 4).mapToObj(i -> new Foo("Foo" + i))
				.peek(f -> IntStream.range(1, 4).mapToObj(i -> new Bar("Bar" + i + " <- " + f.name)).forEach(f.bars::add)).flatMap(f -> f.bars.stream())
				.forEach(b -> System.out.println(b.name));
	}

	public static void null_checks_bad() {
		Outer outer = new Outer();
		if (outer != null && outer.nested != null && outer.nested.inner != null) {
			System.out.println(outer.nested.inner.foo);
		}
	}

	public static void null_checks_success() {
		Optional.of(new Outer()).flatMap(o -> Optional.ofNullable(o.nested)).flatMap(n -> Optional.ofNullable(n.inner))
				.flatMap(i -> Optional.ofNullable(i.foo)).ifPresent(System.out::println);
	}

	/**
	 * Hierarchy of the classes
	 **/
	private static class Outer {
		Nested nested;
	}

	private static class Nested {
		Inner inner;
	}

	private static class Inner {
		String foo;
	}
}
