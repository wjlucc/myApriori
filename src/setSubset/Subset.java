package setSubset;

import java.util.HashSet;

/**
 * �������Բ���һ�����ϵ����зǿ����Ӽ���
 * 
 * @author WJLUCK
 * 
 */
public class Subset {

	/**
	 * ����ĳ�����ϵ����з�0���Ӽ���
	 * 
	 * @param item
	 *            ����ȥ��һ������
	 * @param result
	 *            ���з�0���Ӽ���
	 */
	private static void getSubset(HashSet<String> item,
			HashSet<HashSet<String>> result) {

		// ��������
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
	 * ����ĳ�����ϵ�Ԫ�ؼ���һ���������Ӽ�
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
