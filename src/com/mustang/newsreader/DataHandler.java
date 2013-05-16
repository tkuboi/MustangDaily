package com.mustang.newsreader;

import java.util.ArrayList;

public class DataHandler {
	private static ArrayList<Article> m_articles;
    
	private static DataHandler instance;

	private DataHandler() {
		m_articles = new ArrayList<Article>();
	}

	public static DataHandler getInstance()
	{
		if (instance == null)
		{
			synchronized(DataHandler.class)
			{
				if (instance == null)
				{
					System.out.println("getInstance(): First time getInstance was invoked!");
					instance = new DataHandler();
				}
			}            
		}

		return instance;
	}

	public void setArticles(ArrayList<Article> articles)
	{
		m_articles = articles;
	}

	public ArrayList<Article> getArticles()
	{
		return m_articles;
	}
	
	public int addArticle(Article a) {
		m_articles.add(a);
		return m_articles.size();
	}

	public int getNumArticles() {
		return m_articles.size();
	}

	public Article getArticleAt(int i) {
		return m_articles.get(i);
	}
}
