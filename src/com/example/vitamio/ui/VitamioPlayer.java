package com.example.vitamio.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import com.example.vitamio.R;
import com.example.vitamio.utils.PinyinUtils;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created with IntelliJ IDEA.
 * User: william-work from mobicloud.com.cn
 * Date: 12-7-9
 * Time: 下午5:11
 */
public class VitamioPlayer extends FragmentActivity {

    private ViewPager mPager;
    private FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            Fragment result = null;
            switch (position) {
                case 1:
//                    result = new Fra
                    break;
                case 0:
                    break;
                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frament_pager);
        mPager = (ViewPager) findViewById(R.id.pager);
//        mPager.setAdapter(mAdapter);
        try {
            setTitle(PinyinUtils.cn2Spell("你"));
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }
    }
}