package bojproj.obj;

import bojproj.settings.Settings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class User {
	public static final String URL_HEAD = "https://www.acmicpc.net/user/";
	private static final int SUCCESS_TYPE = 0, FAILED_TYPE = 1;
	private String url, name;
	private ArrayList<Problem> solved, failed;

	public User(String userName) {
		this.url = URL_HEAD + userName;
		name = userName;
		solved = failed = null;
	}

	/**
	 * 푼 문제를 가져온다.
	 * @return
	 */
	public ArrayList<Problem> getSolved() {
		if(solved == null) {
			initUserProblems();
		}
		return solved;
	}

	/**
	 * 실패한 문제들을 가져온다.
	 * @return
	 */
	public ArrayList<Problem> getFailed() {
		if(failed == null) {
			initUserProblems();
		}
		return failed;
	}

	public String getUrl() { return url; }
	public String getName() { return name; }

	/**
	 * 해당 유저의 문제들을 초기화한다.
	 */
	private void initUserProblems() {
		try {
			Document doc = Jsoup.connect(url).timeout(Settings.timeout).get();
			solved = getUserProblems(doc, SUCCESS_TYPE);
			failed = getUserProblems(doc, FAILED_TYPE);
			if(solved != null) solved.sort(Problem::compareTo);
			if(failed != null) failed.sort(Problem::compareTo);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * 해당 유저의 문제들을 가져온다.
	 *
	 * @param type solved, failed
	 * @return
	 */
	private ArrayList<Problem> getUserProblems(Document doc, int type) {
		ArrayList<Problem> ret = null;
		try {
			Element panel = doc.select("div.panel-body").get(type);
			Elements probNums = panel.select("span.problem_number a"), probTitles = panel.select("span.problem_title a");
			if(probNums.size() != probTitles.size()) return null;
			ret = new ArrayList<>(probNums.size());
;			for(int i = 0; i < probNums.size(); ++i) {
				ret.add(new Problem(Integer.parseInt(probNums.get(i).text()), probTitles.get(i).text()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
}
