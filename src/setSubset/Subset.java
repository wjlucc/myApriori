package setSubset;

import java.util.HashSet;

/**
 * 这个类可以产生一个集合的所有非空真子集。
 * 
 * @author WJLUCK
 * 
 */
public class Subset {

	/**
	 * 产生某个集合的所有非0真子集。
	 * 
	 * @param item
	 *            传进去的一个集合
	 * @param result
	 *            所有非0真子集。
	 */
	private static void getSubset(HashSet<String> item,
			HashSet<HashSet<String>> result) {

		// 结束条件
		if (item.size() <= 1) {
			return;
		}
		HashSet<HashSet<String>> subset = getSubset1(item);
		result.addAll(subset);
		for (HashSet<String> aSubs : subset) {
			getSubset(aSubs, result);
		}
	}

	/**
	 * 产生某个集合的元素减少一个的所有子集
	 * 
	 * @param item
	 * @return
	 */
	private static HashSet<HashSet<String>> getSubset1(HashSet<String> item) {
		HashSet<HashSet<String>> result = new HashSet<HashSet<String>>();
		for (String aItem : item) {
			HashSet<String> tempItem = new HashSet<String>();
			tempItem.addAll(item);
			tempItem.remove(aItem);
			result.add(tempItem);
		}
		return result;
	}

	public static void main(String[] args) {

		HashSet<HashSet<String>> result = new HashSet<HashSet<String>>();

		HashSet<String> item = new HashSet<String>();
		item.add("a");
		item.add("b");
		item.add("c");
		getSubset(item, result);
		System.out.println(result);
	}
}
