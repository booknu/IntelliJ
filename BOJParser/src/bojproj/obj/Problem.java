package bojproj.obj;

import bojproj.settings.Settings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Problem implements Comparable<Problem> {
	public static final String URL_HEAD = "https://www.acmicpc.net/problem/";
	private String url;
	private String title;
	private int probNum;
	public Problem(int probNum, String title) {
		url = URL_HEAD + probNum;
		this.probNum = probNum;
		this.title = title;
	}
	public Problem(int probNum) {
		this(probNum, null);
	}
	public String getUrl() { return url; }
	public String getTitle() {
		if(title == null) {
			try {
				Document doc = Jsoup.connect(url).timeout(Settings.timeout).get();
				// !! Need to be implemented !!
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return title;
	}
	public int getProbNum() { return probNum; }

	@Override
	public String toString() {
		return url + "  : " + probNum + "번 - " + title;
	}
	public String toHtmlString() {
		return "<a href='" + url + "'>" + url + "</a>" + "  : " + probNum + "번 - " + title;
	}
	@Override
	public boolean equals(Object o) {
		return probNum == ((Problem)o).probNum;
	}

	@Override
	public int compareTo(Problem o) {
		return probNum - o.probNum;
	}
}
