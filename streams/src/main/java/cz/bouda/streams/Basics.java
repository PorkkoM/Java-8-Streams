package cz.bouda.streams;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import cz.bouda.streams.trigger.Trigger;

public class Basics {

	private static String[] stringsArray = { "Petr", "Ales", "Best", "Milda", "Alena", "Alfred" };

	private static List<String> strings = Arrays.asList(stringsArray);

	public static void main(String... args) {
		Trigger.run(Basics.class);
	}

	public static void sortAndWriteName() {
		strings.stream().filter(s -> s.startsWith("A")).map(String::toUpperCase).sorted().forEach(System.out::println);
	}

	public static void writeFirst() {
		strings.stream().findFirst().ifPresent(System.out::println);
	}

	public static void writeFirstStreamOnly() {
		Stream.of(stringsArray).findFirst().ifPresent(System.out::println);
	}

	public static void intStream() {
		IntStream.range(1, 4).forEach(System.out::println);
	}

	public static void arrayStream() {
		Arrays.stream(new int[] { 1, 2, 3 }).forEach(System.out::println);
	}

	public static void substringStream() {
		Stream.of("a1", "a2", "a3").map(s -> s.substring(1)).mapToInt(Integer::parseInt).max().ifPresent(System.out::println);
	}

	public static void mapObject() {
		IntStream.range(1, 4).mapToObj(i -> "a" + i).forEach(System.out::println);
	}

	public static void doubleMap() {
		Stream.of(1.0, 2.0, 3.0).mapToInt(Double::intValue).mapToObj(i -> "a" + i).forEach(System.out::println);
	}

	public static void orderStream_intermediate_operation() {
		// When executing this code snippet, nothing is printed to the console.
		// That is because intermediate operations will only be executed when a
		// terminal operation is present.
		Stream.of("d2", "a2", "b1", "b3", "c").filter(s -> {
			System.out.println("filter: " + s);
			return true;
		});
	}

	public static void orderStream_terminal_operation() {
		// Let's extend the above example by the terminal operation forEach:
		Stream.of("d2", "a2", "b1", "b3", "c").filter(s -> {
			System.out.println("filter: " + s);
			return true;
		}).forEach(s -> System.out.println("forEach: " + s));
	}

	public static void anyMatch() {
		/*
		 * The operation anyMatch returns true as soon as the predicate applies
		 * to the given input element. This is true for the second element
		 * passed "A2". Due to the vertical execution of the stream chain, map
		 * has only to be executed twice in this case. So instead of mapping all
		 * elements of the stream, map will be called as few as possible.
		 */
		Stream.of("d2", "a2", "b1", "b3", "c").map(s -> {
			System.out.println("map: " + s);
			return s.toUpperCase();
		}).anyMatch(s -> {
			System.out.println("anyMatch: " + s);
			return s.startsWith("A");
		});
	}

	public static void why_order_matters() {
		/*
		 * The next example consists of two intermediate operations map and
		 * filter and the terminal operation forEach. Let's once again inspect
		 * how those operations are being executed:
		 */
		Stream.of("d2", "a2", "b1", "b3", "c").map(s -> {
			System.out.println("map: " + s);
			return s.toUpperCase();
		}).filter(s -> {
			System.out.println("filter: " + s);
			return s.startsWith("A");
		}).forEach(s -> System.out.println("forEach: " + s));
	}

	/*
	 * Optimized
	 */
	public static void why_order_matters_2() {
		/*
		 * We can greatly reduce the actual number of executions if we change
		 * the order of the operations, moving filter to the beginning of the
		 * chain:
		 */
		Stream.of("d2", "a2", "b1", "b3", "c").filter(s -> {
			System.out.println("filter: " + s);
			return s.startsWith("a");
		}).map(s -> {
			System.out.println("map: " + s);
			return s.toUpperCase();
		}).forEach(s -> System.out.println("forEach: " + s));
	}

	public static void why_order_matters_order() {
		Stream.of("d2", "a2", "b1", "b3", "c").sorted((s1, s2) -> {
			System.out.printf("sort: %s; %s\n", s1, s2);
			return s1.compareTo(s2);
		}).filter(s -> {
			System.out.println("filter: " + s);
			return s.startsWith("a");
		}).map(s -> {
			System.out.println("map: " + s);
			return s.toUpperCase();
		}).forEach(s -> System.out.println("forEach: " + s));
	}

	/*
	 * Optimized
	 */
	public static void why_order_matters_order_2() {
		Stream.of("d2", "a2", "b1", "b3", "c").filter(s -> {
			System.out.println("filter: " + s);
			return s.startsWith("a");
		}).sorted((s1, s2) -> {
			System.out.printf("sort: %s; %s\n", s1, s2);
			return s1.compareTo(s2);
		}).map(s -> {
			System.out.println("map: " + s);
			return s.toUpperCase();
		}).forEach(s -> System.out.println("forEach: " + s));
	}

	/*
	 * Java 8 streams cannot be reused. As soon as you call any terminal
	 * operation the stream is closed:
	 */
	public static void reusing_stream() {
		Stream<String> stream = Stream.of("d2", "a2", "b1", "b3", "c").filter(s -> s.startsWith("a"));

		stream.anyMatch(s -> true); // ok
		try {
			stream.noneMatch(s -> true); // exception
		} catch (Exception e) {
			System.out.println("NoneMatch");
		}
	}

	/*
	 * To overcome this limitation we have to to create a new stream chain for
	 * every terminal operation we want to execute, e.g. we could create a
	 * stream supplier to construct a new stream with all intermediate
	 * operations already set up:
	 */
	public static void reusing_stream_2() {
		Supplier<Stream<String>> streamSupplier = () -> Stream.of("d2", "a2", "b1", "b3", "c").filter(s -> s.startsWith("a"));

		streamSupplier.get().anyMatch(s -> true); // ok
		streamSupplier.get().noneMatch(s -> true); // ok
	}
}
