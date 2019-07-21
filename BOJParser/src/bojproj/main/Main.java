package bojproj.main;

import bojproj.obj.Problem;
import bojproj.obj.User;
import bojproj.parser.ProbTableParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
	private static class Pair<U extends Comparable<U>, V> implements Comparable<Pair<U, V>> {
		public U fi;
		public V se;
		public Pair(U first, V second) {
			fi = first;
			se = second;
		}
		public Pair() {
			this(null, null);
		}

		/**
		 * fi로만 비교한다.
		 * @param o
		 * @return
		 */
		@Override
		public int compareTo(Pair<U, V> o) {
			return fi.compareTo(o.fi);
		}
	}
	public static void main(String args[]) throws FileNotFoundException, NoSuchAlgorithmException, KeyManagementException {
		Scanner userSc = new Scanner(new File("users.txt"));
		Scanner inpSc = new Scanner(new File("input.txt"));
		// -- user 정보 읽기
		ArrayList<User> users = new ArrayList<>();
		while(userSc.hasNextLine()) {
			String name = userSc.nextLine().trim();
			users.add(new User(name));
		}
		userSc.close();

		// -- 공통으로 푼, 실패한 문제들 분석
		ArrayList<Problem> solved = new ArrayList<>(), failed = new ArrayList<>();
		for(User x : users) {
			System.out.println(x.getUrl());
			solved.addAll(x.getSolved());
			failed.addAll(x.getFailed());
		}
		solved.sort(Problem::compareTo);
		failed.sort(Problem::compareTo);
		ArrayList<Pair<Problem, Integer>> sol = uniqueCount(solved), fail = uniqueCount(failed);
		// -- 입력
		int inpType = -1;
		if(inpSc.hasNextLine()) {
			String type = inpSc.nextLine().trim();
			if(type.equals("prob")) inpType = 0;
			else if(type.equals("probset")) inpType = 1;
			System.out.println(type + ": " + inpType);
		}
		ArrayList<Problem> targetProbs = new ArrayList<>();
		while(inpSc.hasNextLine()) {
			String in = inpSc.nextLine().trim();
			System.out.println(in);
			switch(inpType) {
				case 0: // prob
					if(in.startsWith(Problem.URL_HEAD)) {
						targetProbs.add(new Problem(Integer.parseInt(in.replace(Problem.URL_HEAD, ""))));
					} else {
						targetProbs.add(new Problem(Integer.parseInt(in)));
					}
					break;
				case 1: // probset
					ProbTableParser parser = ProbTableParser.getParser(in);
					targetProbs.addAll(parser.getProblems());
					break;
			}
		}
		inpSc.close();
		targetProbs.sort(Problem::compareTo);
		targetProbs = unique(targetProbs);
		ArrayList<Pair<Problem, Integer>> allSolved = new ArrayList<>(), notSolved = new ArrayList<>(), partialSolved = new ArrayList<>();
		for(Problem x : targetProbs) {
			int pos = Collections.binarySearch(sol, new Pair<Problem, Integer>(x, 0));
			if(pos < 0) {
				notSolved.add(new Pair(x, 0));
			} else if(sol.get(pos).se == users.size()) {
				allSolved.add(sol.get(pos));
			} else {
				partialSolved.add(sol.get(pos));
			}
		}
		PrintWriter outPr = new PrintWriter(new File("result.html"));
		outPr.println("<html><head><meta charset='utf-8'></head><body>");
		outPr.println("<hr><h1>----- 아무도 안 푼 문제 -----</h1><hr>");
		outPr.println("Total: " + notSolved.size() + "<br>");
		for(Pair<Problem, Integer> x : notSolved) {
			outPr.println(x.fi.toHtmlString() + "<br>");
		}
		outPr.println("<hr><h1>----- 몇 사람만 푼 문제 -----</h1><hr>");
		outPr.println("Total: " + partialSolved.size() + "<br>");
		for(Pair<Problem, Integer> x : partialSolved) {
			outPr.println(x.fi.toHtmlString() + " --> " + x.se + "명"  + "<br>");
		}
		outPr.println("<hr><h1>----- 모두 푼 문제 -----</h1><hr>");
		outPr.println("Total: " + allSolved.size() + "<br>");
		for(Pair<Problem, Integer> x : allSolved) {
			outPr.println(x.fi.toHtmlString() + "<br>") ;
		}
		outPr.println("</body></html>");
		outPr.close();
	}

	public static<T> ArrayList<T> unique(ArrayList<T> ar) {
		ArrayList<T> ret = new ArrayList<>();
		for(T x : ar) {
			if(ret.size() == 0 || !ret.get(ret.size()-1).equals(x)) {
				ret.add(x);
			}
		}
		return ret;
	}

	/**
	 * 정렬된 ar을 받아 각 원소들이 몇 번 씩 등장했는지 저장된 배열을 반환
	 * @param ar
	 * @param <T>
	 * @return
	 */
	public static<T extends Comparable<T>> ArrayList<Pair<T, Integer>> uniqueCount(ArrayList<T> ar) {
		ArrayList<Pair<T, Integer>> ret = new ArrayList<>();
		for(T x : ar) {
			if(ret.size() == 0 || !ret.get(ret.size()-1).fi.equals(x)) {
				ret.add(new Pair(x, 1));
			} else {
				ret.get(ret.size()-1).se++;
			}
		}
		return ret;
	}
}
