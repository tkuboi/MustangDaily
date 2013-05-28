package com.mustang.newsreader;

import java.util.ArrayList;

public class Article {
    private String title;
    private String pubdate;
    private String link;
    private String description;
    private String content;
    private ArrayList<String> imgsrc;
    private ArrayList<String> categories;

    public Article() {
    }
    
    public Article(String title, String summary, String content) {
        this.title = title;
        this.description = summary;
        this.content = content;
    }
    
//    public Article(String title, String summary, String content, String pubdate, ArrayList<String> categories) {
//        this.title = title;
//        this.description = summary;
//        this.content = content;
//        this.categories = categories;
//        this.pubdate = pubdate;
//        this.imgsrc = imgsrc;
//    }

	public String getTitle() {
		return this.title;
	}

	public String getDescription() {
		return this.description;
	}

	public String getContent() {
		return this.content;
	}

	public String getImgsrcAt(int idx) {
		if (this.imgsrc.size() >= idx + 1)
		    return this.imgsrc.get(idx);
		else
			return null;
	}

	public String getPubdate() {
		return this.pubdate;
	}

	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}

	public String getLink() {
		return this.link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public ArrayList<String> getCategories() {
		return this.categories;
	}

	public void setCategories(ArrayList<String> categories) {
		this.categories = categories;
	}

	public int addCategory(String category){
		if (this.categories == null)
			this.categories = new ArrayList<String>();
		this.categories.add(category);
		return this.categories.size();
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setImgsrc(ArrayList<String> imgsrc) {
		this.imgsrc = imgsrc;
	}

	public ArrayList<String> getImgsrc() {
		return this.imgsrc;
	}
	
	public int addImgsrc(String str) {
		if (this.imgsrc == null)
			this.imgsrc = new ArrayList<String>();
		this.imgsrc.add(str);
		return this.imgsrc.size();
	}
}
