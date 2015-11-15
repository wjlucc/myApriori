package test;

import java.util.HashSet;

public class Main {
	public static void main(String[] args) {
		HashSet<HashSet<String>> s = new HashSet<HashSet<String>>();
		HashSet<String> a1 = new HashSet<String>();
		a1.add("a");
		a1.add("b");
		HashSet<String> a2 = new HashSet<String>();
		a2.add("c");
		a2.add("d");
		HashSet<String> a3 = new HashSet<String>();
		a3.add("e");
		a3.add("f");
		HashSet<String> a4 = new HashSet<String>();
		a4.add("g");
		a4.add("h");

		s.add(a1);
		s.add(a2);
		s.add(a3);
		s.add(a4);

		print(s);

		HashSet<HashSet<String>> w = new HashSet<HashSet<String>>();
		for (HashSet<String> item : s) {
			HashSet<String> q = new HashSet<String>();

			// 这样进行操作后，原来的不会发生变化
			q.addAll(item);
			q.add("haha");
			w.add(q);
		}
		System.out.println("---------------------------");
		print(w);
		System.out.println("---------------------------");
		print(s);

		
		HashSet<HashSet<String>> e = new HashSet<HashSet<String>>();
		for (HashSet<String> item : s) {
			e.add(item);
		}
		s.removeAll(e);
		
		
		System.out.println("---------------------------");
		print(s);
	}

	/**
	 * @param s
	 */
	private static void print(HashSet<HashSet<String>> s) {
		for (HashSet<String> item : s) {
			for (String str : item) {
				System.out.print(str + "\t");
			}
			System.out.println();
		}
	}

}
