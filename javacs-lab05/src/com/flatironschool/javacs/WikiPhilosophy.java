package com.flatironschool.javacs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {

	final static WikiFetcher wf = new WikiFetcher();

	private static int parentheses_counter;
	private static Deque<String> urlQueue;

	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 * 
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 * 
	 * 1. Clicking on the first non-parenthesized, non-italicized link 2.
	 * Ignoring external links, links to the current page, or red links 3.
	 * Stopping when reaching "Philosophy", a page with no links or a page that
	 * does not exist, or when a loop occurs
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		// some example code to get you started

		String url = args[0];// "https://en.wikipedia.org/wiki/Java_(programming_language)";

		urlQueue = new ArrayDeque<>();
		parentheses_counter = 0;
		
		// String url = "https://en.wikipedia.org/wiki/Mathematics";
		boolean foundLink = false;
		String errorMessage = "Could not find the link to Philosophy page";

		try {
			foundLink = parsePage(url);
		} catch (UnsupportedOperationException ex) {
			errorMessage = ex.getMessage();
		}
		// print the visited links
		Iterator<String> iter = urlQueue.iterator();
		while (iter.hasNext()) {
			System.out.println(iter.next());
		}

		if (!foundLink)
			throw new UnsupportedOperationException(errorMessage);

		return;
	}
	
	
	private static boolean parsePage(String url) throws IOException {

		boolean foundPhilPage = false;
		boolean noLinksFound = true;

		Elements paragraphs = wf.fetchWikipedia(url);

		Element firstPara = paragraphs.get(0);

		System.out.println("Parsing page " + url);

		Iterable<Node> iter = new WikiNodeIterable(firstPara);
		urlQueue.add(url);

		for (Node node : iter) {
			if (node instanceof TextNode) {
				matchParatheses(((TextNode) node).text().toCharArray());
			} else if (node instanceof Element) {
				Element element = (Element) node;
				if (element.tagName().equals("a")) {
					String href = element.attr("href");
					noLinksFound = false;
					if (parentheses_counter > 0) {
						System.out.println("Skipping this link as this is within parantheses " + href);
					} else {
						if (urlQueue.contains(href)) {
							System.out.println("Getting into a loop");
							throw new UnsupportedOperationException("Getting into a loop");
						} else {
							// skip any external link
							if (href.startsWith("http")) {
								System.out.println("Skipping an external link " + href);
							} else if (href.equals("/wiki/Property_(philosophy)")) {
								System.out.println("Found link to philosophy " + href);
								foundPhilPage = true;
								break;
							} else if (validateParent(element)) {
								String completeUrl = "https://en.wikipedia.org" + href;
								if (!url.equals(completeUrl))
								{
									foundPhilPage = parsePage("https://en.wikipedia.org" + href);
									break;
								}
							}
						}
					}

				}

			}
		}

		if (noLinksFound)
			throw new UnsupportedOperationException ("Page \"" + url +"\" has no links");
		
		return foundPhilPage;
	}

	/**
	 * check to make sure parent is not i, em, or sup tags
	 * 
	 * @param node
	 * @return
	 */
	private static boolean validateParent(Node node) {

		boolean validParent = true;

		if (node != null) {
			Node parent = node.parent();
			if (parent != null) {
				// check if parent is tag i, em, or sup
				if (parent instanceof Element) {
					Element element = (Element) parent;
					if (element.tagName().equals("i") || element.tagName().equals("em")
							|| element.tagName().equals("sup")) {
						validParent = false;
					}

				}
				if (validParent)
					validateParent(parent);
			}
		}

		return validParent;

	}

	private static void matchParatheses(char[] text) {
		int index = 0;

		while (index < text.length) {
			char c = text[index];
			if (c == '(')
				parentheses_counter++;
			else if (c == ')')
				parentheses_counter--;
			index++;
		}
	}
}
