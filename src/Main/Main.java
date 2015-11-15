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
		System.out.println("���յĽ����*****************************");
		printHashMap(itemSetPre);

	}

	/**
	 * ��ԭʼ����data �� ����C�������µ����HashMap<HashSet<String>, Integer> ͳ�����ݡ�
	 * ������е�ÿһ��������ݣ���ÿһ���������Աȡ� �ó�C��һ���� ���Ǹ� HashSet<String>
	 * �Ľṹ������ת����List<String> ��data �е����������Ƚϡ�data �е�һ�������� �� List<String>.
	 * �������Ľ�����������C�е�������+1�� 
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
			System.out.println("+++++++++++++++ѭ��ǰ��list:" + list);

			for (List<String> itemData : data) {// �ó�ÿһ������

				ArrayList<String> tempList = new ArrayList<String>();
				tempList.addAll(list);
				boolean flag = !tempList.retainAll(itemData);

				System.out.println("**************");
				System.out.println("itemData" + itemData);
				System.out.println(flag);
				System.out.println("ѭ�����list:" + list);
				System.out.println("**************");

				if (flag) {// �ж��� �����ݵĹ�ϵ������������ݵ��Ӽ�
					if (result.containsKey(item)) { // ���result�а��� ������
						Integer i = result.get(item);
						result.put(item, i + 1);
					} else {
						result.put(item, 1);
					}
				}
			}
		}
		removeMinSup(result);
		System.out
				.println("�����ǲ��Խ���----------------------------------------------------");
		printHashMap(result);
		return result;
	}

	/**
	 * ���Լ�֦Ч���õ��ģ����Ҫɾ��
	 * 
	 * @param itemSet
	 */
	private static void testPruning(HashMap<HashSet<String>, Integer> itemSet) {
		HashSet<String> a1 = new HashSet<String>();
		a1.add("I1");
		a1.add("I2");
		itemSet.put(a1, 4);

		HashSet<String> a2 = new HashSet<String>();
		a2.add("I1");
		a2.add("I3");
		itemSet.put(a2, 4);

		HashSet<String> a3 = new HashSet<String>();
		a3.add("I1");
		a3.add("I5");
		itemSet.put(a3, 2);

		HashSet<String> a4 = new HashSet<String>();
		a4.add("I2");
		a4.add("I3");
		itemSet.put(a4, 4);

		HashSet<String> a5 = new HashSet<String>();
		a5.add("I4");
		a5.add("I2");
		itemSet.put(a5, 2);

		HashSet<String> a6 = new HashSet<String>();
		a6.add("I5");
		a6.add("I2");
		itemSet.put(a6, 2);

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

		// System.out.println("--------------------");
		// System.out.println("�����ӡ�������Ӻͼ�֦֮ǰ�Ľ��");
		//
		// for (HashSet<String> s : c) {
		// for (String i : s) {
		// System.out.print(i + "\t");
		// }
		// System.out.println();
		// }
		//
		// Ҫ��֦��,�ж�c������һ��������Ӽ� �Ƿ���keySet�а������ǵĻ���
		Pruning(c, keySet);

		// private static HashMap<HashSet<String>, Integer> itemSetGen(
		// ArrayList<List<String>> data, HashSet<HashSet<String>> c) {}

		// System.out.println("�����ӡ�������Ӻͼ�֦֮��Ľ��");
		//
		// for (HashSet<String> s : c) {
		// for (String i : s) {
		// System.out.print(i + "\t");
		// }
		// System.out.println();
		// }

		return c;

	}

	/**
	 * ��������֦����������⣡������ �ж�c������һ��������Ӽ� �Ƿ���keySet�а��� ����������һ �ó�c��һ��Ǹ�
	 * HashSet<String> �ṹ���ó��ýṹ��ȫ�� ��������һ�� ���Ӽ�������Ӽ������ײ��� �ڶ������ж���ÿ���Ӽ��Ƿ��� keySet
	 * �е�Ԫ�أ��ǵĻ��ͱ��������ǵĻ�����ɾ��
	 * 
	 * @param c
	 * @param keySet
	 */
	private static void Pruning(HashSet<HashSet<String>> c,
			Set<HashSet<String>> keySet) {

		// remove �д�ŵ�����Ҫ��ɾ����c�е��
		HashSet<HashSet<String>> remove = new HashSet<HashSet<String>>();

		for (HashSet<String> item : c) {// ����c��ÿһ��Ԫ�أ��ж��Ƿ�Ӧ�ñ�ɾ����
			// �����ж��ǲ�����Ҫ��֦,���������û�߼�֦��·��
			boolean flag = false;
			for (String s : item) {// ����ÿһ��Ԫ���е�һ�ȥ������

				HashSet<String> subset = new HashSet<String>();
				subset.addAll(item);
				subset.remove(s);

				// Ҫ�ж�ԭ�����������ǲ��ǰ����Ӽ�
				// System.out.println("keySet"+keySet);
				// System.out.println("subset"+subset);
				if (!keySet.contains(subset)) {
					flag = true;
				}
			}

			// System.out.println(flag);
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

		// �������Ӻ�Ľ��
		// for(HashSet<String> i : c){
		// for(String s:i){
		// System.out.print(s);
		// }
		// System.out.println();
		// }

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
		// System.out.println("��ʼ��ʼ����������֧�ֶ�");
		// printHashMap(l1);

	}

	/**
	 * 
	 * ������ӡHashMap�������õġ�
	 * 
	 * @param l1
	 */
	private static void printHashMap(HashMap<HashSet<String>, Integer> l1) {
		Set<Map.Entry<HashSet<String>, Integer>> set2 = l1.entrySet();
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

		// for(List<String> item:data){
		// for(String s:item){
		// System.out.print(s+"\t");
		// }
		// System.out.println();
		// }

	}
}
