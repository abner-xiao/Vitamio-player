package com.example.vitamio.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.vitamio.R;
import com.example.vitamio.model.DatabaseHelper;
import com.example.vitamio.model.MediaFile;
import com.example.vitamio.model.PFile;
import com.example.vitamio.ui.adapter.ArrayListAdapter;
import com.example.vitamio.utils.FileUtils;
import com.example.vitamio.utils.PinyinUtils;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.File;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: william-work from mobicloud.com.cn
 * Date: 12-7-11
 * Time: 上午10:27
 */
public class FragmentFile extends FragmentBase implements AdapterView.OnItemClickListener {

    public static final String TAG = FragmentFile.class.getSimpleName();

    private FilterAdapter mAdapter;
    private TextView first_letter_overlay;
    private ImageView alphabet_scroller;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        //initView
        first_letter_overlay = (TextView) v.findViewById(R.id.first_letter_overlay);
        alphabet_scroller = (ImageView) v.findViewById(R.id.alphabet_scroller);

        alphabet_scroller.setClickable(true);
        alphabet_scroller.setOnTouchListener(asOnTouch);
        mListView.setOnItemClickListener(this);
        mListView.setVisibility(View.VISIBLE);

        if (!new DatabaseHelper(getActivity()).isExist()) {
            new ScanVideoTask().execute();
        } else {
            new DataTask().execute();
        }
//        mListView.setBackgroundColor(Color.RED);

        return v;
    }

    private class DataTask extends AsyncTask<Void, Void, ArrayList<PFile>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingLayout.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }

        @Override
        protected ArrayList<PFile> doInBackground(Void... params) {
            return FileUtils.getAllSortFiles(getActivity());
        }

        @Override
        protected void onPostExecute(ArrayList<PFile> list) {
            super.onPostExecute(list);
            mAdapter = new FilterAdapter(getActivity(), FileUtils.getAllSortFiles(getActivity()));
            mListView.setAdapter(mAdapter);

            mLoadingLayout.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }
    }

    private class ScanVideoTask extends AsyncTask<Void, File, ArrayList<PFile>> {
        private ProgressDialog pd;
        //        private AlertDialog ad;
        private ArrayList<File> files = new ArrayList<File>();

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(getActivity());
            pd.setMessage("正在扫描视频文件");
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected ArrayList<PFile> doInBackground(Void... params) {
            //查找媒体文件
            eachAllMedia(Environment.getExternalStorageDirectory());
            try {
                for (File f : files) {
                    ContentValues values = new ContentValues();
                    String title = FileUtils.getFileNameNoExt(f);
                    values.put(MediaFile.Medias.TITLE, title);
                    values.put(MediaFile.Medias.TITLE_PINYIN, PinyinUtils.cn2Spell(title));
                    values.put(MediaFile.Medias.PATH, f.getPath());
                    values.put(MediaFile.Medias.POSITION, 0);
                    values.put(MediaFile.Medias.DURATION, 0);
                    values.put(MediaFile.Medias.LAST_ACCESS_TIME, System.currentTimeMillis());
                    getActivity().getContentResolver().insert(MediaFile.Medias.CONTENT_URI, values);
                    values.clear();
                }
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }
            return FileUtils.getAllSortFiles(getActivity());
        }

        @Override
        protected void onProgressUpdate(File... values) {
            File file = values[0];
            files.add(file);
            pd.setMessage(file.getName());
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<PFile> list) {
            super.onPostExecute(list);
            mAdapter = new FilterAdapter(getActivity(), list);
            mListView.setAdapter(mAdapter);
            pd.dismiss();
        }

        /**
         * 遍历sdCard文件，查找视频文件
         *
         * @param file
         */
        public void eachAllMedia(File file) {
            if (file != null && file.exists() && file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        if (f.isDirectory()) {
                            eachAllMedia(f);
                        } else if (f.exists() && f.canRead() && FileUtils.isVideoOrAudio(f)) {
                            if (f.length() > 5 * 1024 * 1024L)
                                publishProgress(f);
                        }
                    }
                }
            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final PFile file = mAdapter.getItem(position);
        File temp = new File(file.path);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(temp), "video/*");
        startActivity(intent);
    }


    private class FilterAdapter extends ArrayListAdapter<PFile> {

        private FilterAdapter(Context context, ArrayList arrayList) {
            super(context, arrayList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final PFile f = getItem(position);
            if (convertView == null) {
                final LayoutInflater mInflater = getActivity().getLayoutInflater();
                convertView = mInflater.inflate(R.layout.fragment_item, null);
            }
            ((TextView) convertView.findViewById(R.id.title)).setText(f.title);
            return convertView;
        }
    }

    private View.OnTouchListener asOnTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    alphabet_scroller.setPressed(true);
                    first_letter_overlay.setVisibility(View.VISIBLE);
                    mathScrollerPosition(event.getY());
                    break;
                case MotionEvent.ACTION_UP:
                    alphabet_scroller.setPressed(false);
                    first_letter_overlay.setVisibility(View.GONE);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mathScrollerPosition(event.getY());
                    break;
                default:
                    throw new IllegalArgumentException("无用触屏事件 " + event.getAction());
            }
            return false;
        }
    };

    /**
     * 显示字符
     *
     * @param y
     */
    private void mathScrollerPosition(float y) {
        int height = alphabet_scroller.getHeight();
        float charHeight = height / 28.0f;
        char c = 'A';
        if (y < 0)
            y = 0;
        else if (y > height)
            y = height;

        int index = (int) (y / charHeight) - 1;
        if (index < 0)
            index = 0;
        else if (index > 25)
            index = 25;

        String key = String.valueOf((char) (c + index));
        first_letter_overlay.setText(key);

        int position = 0;
        if (index == 0)
            mListView.setSelection(0);
        else if (index == 25)
            mListView.setSelection(mAdapter.getCount() - 1);
        else {
            for (PFile item : mAdapter.getAll()) {
                if (item.title_pinyin.startsWith(key)) {
                    mListView.setSelection(position);
                    break;
                }
                position++;
            }
        }
    }

}
