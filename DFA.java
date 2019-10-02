import java.util.*;

public class DFA {

	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
        list.add("ATCC");
        list.add("ATGG");
        list.add("GTCAC");
        list.add("TTACG");
        list.add("AATTCG");
        SensitiveWordUtil.init(new HashSet<>(list));
        String str = "TATCCG";
        System.out.println(SensitiveWordUtil.contains(str));
        String str2 = "ABCDEF";
        System.out.println(SensitiveWordUtil.contains(str2));

	}

}

class SensitiveWordUtil {

    /**
     * ���д�ƥ�����
     */
    private static final int MinMatchTYpe = 1;      //��Сƥ������磺���дʿ�["�й�","�й���"]����䣺"�����й���"��ƥ����������[�й�]��
    private static final int MaxMatchType = 2;      //���ƥ������磺���дʿ�["�й�","�й���"]����䣺"�����й���"��ƥ����������[�й���]

    private static final char maskChar = '*'; // ����

    /**
     * ���дʼ���
     */
    private static HashMap sensitiveWordMap;

    /**
     * ��ʼ�����дʿ⣬����DFA�㷨ģ��
     *
     * @param sensitiveWordSet ���дʿ�
     */
    public static synchronized void init(Set<String> sensitiveWordSet) {
        initSensitiveWordMap(sensitiveWordSet);
    }

    /**
     * ��ʼ�����дʿ⣬����DFA�㷨ģ��
     *
     * @param sensitiveWordSet ���дʿ�
     */
    private static void initSensitiveWordMap(Set<String> sensitiveWordSet) {
        //��ʼ�����д��������������ݲ���
        sensitiveWordMap = new HashMap(sensitiveWordSet.size());
        String key;
        Map nowMap;
        Map<String, String> newWorMap;
        //����sensitiveWordSet
        Iterator<String> iterator = sensitiveWordSet.iterator();
        while (iterator.hasNext()) {
            //�ؼ���
            key = iterator.next();
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                //ת����char��
                char keyChar = key.charAt(i);
                //���л�ȡ�ؼ���
                Object wordMap = nowMap.get(keyChar);
                //������ڸ�key��ֱ�Ӹ�ֵ��������һ��ѭ����ȡ
                if (wordMap != null) {
                    nowMap = (Map) wordMap;
                } else {
                    //���������򹹽�һ��map��ͬʱ��isEnd����Ϊ0����Ϊ���������һ��
                    newWorMap = new HashMap<>();
                    //�������һ��
                    newWorMap.put("isEnd", "0");
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if (i == key.length() - 1) {
                    //���һ��
                    nowMap.put("isEnd", "1");
                }
            }
        }
    }

    /**
     * �ж������Ƿ���������ַ�
     *
     * @param txt       ����
     * @param matchType ƥ����� 1����Сƥ�����2�����ƥ�����
     * @return ����������true�����򷵻�false
     */
    public static boolean contains(String txt, int matchType) {
        boolean flag = false;
        for (int i = 0; i < txt.length(); i++) {
            int matchFlag = checkSensitiveWord(txt, i, matchType); //�ж��Ƿ���������ַ�
            if (matchFlag > 0) {    //����0���ڣ�����true
                flag = true;
            }
        }
        return flag;
    }

    /**
     * �ж������Ƿ���������ַ�
     *
     * @param txt ����
     * @return ����������true�����򷵻�false
     */
    public static boolean contains(String txt) {
        return contains(txt, MaxMatchType);
    }

    /**
     * ��ȡ�����е����д�
     *
     * @param txt       ����
     * @param matchType ƥ����� 1����Сƥ�����2�����ƥ�����
     */
    public static Set<String> getSensitiveWord(String txt, int matchType) {
        Set<String> sensitiveWordList = new HashSet<>();

        for (int i = 0; i < txt.length(); i++) {
            //�ж��Ƿ���������ַ�
            int length = checkSensitiveWord(txt, i, matchType);
            if (length > 0) {//����,����list��
                sensitiveWordList.add(txt.substring(i, i + length));
                i = i + length - 1;//��1��ԭ������Ϊfor������
            }
        }

        return sensitiveWordList;
    }

    /**
     * ��ȡ�����е����д�
     *
     * @param txt ����
     */
    public static Set<String> getSensitiveWord(String txt) {
        return getSensitiveWord(txt, MaxMatchType);
    }

    /**
     * �滻�������ַ�
     *
     * @param txt         �ı�
     * @param replaceChar �滻���ַ���ƥ������д����ַ�����滻���� ��䣺�Ұ��й��� ���дʣ��й��ˣ��滻�ַ���*�� �滻������Ұ�***
     * @param matchType   ���д�ƥ�����
     */
    public static String replaceSensitiveWord(String txt, char replaceChar, int matchType) {
        String resultTxt = txt;
        //��ȡ���е����д�
        Set<String> set = getSensitiveWord(txt, matchType);
        Iterator<String> iterator = set.iterator();
        String word;
        String replaceString;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }

        return resultTxt;
    }

    /**
     * �滻�������ַ�
     *
     * @param txt         �ı�
     * @param replaceChar �滻���ַ���ƥ������д����ַ�����滻���� ��䣺�Ұ��й��� ���дʣ��й��ˣ��滻�ַ���*�� �滻������Ұ�***
     */
    public static String replaceSensitiveWord(String txt, char replaceChar) {
        return replaceSensitiveWord(txt, replaceChar, MaxMatchType);
    }

    /**
     * �滻�������ַ�
     *
     * @param txt       �ı�
     * @param matchType ���д�ƥ�����
     */
    public static String replaceSensitiveWord(String txt, int matchType) {
        String resultTxt = txt;
        //��ȡ���е����д�
        Set<String> set = getSensitiveWord(txt, matchType);
        Iterator<String> iterator = set.iterator();
        String word;
        while (iterator.hasNext()) {
            word = iterator.next();
            resultTxt = resultTxt.replaceAll(word, getReplaceChars(maskChar, word.length()));
        }

        return resultTxt;
    }

    /**
     * ��ȡ�滻�ַ���
     */
    private static String getReplaceChars(char replaceChar, int length) {
        StringBuilder resultReplace = new StringBuilder(String.valueOf(replaceChar));
        for (int i = 1; i < length; i++) {
            resultReplace.append(replaceChar);
        }

        return resultReplace.toString();
    }

    /**
     * ����������Ƿ���������ַ������������£�<br>
     *
     * @return ������ڣ��򷵻����д��ַ��ĳ��ȣ������ڷ���0
     */
    private static int checkSensitiveWord(String txt, int beginIndex, int matchType) {
        //���дʽ�����ʶλ���������д�ֻ��1λ�����
        boolean flag = false;
        //ƥ���ʶ��Ĭ��Ϊ0
        int matchFlag = 0;
        char word;
        Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            //��ȡָ��key
            nowMap = (Map) nowMap.get(word);
            if (nowMap != null) {//���ڣ����ж��Ƿ�Ϊ���һ��
                //�ҵ���Ӧkey��ƥ���ʶ+1
                matchFlag++;
                //���Ϊ���һ��ƥ�����,����ѭ��������ƥ���ʶ��
                if ("1".equals(nowMap.get("isEnd"))) {
                    //������־λΪtrue
                    flag = true;
                    //��С����ֱ�ӷ���,���������������
                    if (MinMatchTYpe == matchType) {
                        break;
                    }
                }
            } else {//�����ڣ�ֱ�ӷ���
                break;
            }
        }
        if (matchFlag < 2 || !flag) {//���ȱ�����ڵ���1��Ϊ��
            matchFlag = 0;
        }
        return matchFlag;
    }
}
