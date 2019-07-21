package bojproj.parser;

import bojproj.obj.Problem;
import bojproj.settings.Settings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class ProbTableParser {
	private String url;
	private ProbTableParser(String url) {
		this.url = url;
	}
	public static ProbTableParser getParser(String url) {
		return new ProbTableParser(url);
	}
	public ArrayList<Problem> getProblems() {
		ArrayList<Problem> ret = null;
		try {
			Document doc = Jsoup.connect(url).timeout(Settings.timeout).get();
			int numIdx = -1, titleIdx = -1;
			Elements titleRows = doc.select("div.table-responsive thead tr th");
			Elements rows = doc.select("div.table-responsive tbody tr");
			// 일단 문제 번호, 제목에 해당되는 table col을 찾자.
			for(int i = 0; i < titleRows.size(); ++i) {
				if(titleRows.get(i).text().equals("문제 번호")) numIdx = i;
				else if(titleRows.get(i).text().equals("제목")) titleIdx = i;
			}
			ret = new ArrayList<>(rows.size());
			for(int i = 0; i < rows.size(); ++i) {
				Element numNode = rows.get(i).child(numIdx), titleNode = rows.get(i).child(titleIdx);
				ret.add(new Problem(Integer.parseInt(numNode.text()), titleNode.text()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
}
