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

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

public class Main {

	// ���õ���С֧�ֶȼ���Ϊ 2.
	private static int min_sup = 2;
	private static double min_confid = 0.7;

	public static void main(String[] args) {
		// �����������ԭʼ���ݣ�ÿһ����һ������ÿһ�������ְ�����һ��ֵ��
		ArrayList<List<String>> data = new ArrayList<List<String>>();
		String path = "data.txt";
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

	

		System.out.println("���ղ�����Ƶ�����*********************");
		printHashMap(itemSetPre);

		System.out.println("���ղ����Ĺ���������*********************");
		associationRulesGen(data, itemSetPre);

	}

	/**
	 * ����Ƶ����������Ҵ�ӡ���������� ���ڲ�����ÿһ��Ƶ���Ҫ�ҵ�һ���������
	 * 
	 * @param data
	 *            ԭʼ���� ArrayList<List<String>> data
	 * @param itemSet
	 *            ���ɵ�Ƶ���
	 */
	private static void associationRulesGen(ArrayList<List<String>> data,
			HashMap<HashSet<String>, Integer> itemSet) {
	
		// ��Ƶ��� �е�Ƶ������ȡ����������һ������
		// HashSet<HashSet<String>>���磺{{1,2,3}��{1,2,5}}������������е�ÿһ��,�������е�ÿһ��Ƶ��������²���.

		/*
		 * 1������������ͬʱ�� ��Ƶ�����������Ŀ������ֱ�Ӵ�����Ĳ��� itemSet�ж�ȡ��ÿ��Ƶ����ֻ��Ҫ��ȡһ�μ��ɡ���Ϊ���ӡ�
		 * 2���ó���Ƶ��������зǿ��Ӽ�
		 * ���ṹҲ��HashSet<HashSet<String>>���磺{{1,2}��{1,5}��{2}����������}��
		 * 3������ÿһ���ǿ��Ӽ��������²����� 1������÷ǿ��Ӽ��������еĳ��ִ�������Ϊ��ĸ����һ�γ�����Ϊ���Ŷ�ֵ��
		 * 2����һ��������Ÿ÷ǿ��Ӽ���������Ƶ����Ĳ����� if ���Ŷ�ֵ�����䷧ֵ���������²����� 1�����÷ǿ��Ӽ� �� ���Ӧ�Ĳ���������
		 * Map�У�Map�ṹ��HashMap<HashSet<String>,HashSet<String>>
		 * 2������һ����Map������һ��Map��
		 * ��Map�ṹ��HashMap<HashMap<HashSet<String>,HashSet<String>>,Double>
		 */
		// ÿһ��Ƶ����ѭ��һ�λ��������һ��Map
		// �ṹ��HashMap<HashMap<HashSet<String>,HashSet<String>>,Double>
		
		

		Set<HashSet<String>> freqItem = itemSet.keySet();
	
	
		HashMap<HashMap<HashSet<String>, HashSet<String>>, Double> result = new HashMap<HashMap<HashSet<String>, HashSet<String>>, Double>();

		for (HashSet<String> aFreqItem : freqItem) {
			double confidence = itemSet.get(aFreqItem);
			
			// �õ���ÿһ��Ƶ����ķǿ��Ӽ��������������㡣
			HashSet<HashSet<String>> freqItemSubset = new HashSet<HashSet<String>>();
			getSubset(aFreqItem, freqItemSubset);

			for (HashSet<String> aFreqItemSubset : freqItemSubset) {
				// ����õ�����ÿһ���Ӽ��������г��ֵĴ�����
				int num = numOfSubset(data, aFreqItemSubset);
//				System.out.println(aFreqItemSubset+"----------"+num);
				double confid = confidence /num;
//				System.out.println("confidence:"+confid);

				if (confid > min_confid) {

					// �����˲���
					HashSet<String> subsetCompl = getComplement(aFreqItem,
							aFreqItemSubset);
					HashMap<HashSet<String>, HashSet<String>> assRule = new HashMap<HashSet<String>, HashSet<String>>();
					assRule.put(aFreqItemSubset, subsetCompl);
					result.put(assRule, confid);
				}
			}

			
		}
		printAssRules(result);

	}

	/**
	 * ��ӡ������ṹ��OK
	 * 
	 * @param result
	 */
	private static void printAssRules(
			HashMap<HashMap<HashSet<String>, HashSet<String>>, Double> result) {
		
		for(Map.Entry<HashMap<HashSet<String>, HashSet<String>>, Double> me :result.entrySet()){			
			
			HashMap<HashSet<String>, HashSet<String>> key = me.getKey();
			for(Map.Entry<HashSet<String>, HashSet<String>> me2:key.entrySet()){
				
				System.out.print(me2.getKey()+"==>"+me2.getValue());
				System.out.println(", confidence:"+me.getValue());
			}

			
		}
		
	}

	/**
	 * �����Ӽ���Ӧ�Ĳ���
	 * 
	 * @param item
	 *            ȫ��
	 * @param itemSubset
	 *            �Ӽ�
	 * @return
	 */
	private static HashSet<String> getComplement(HashSet<String> item,
			HashSet<String> itemSubset) {
		HashSet<String> result = new HashSet<String>();
		result.addAll(item);

		for (String str : itemSubset) {
			result.remove(str);
		}

		return result;
	}

	/**
	 * �õ�����ÿһ���Ӽ��������г��ֵĴ���
	 * 
	 * @param data
	 *            ԭʼ������
	 * @param item
	 *            ���е�һ�
	 * @return
	 */
	private static int numOfSubset(ArrayList<List<String>> data,
			HashSet<String> item) {
		int num = 0;
		// List���췽������ֱ�ӽ�setת����list������һ��ת������list��
		ArrayList<String> list = new ArrayList<String>(item);
		for (List<String> itemData : data) {
			ArrayList<String> tempList = new ArrayList<String>();
			tempList.addAll(list);
			boolean flag = !tempList.retainAll(itemData);

			if (flag) {// �ж��� �����ݵĹ�ϵ������������ݵ��Ӽ�
				num++;
			}
		}
		return num;
	}

	/**
	 * �õ�ÿһ��Ƶ����ķǿ��Ӽ�. ѭ�����ԣ��жϵ�ǰԪ����Ŀ������Ϊ1ʱ��ֹͣ�ݹ�
	 * ÿ���ҵ�������һ�����Ӽ�����{1,2,3}���ҵ��Ӽ�{{1,2}��{2,3}��{1,3}}������������ÿһ���Ӽ����ٲ���������һ�����Ӽ���
	 * 
	 * @param item
	 *            ��Ӧ��Ƶ�����{1,2,3}
	 * @param result
	 *            ������Ӽ��ṹ����{{1,2}��{1,5}��{2}����������}
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

	// �õ�ĳ�����Ԫ������ 1 ���Ӽ���
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
