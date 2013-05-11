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

    public Article(String title, String summary, String content) {
        this.title = title;
        this.description = summary;
        this.content = content;
    }
    public Article(String title, String summary, String content, String pubdate, ArrayList<String> cat) {
        this.title = title;
        this.description = summary;
        this.content = content;
        this.categories = cat;
        this.pubdate = pubdate;
    }

	public String getTitle() {
		// TODO Auto-generated method stub
		return title;
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return description;
	}

	public String getContent() {
		// TODO Auto-generated method stub
		return content;
	}

	public String getImgsrcAt(int idx) {
		if (imgsrc.size() >= idx + 1)
		    return imgsrc.get(idx);
		else
			return null;
	}

	public String getPubdate() {
		return pubdate;
	}

	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public ArrayList<String> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<String> categories) {
		this.categories = categories;
	}

	public int addCategory(String category){
		if (categories == null)
			categories = new ArrayList<String>();
		categories.add(category);
		return categories.size();
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
		if (imgsrc == null)
			imgsrc = new ArrayList<String>();
		this.imgsrc.add(str);
		return imgsrc.size();
	}
}
