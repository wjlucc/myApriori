package Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

	// 设置的最小支持度计数为 2.
	private static int min_sup = 2;

	public static void main(String[] args) {
		// 这里用来存放原始数据，每一项是一条事务。每一条事务又包含有一组值。
		ArrayList<List<String>> data = new ArrayList<List<String>>();
		String path = "datatest.txt";
		// 读入原始数据
		try {
			readData(data, path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 该变量存放每次的项集和支持度
		HashMap<HashSet<String>, Integer> itemSet = new HashMap<HashSet<String>, Integer>();
		// 开始时初始化为 L1
		initL1(data, itemSet);

		// 这里用来存放先前的，防止循环跳出时，保存上一个
		HashMap<HashSet<String>, Integer> itemSetPre = new HashMap<HashSet<String>, Integer>();

		// 这里迭代每次产生新的频繁项集，还有一个问题，新的itemSet size为0时，循环结束，最终的结果保存在 itemSetPre 中。
		for (int k = 2; itemSet.size() != 0; k++) {

			itemSetPre = itemSet;
			// 这个操作的结果是每次都产生了C，做了两件事情，连接和剪枝.
			HashSet<HashSet<String>> c = aproiriGen(itemSet);

			// 这个操作是产生了最终的新一代 L.
			itemSet = itemSetGen(data, c);

		}
		
		associationRulesGen(data,itemSet);
		
		System.out.println("最终产生的频繁项集是*********************");
		printHashMap(itemSetPre);
		
		System.out.println("最终产生的频繁项集是*********************");
		associationRulesGen(data,itemSet);

	}

	/**
	 * 根据频繁项集产生并且打印出关联规则。
	 * 对于产生的每一条频繁项，否
	 * @param data
	 * @param itemSet
	 */
	private static void associationRulesGen(ArrayList<List<String>> data,
			HashMap<HashSet<String>, Integer> itemSet) {
	}

	/**
	 * 由原始数据data 和 集合C产生了新的项集。HashMap<HashSet<String>, Integer> 统计数据。
	 * 对于项集中的每一项，遍历数据，与每一条数据做对比。 拿出C的一条项 ，是个 HashSet<String>
	 * 的结构。将其转换成List<String> 和data 中的数据逐条比较。data 中的一条数据是 个 List<String>.
	 * 求两个的交集，若等于C中的项，则该项+1。
	 * 
	 * @param data
	 *            原始的数据。
	 * @param c
	 *            连接和剪枝之后的项集，未统计数据值。
	 * @return
	 */
	private static HashMap<HashSet<String>, Integer> itemSetGen(
			ArrayList<List<String>> data, HashSet<HashSet<String>> c) {

		HashMap<HashSet<String>, Integer> result = new HashMap<HashSet<String>, Integer>();

		for (HashSet<String> item : c) {

			// List构造方法可以直接将set转换成list，将这一项转换成了list。
			ArrayList<String> list = new ArrayList<String>(item);
			for (List<String> itemData : data) {
				ArrayList<String> tempList = new ArrayList<String>();
				tempList.addAll(list);
				boolean flag = !tempList.retainAll(itemData);

				if (flag) {// 判断项 与数据的关系，如果项是数据的子集
					if (result.containsKey(item)) {
						Integer i = result.get(item);
						result.put(item, i + 1);
					} else {
						result.put(item, 1);
					}
				}
			}
		}
		// 删除小于最小支持度的。
		removeMinSup(result);
		return result;
	}

	/**
	 * 
	 * 这个操作的结果是每次都产生了C，做了两件事情，连接和剪枝。从上一代 的L中产生。 C的结构是一个HashSet
	 * 其元素为HashSet，对应的值是String。HashSet<HashSet<String>>
	 * 
	 * @param itemSet
	 * @return
	 */
	private static HashSet<HashSet<String>> aproiriGen(
			HashMap<HashSet<String>, Integer> itemSet) {

		HashSet<HashSet<String>> c = new HashSet<HashSet<String>>();

		// 这个里面存放的是L产生的键的集合，先要进行连接操作
		Set<HashSet<String>> keySet = itemSet.keySet();

		// 进行连接
		cartJoin(c, keySet);

		// 进行剪枝
		Pruning(c, keySet);
		return c;
	}

	/**
	 * HashSet<String> 结构，得出该结构的全部 少于自身一个 的子集。这个子集很容易产生。 第二步，判断其每个子集是否都是 keySet
	 * 中的元素，是的话就保留，不是的话，就删掉。
	 * 
	 * @param c
	 * @param keySet
	 */
	private static void Pruning(HashSet<HashSet<String>> c,
			Set<HashSet<String>> keySet) {

		// remove 中存放的是需要被删掉的c中的项。
		HashSet<HashSet<String>> remove = new HashSet<HashSet<String>>();

		for (HashSet<String> item : c) {
			boolean flag = false;
			for (String s : item) {

				HashSet<String> subset = new HashSet<String>();
				subset.addAll(item);
				subset.remove(s);
				if (!keySet.contains(subset)) {
					flag = true;
				}
			}
			if (flag) {
				remove.add(item);
			}
		}
		c.removeAll(remove);
	}

	/**
	 * 进行两者的连接
	 * 
	 * @param c
	 * @param keySet
	 */
	private static void cartJoin(HashSet<HashSet<String>> c,
			Set<HashSet<String>> keySet) {

		for (HashSet<String> item1 : keySet) {
			for (HashSet<String> item2 : keySet) {
				for (String item : item2) {
					HashSet<String> newItem = new HashSet<String>();
					newItem.addAll(item1);
					newItem.add(item);
					if (newItem.size() == (item1.size() + 1)) {
						c.add(newItem);
					}
				}
			}
		}

	}

	/**
	 * 用于刚开始时进行的第一次初始化，产生L1。L1 所用的结构是HashMap，键是HashSet，值是Integer。 遍历data
	 * 统计每个候选一项集的数目。删除小于最小支持度的项集。
	 * 
	 * @param data2
	 * @param initL1
	 */
	private static void initL1(ArrayList<List<String>> data,
			HashMap<HashSet<String>, Integer> l1) {

		for (List<String> item : data) {
			for (String it : item) {
				HashSet<String> key = new HashSet<String>();
				key.add(it);
				if (!l1.containsKey(key)) {
					l1.put(key, 1);
				} else {
					Integer i = (l1.get(key) + 1);
					l1.put(key, i);
				}
			}
		}
		// 将不满足最小支持度的项都删除掉
		removeMinSup(l1);
	}

	/**
	 * 
	 * 用来打印HashMap。
	 * 
	 * @param set
	 */
	private static void printHashMap(HashMap<HashSet<String>, Integer> set) {
		Set<Map.Entry<HashSet<String>, Integer>> set2 = set.entrySet();
		for (Map.Entry<HashSet<String>, Integer> me : set2) {
			System.out.println(me.getKey() + "---" + me.getValue());
		}
	}
	

	/**
	 * 将不满足最小支持度的项都删除。
	 * 
	 * @param l1
	 */
	private static void removeMinSup(HashMap<HashSet<String>, Integer> itemSet) {
		// 存放需要被删除的键
		Set<HashSet<String>> remove = new HashSet<HashSet<String>>();

		// 这里遍历一遍，将值小于最小支持度的 项保存下来
		Set<Map.Entry<HashSet<String>, Integer>> set1 = itemSet.entrySet();
		for (Map.Entry<HashSet<String>, Integer> me : set1) {
			if (me.getValue() < min_sup) {
				remove.add(me.getKey());
			}
		}

		// 删除不满足最小支持度的项。
		for (HashSet<String> key : remove) {
			itemSet.remove(key);
		}
	}

	/**
	 * 该方法用于初始读数据。
	 * 
	 * @param data2
	 * @throws IOException
	 */
	private static void readData(ArrayList<List<String>> data, String path)
			throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] arrStr = line.split("\\s+");
			List<String> item = Arrays.asList(arrStr);
			data.add(item);
		}
	}
}
