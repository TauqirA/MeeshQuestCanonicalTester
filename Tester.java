package cmsc420.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cmsc420.meeshquest.part2.MeeshQuest;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

public class Tester {
	private static List<String> fileToLines(String filename) {
		List<String> lines = new LinkedList<String>();
		String line = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			while ((line = in.readLine()) != null) {
				lines.add(line);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	public static void main(String[] args) throws IOException {
		TreeSet<Integer> testToCheck = new TreeSet<Integer>();
		int start = 2600;
		int end = 2840;
		String test = "shortestPath";
		
		for(int i = start; i < end; i++){
			System.out.println(i);
			if(i == 2685 ) continue;//Long input that messes stuff up
			Document doc = Jsoup.connect("https://cmsc420.cs.umd.edu/meeshquest/part3/input/"+i+"/").get();
			String s = doc.getElementById("input").nextElementSibling().text();
			if( test != null && !s.contains(test)) continue;
			String outtext = doc.getElementById("output").nextElementSibling().nextElementSibling().text();
			try (PrintStream out = new PrintStream(new FileOutputStream("testfiles/in.expoutput.xml"))) {
				out.print(outtext);
			}
			PrintStream outOld = System.out;	  
			PrintStream ps = new PrintStream(new File("testfiles/in.input.xml"));	     
			PrintStream out = new PrintStream(new File("testfiles/in.output.xml"));	  
			ps.println(s);
			System.setOut(out);
			MeeshQuest.main(args);
			System.setOut(outOld);
			List<String> original = fileToLines("testfiles/in.expoutput.xml");
			List<String> revised  = fileToLines("testfiles/in.output.xml");

			Patch patch = DiffUtils.diff(original, revised);

			for (Delta delta: patch.getDeltas()) {
				testToCheck.add(i);
				System.out.println(delta.getOriginal() +" <-expected:actual-> "+ delta.getRevised());

			}
		}
		System.out.println(testToCheck);
	}

}
