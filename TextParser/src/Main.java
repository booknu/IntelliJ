import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
	static String[] types = {
		"hellven",
			"xvideos",
			"exhentai",
			"hitomi",
			"pornhub"
	};
	public static void main(String args[]) throws FileNotFoundException {
		Scanner sc = new Scanner(new File("urls.txt"));
		StringBuilder[] msg = new StringBuilder[types.length+1];
		for(int i = 0; i < msg.length; ++i) msg[i] = new StringBuilder();
		while(sc.hasNextLine()) {
			String cur = sc.nextLine().trim();
			if(cur.equals("")) continue;
			boolean ok = false;
			for(int i = 0; i < types.length; ++i) {
				if(cur.contains(types[i])) {
					msg[i].append(cur + '\n');
					ok = true;
					break;
				}
			}
			if(!ok) {
				msg[msg.length-1].append(cur + '\n');
			}
		}
		sc.close();
		for(int i = 0; i < types.length; ++i) {
			PrintWriter pt = new PrintWriter(new File(types[i] + ".txt"));
			pt.println(msg[i].toString());
			pt.close();
		}
		PrintWriter pt = new PrintWriter(new File("Unknown.txt"));
		pt.println(msg[msg.length-1].toString());
		pt.close();
	}
}
