package org.unibl.etf.mdp.dobavljacserver.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Book {

	private String title;
	private String author;
	private String editor;
	private String language;
	private Date releaseDate;
	private String content;
	private byte[] coverImage;

	public Book() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Book(String title, String author, String editor, String language, Date realeaseDate) {
		super();
		this.title = title;
		this.author = author;
		this.editor = editor;
		this.language = language;
		this.releaseDate = realeaseDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public byte[] getCoverImage() {
		return coverImage;
	}

	public void setCoverImage(byte[] coverImage) {
		this.coverImage = coverImage;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public int hashCode() {
		return Objects.hash(author, editor, language, releaseDate, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		return Objects.equals(author, other.author) && Objects.equals(editor, other.editor)
				&& Objects.equals(language, other.language) && Objects.equals(releaseDate, other.releaseDate)
				&& Objects.equals(title, other.title);
	}

	@Override
	public String toString() {
		SimpleDateFormat displayFormat = new SimpleDateFormat("dd.MM.yyyy.");
		String releaseDateStr = (releaseDate != null) ? displayFormat.format(releaseDate) : "N/A";

		return  author+" - " + title+" [" + editor + " - " + language + "] (" + releaseDateStr+")";
	}

}
