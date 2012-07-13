package com.example.vitamio.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Gallery;
import android.widget.Toast;
import com.example.vitamio.R;
import com.example.vitamio.model.DatabaseHelper;
import com.example.vitamio.model.MediaFile;
import com.example.vitamio.utils.PinyinUtils;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created with IntelliJ IDEA.
 * User: william-work from mobicloud.com.cn
 * Date: 12-7-9
 * Time: 下午5:11
 */
public class VitamioPlayer extends FragmentActivity {
    public static final String TAG = VitamioPlayer.class.getSimpleName();

    private ViewPager mPager;
    private FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "Pager getItem ! position is: "+ position);
            Fragment result = null;
            switch (position) {
                case 0:
                    result = new FragmentFile();
                    break;
                case 1:
                    result = new FragmentFile();
                    break;
                default:
                    result = new FragmentFile();
                    break;
            }
            return result;
        }

        @Override
        public int getCount() {
            Log.d(TAG, "getCount");
            return 1;
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate");
        setContentView(R.layout.frament_pager);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setCurrentItem(0);
        mPager.setAdapter(mAdapter);
    }
}