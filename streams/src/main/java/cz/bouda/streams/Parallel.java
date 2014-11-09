package cz.bouda.streams;

import java.util.Arrays;
import java.util.List;

import cz.bouda.streams.domain.Person;

/*
 * Streams can be executed in parallel to increase runtime performance on large 
 * amount of input elements. Parallel streams use a common ForkJoinPool available 
 * via the static ForkJoinPool.commonPool() method. The size of the underlying 
 * thread-pool uses up to five threads - depending on the amount of available 
 * physical CPU cores:
 * 
 * 	ForkJoinPool commonPool = ForkJoinPool.commonPool();
 *	System.out.println(commonPool.getParallelism());
 *
 *	-Djava.util.concurrent.ForkJoinPool.common.parallelism=5
 *
 * In summary, it can be stated that parallel streams can bring be a nice performance 
 * boost to streams with a large amount of input elements. But keep in mind that some 
 * parallel stream operations like reduce and collect need additional computations 
 * (combine operations) which isn't needed when executed sequentially.
 **/
public class Parallel {

	private static List<Person> persons = Arrays.asList(new Person("Max", 18), new Person("Peter", 23), new Person("Pamela", 23),
			new Person("David", 12));

	public static void main(String... args) {
		Trigger.run(Parallel.class);
	}

	static void print_thread() {
		Arrays.asList("a1", "a2", "b1", "c2", "c1").parallelStream().filter(s -> {
			System.out.format("filter: %s [%s]\n", s, Thread.currentThread().getName());
			return true;
		}).map(s -> {
			System.out.format("map: %s [%s]\n", s, Thread.currentThread().getName());
			return s.toUpperCase();
		}).forEach(s -> System.out.format("forEach: %s [%s]\n", s, Thread.currentThread().getName()));
	}

	/*
	 * It seems that sort is executed sequentially on the main thread only. But
	 * this is not the case. Actually, sort on a parallel stream uses the new
	 * Java 8 method Arrays.parallelSort() under the hood. Keep in mind that the
	 * debug output only refers to the execution of the passed lambda
	 * expression. So, for sort the comparator is executed solely on the main
	 * thread but the actual sorting algorithm is executed in parallel.
	 */
	static void print_thread_sorted() {
		Arrays.asList("a1", "a2", "b1", "c2", "c1").parallelStream().filter(s -> {
			System.out.format("filter: %s [%s]\n", s, Thread.currentThread().getName());
			return true;
		}).map(s -> {
			System.out.format("map: %s [%s]\n", s, Thread.currentThread().getName());
			return s.toUpperCase();
		}).sorted((s1, s2) -> {
			System.out.format("sort: %s <> %s [%s]\n", s1, s2, Thread.currentThread().getName());
			return s1.compareTo(s2);
		}).forEach(s -> System.out.format("forEach: %s [%s]\n", s, Thread.currentThread().getName()));
	}

	static void reduce_parallel() {
		persons.parallelStream().reduce(0, (sum, p) -> {
			System.out.format("accumulator: sum=%s; person=%s [%s]\n", sum, p, Thread.currentThread().getName());
			return sum += p.age;
		}, (sum1, sum2) -> {
			System.out.format("combiner: sum1=%s; sum2=%s [%s]\n", sum1, sum2, Thread.currentThread().getName());
			return sum1 + sum2;
		});
	}

}
