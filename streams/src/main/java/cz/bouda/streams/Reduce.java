package cz.bouda.streams;

import cz.bouda.streams.domain.Person;
import cz.bouda.streams.trigger.Description;
import cz.bouda.streams.trigger.Trigger;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Reduce {

	private static List<Person> persons
			= Arrays.asList(
					new Person("Max", 18),
					new Person("Peter", 23),
					new Person("Pamela", 23),
					new Person("David", 12));

	public static void main(String... args) {
		Trigger.run(Reduce.class);
	}

	/*
	 * The reduce method accepts a BinaryOperator accumulator function. That's
	 * actually a BiFunction where both operands share the same type, in that
	 * case Person. BiFunctions are like Function but accept two arguments. The
	 * example function compares both persons ages in order to return the person
	 * with the maximum age.
	 */
	public static void reduce_1() {
		persons.stream()
				.reduce((p1, p2) -> p1.age > p2.age ? p1 : p2)
				.ifPresent(System.out::println); // Pamela
	}

	/*
	 * The second reduce method accepts both an identity value and a
	 * BinaryOperator accumulator. This method can be utilized to construct a
	 * new Person with the aggregated names and ages from all other persons in
	 * the stream:
	 */
	public static void reduce_2() {
		Person result = persons
				.stream()
				.reduce(new Person("", 0), (p1, p2) -> {
					p1.age += p2.age;
					p1.name += p2.name;
					return p1;
				});

		System.out.format("name=%s; age=%s", result.name, result.age);
	}

	/*
	 * The third reduce method accepts three parameters: an identity value, a
	 * BiFunction accumulator and a combiner function of type BinaryOperator.
	 * Since the identity values type is not restricted to the Person type, we
	 * can utilize this reduction to determine the sum of ages from all persons:
	 */
	public static void reduce_3() {
		Integer ageSum = persons
				.stream()
				.reduce(0, (sum, p) -> sum += p.age, (sum1, sum2) -> sum1 + sum2);

		System.out.println(ageSum);
	}

	/*
	 * As you can see the result is 76, but what's happening exactly under the
	 * hood?
	 */
	public static void reduce_3_5_in_parallel() {
		persons
			.parallelStream()
			.reduce(0,
				(sum, p) -> {
					System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
					return sum += p.age;
				},
				(sum1, sum2) -> {
					System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
					return sum1 + sum2;
				});

	}

	@Description("Example reducing during BigInteger::add")
	public static void reduceAdd(){
		Stream<BigInteger> biStream = LongStream.of(1, 2, 3)
			.mapToObj(BigInteger::valueOf);
		
		Optional<BigInteger> bigIntegerSum = biStream
			.reduce(BigInteger::add);
		
		System.out.println("Result: " + bigIntegerSum);
	}
	
	@Description("Example reducing during BigInteger::add with Default")
	public static void reduceAddWithDefault(){
		Stream<BigInteger> biStream = LongStream.of()
			.mapToObj(BigInteger::valueOf);
		
		BigInteger bigIntegerSum = biStream
			.reduce(BigInteger.valueOf(5), BigInteger::add);
		
		System.out.println("Result: " + bigIntegerSum);
	}
	
}
