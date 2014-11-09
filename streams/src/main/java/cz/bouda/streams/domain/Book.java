package cz.bouda.streams.domain;

import java.util.List;

public class Book {

	private Topic topic;

	private List<Author> authors;

	public Book(Topic topic) {
		this.topic = topic;
	}
	
	public Topic getTopic() {
		return topic;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

    @Override
    public String toString() {
        return "Book [ topic=" + topic + ", authors=" + authors + "]"; 
    }
}
