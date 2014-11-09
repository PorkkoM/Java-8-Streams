package cz.bouda.streams;


import cz.bouda.streams.domain.*;
import cz.bouda.streams.trigger.*;
import java.util.*;

import static java.util.stream.Collectors.*;
import java.util.stream.Stream;

public class Grouping {

    private static List<Book> library = null;

    static {
        Author author1 = new Author("Adam Bien");
        Author author2 = new Author("Adam Warski");
        Author author3 = new Author("Petr Bouda");
        Author author4 = new Author("Ales Kopecky");
        Author author5 = new Author("Marek Pribyl");

        Book book1 = new Book(Topic.MASTERING_LAMBDAS);
        book1.setAuthors(Arrays.asList(author1, author2, author3));

        Book book2 = new Book(Topic.JAVA_8_IN_ACTION);
        book2.setAuthors(Arrays.asList(author1, author5));

        library = Arrays.asList(book1, book1, book2);
    }

    public static void main(String... args) {
        Trigger.run(Grouping.class);
    }

    @Description("Number of book with the same topic in library")
    public static void numberInTopic() {
        Map<Topic, Long> number = library.stream().collect(
            groupingBy(Book::getTopic, counting()));

        number.forEach((k, v)-> System.out.println("- " + k + " " + v));
    }

    @Description("Most authors by topic")
    public static void mostAuthorByTopic() {
        Map<Topic, Optional<Book>> most = library.stream().collect(
            groupingBy(Book::getTopic, maxBy(Comparator.comparing(b -> b.getAuthors().size()))));

        most.forEach((k, v) -> System.out.println("- " + k + " " + v));
    }
	
	@Description("Example of IntSummaryStatistics")
	public static void summaryIntStatictics(){
		Map<Topic, IntSummaryStatistics> stats = library.stream().collect(
			groupingBy(Book::getTopic, summarizingInt(b -> b.getAuthors().size()))
		);
		
		System.out.println(stats.get(Topic.MASTERING_LAMBDAS));
	}
	
	@Description("Most popular topic in library")
	public static void mostPopularTopic(){
		Stream<Map.Entry<Topic, Long>> popularStream = library.stream().collect(
			groupingBy(Book::getTopic, counting())).entrySet().stream();
		
		Optional<Topic> result = popularStream
			.max(Map.Entry.comparingByValue())
			.map(Map.Entry::getKey);
		
		System.out.println(result.get());
 	}
			
}
