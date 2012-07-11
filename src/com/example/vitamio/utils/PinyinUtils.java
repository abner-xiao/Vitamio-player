package com.example.vitamio.utils;

import android.util.Log;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created with IntelliJ IDEA.
 * User: william-work from mobicloud.com.cn
 * Date: 12-7-11
 * Time: 上午11:21
 */
public final class PinyinUtils {
    public static final String TAG = PinyinUtils.class.getSimpleName();
    private static final HanyuPinyinOutputFormat spellFormat;

    static {
        spellFormat = new HanyuPinyinOutputFormat();
        spellFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        spellFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    }

    /**
     * 获取汉子串拼音首字母，英文字符不变
     *
     * @param chinese 汉子串
     * @return 汉语拼音首字母
     */
    public static String cn2FirstSpell(String chinese) throws BadHanyuPinyinOutputFormatCombination {
        StringBuffer pyBuffer = new StringBuffer();
        for (int i = 0; i < chinese.length(); ++i) {
            String temp = getCharacterPinyin(chinese.charAt(i));
            pyBuffer.append(temp.charAt(0));
        }
        return pyBuffer.toString().trim();
    }

    /**
     * 获取汉字串，英文字符不变
     *
     * @param chinese
     * @return 汉子拼音
     * @throws BadHanyuPinyinOutputFormatCombination
     *
     */
    public static String cn2Spell(String chinese) throws BadHanyuPinyinOutputFormatCombination {
        StringBuffer pyBuffer = new StringBuffer();
        for (int i = 0; i < chinese.length(); ++i) {
            String tempPinyin = getCharacterPinyin(chinese.charAt(i));
            pyBuffer.append(tempPinyin);
        }
        return pyBuffer.toString().trim();
    }

    /**
     * 转换单个字符
     *
     * @param c
     * @return
     * @throws BadHanyuPinyinOutputFormatCombination
     *
     */
    public static String getCharacterPinyin(char c) throws BadHanyuPinyinOutputFormatCombination {
        String[] Pinyin = PinyinHelper.toHanyuPinyinStringArray(c, spellFormat);
        //如果不是汉子，上面的方法会返回Null, 我们返回原字符
        if (Pinyin == null) {
            return String.valueOf(c);
        }
        //如果多音字，只取第一个
        return Pinyin[0];
    }
}
