package cz.bouda.streams;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cz.bouda.streams.domain.Person;

public class Advanced {

	private static List<Person> persons =
			Arrays.asList(
					new Person("Max", 18),
					new Person("Peter", 23),
					new Person("Pamela", 23),
					new Person("David", 12));

	public static void main(String... args) {
		Trigger.run(Advanced.class);
	}

	/*
	 * Collect is an extremely useful terminal operation to transform the
	 * elements of the stream into a different kind of result, e.g. a List, Set
	 * or Map. Collect accepts a Collector which consists of four different
	 * operations: a supplier, an accumulator, a combiner and a finisher. This
	 * sounds super complicated at first, but the good part is Java 8 supports
	 * various built-in collectors via the Collectors class. So for the most
	 * common operations you don't have to implement a collector yourself.
	 */
	static void collector() {
		List<Person> filtered =
				persons
						.stream()
						.filter(p -> p.name.startsWith("P"))
						.collect(Collectors.toList());
		System.out.println(filtered);
	}

	static void collector_group_by_age() {
		Map<Integer, List<Person>> personsByAge = persons
				.stream()
				.collect(Collectors.groupingBy(p -> p.age));

		personsByAge
				.forEach((age, p) -> System.out.format("age %s: %s\n", age, p));
	}

	static void collector_average() {
		Double averageAge = persons
				.stream()
				.collect(Collectors.averagingInt(p -> p.age));

		System.out.println(averageAge);
	}

	/*
	 * If you're interested in more comprehensive statistics, the summarizing
	 * collectors return a special built-in summary statistics object. So we can
	 * simply determine min, max and arithmetic average age of the persons as
	 * well as the sum and count.
	 */
	static void summary_statistics() {
		IntSummaryStatistics ageSummary =
				persons
						.stream()
						.collect(Collectors.summarizingInt(p -> p.age));

		System.out.println(ageSummary);
	}

	/*
	 * The next example joins all persons into a single string
	 */
	static void joining_into_into_a_single_string() {
		String phrase = persons
				.stream()
				.filter(p -> p.age >= 18)
				.map(p -> p.name)
				.collect(Collectors.joining(" and ", "In Germany ", " are of legal age."));

		System.out.println(phrase);
	}

	/*
	 * In order to transform the stream elements into a map, we have to specify
	 * how both the keys and the values should be mapped. Keep in mind that the
	 * mapped keys must be unique, otherwise an IllegalStateException is thrown.
	 * You can optionally pass a merge function as an additional parameter to
	 * bypass the exception:
	 */
	static void grouping_into_a_map() {
		Map<Integer, String> map = persons
				.stream()
				.collect(Collectors.toMap(
						p -> p.age,
						p -> p.name,
						(name1, name2) -> name1 + ";" + name2));

		System.out.println(map);
	}

}
