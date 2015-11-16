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

	// ���õ���С֧�ֶȼ���Ϊ 2.
	private static int min_sup = 2;

	public static void main(String[] args) {
		// �����������ԭʼ���ݣ�ÿһ����һ������ÿһ�������ְ�����һ��ֵ��
		ArrayList<List<String>> data = new ArrayList<List<String>>();
		String path = "datatest.txt";
		// ����ԭʼ����
		try {
			readData(data, path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// �ñ������ÿ�ε����֧�ֶ�
		HashMap<HashSet<String>, Integer> itemSet = new HashMap<HashSet<String>, Integer>();
		// ��ʼʱ��ʼ��Ϊ L1
		initL1(data, itemSet);

		// �������������ǰ�ģ���ֹѭ������ʱ��������һ��
		HashMap<HashSet<String>, Integer> itemSetPre = new HashMap<HashSet<String>, Integer>();

		// �������ÿ�β����µ�Ƶ���������һ�����⣬�µ�itemSet sizeΪ0ʱ��ѭ�����������յĽ�������� itemSetPre �С�
		for (int k = 2; itemSet.size() != 0; k++) {

			itemSetPre = itemSet;
			// ��������Ľ����ÿ�ζ�������C�������������飬���Ӻͼ�֦.
			HashSet<HashSet<String>> c = aproiriGen(itemSet);

			// ��������ǲ��������յ���һ�� L.
			itemSet = itemSetGen(data, c);

		}
		
		associationRulesGen(data,itemSet);
		
		System.out.println("���ղ�����Ƶ�����*********************");
		printHashMap(itemSetPre);
		
		System.out.println("���ղ�����Ƶ�����*********************");
		associationRulesGen(data,itemSet);

	}

	/**
	 * ����Ƶ����������Ҵ�ӡ����������
	 * ���ڲ�����ÿһ��Ƶ�����
	 * @param data
	 * @param itemSet
	 */
	private static void associationRulesGen(ArrayList<List<String>> data,
			HashMap<HashSet<String>, Integer> itemSet) {
	}

	/**
	 * ��ԭʼ����data �� ����C�������µ����HashMap<HashSet<String>, Integer> ͳ�����ݡ�
	 * ������е�ÿһ��������ݣ���ÿһ���������Աȡ� �ó�C��һ���� ���Ǹ� HashSet<String>
	 * �Ľṹ������ת����List<String> ��data �е����������Ƚϡ�data �е�һ�������� �� List<String>.
	 * �������Ľ�����������C�е�������+1��
	 * 
	 * @param data
	 *            ԭʼ�����ݡ�
	 * @param c
	 *            ���Ӻͼ�֦֮������δͳ������ֵ��
	 * @return
	 */
	private static HashMap<HashSet<String>, Integer> itemSetGen(
			ArrayList<List<String>> data, HashSet<HashSet<String>> c) {

		HashMap<HashSet<String>, Integer> result = new HashMap<HashSet<String>, Integer>();

		for (HashSet<String> item : c) {

			// List���췽������ֱ�ӽ�setת����list������һ��ת������list��
			ArrayList<String> list = new ArrayList<String>(item);
			for (List<String> itemData : data) {
				ArrayList<String> tempList = new ArrayList<String>();
				tempList.addAll(list);
				boolean flag = !tempList.retainAll(itemData);

				if (flag) {// �ж��� �����ݵĹ�ϵ������������ݵ��Ӽ�
					if (result.containsKey(item)) {
						Integer i = result.get(item);
						result.put(item, i + 1);
					} else {
						result.put(item, 1);
					}
				}
			}
		}
		// ɾ��С����С֧�ֶȵġ�
		removeMinSup(result);
		return result;
	}

	/**
	 * 
	 * ��������Ľ����ÿ�ζ�������C�������������飬���Ӻͼ�֦������һ�� ��L�в����� C�Ľṹ��һ��HashSet
	 * ��Ԫ��ΪHashSet����Ӧ��ֵ��String��HashSet<HashSet<String>>
	 * 
	 * @param itemSet
	 * @return
	 */
	private static HashSet<HashSet<String>> aproiriGen(
			HashMap<HashSet<String>, Integer> itemSet) {

		HashSet<HashSet<String>> c = new HashSet<HashSet<String>>();

		// ��������ŵ���L�����ļ��ļ��ϣ���Ҫ�������Ӳ���
		Set<HashSet<String>> keySet = itemSet.keySet();

		// ��������
		cartJoin(c, keySet);

		// ���м�֦
		Pruning(c, keySet);
		return c;
	}

	/**
	 * HashSet<String> �ṹ���ó��ýṹ��ȫ�� ��������һ�� ���Ӽ�������Ӽ������ײ����� �ڶ������ж���ÿ���Ӽ��Ƿ��� keySet
	 * �е�Ԫ�أ��ǵĻ��ͱ��������ǵĻ�����ɾ����
	 * 
	 * @param c
	 * @param keySet
	 */
	private static void Pruning(HashSet<HashSet<String>> c,
			Set<HashSet<String>> keySet) {

		// remove �д�ŵ�����Ҫ��ɾ����c�е��
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
	 * �������ߵ�����
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
	 * ���ڸտ�ʼʱ���еĵ�һ�γ�ʼ��������L1��L1 ���õĽṹ��HashMap������HashSet��ֵ��Integer�� ����data
	 * ͳ��ÿ����ѡһ�����Ŀ��ɾ��С����С֧�ֶȵ����
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
		// ����������С֧�ֶȵ��ɾ����
		removeMinSup(l1);
	}

	/**
	 * 
	 * ������ӡHashMap��
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
	 * ����������С֧�ֶȵ��ɾ����
	 * 
	 * @param l1
	 */
	private static void removeMinSup(HashMap<HashSet<String>, Integer> itemSet) {
		// �����Ҫ��ɾ���ļ�
		Set<HashSet<String>> remove = new HashSet<HashSet<String>>();

		// �������һ�飬��ֵС����С֧�ֶȵ� �������
		Set<Map.Entry<HashSet<String>, Integer>> set1 = itemSet.entrySet();
		for (Map.Entry<HashSet<String>, Integer> me : set1) {
			if (me.getValue() < min_sup) {
				remove.add(me.getKey());
			}
		}

		// ɾ����������С֧�ֶȵ��
		for (HashSet<String> key : remove) {
			itemSet.remove(key);
		}
	}

	/**
	 * �÷������ڳ�ʼ�����ݡ�
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
