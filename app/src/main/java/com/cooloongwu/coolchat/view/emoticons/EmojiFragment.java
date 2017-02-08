package com.cooloongwu.coolchat.view.emoticons;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.emoji.entity.Emoji;
import com.cooloongwu.emoji.utils.EmoticonsUtils;
import com.cooloongwu.emoji.view.EmojiIndicatorView;

import java.util.ArrayList;
import java.util.List;


public class EmojiFragment extends Fragment {

    private int columns = 7;
    private int rows = 4;

    private ArrayList<Emoji> emojiArrayList;
    private ArrayList<View> viewPagerItems = new ArrayList<>();
    private EmojiIndicatorView indicatorView;

    private OnEmojiClickListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        emojiArrayList = EmoticonsUtils.getEmojiList();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnEmojiClickListener) {
            this.listener = (OnEmojiClickListener) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_emoji, container, false);

        initViews(rootView);
        return rootView;
    }

    private void initViews(View view) {
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        indicatorView = (EmojiIndicatorView) view.findViewById(R.id.emoji_indicator);

        indicatorView.init(getPagerCount(emojiArrayList));
        viewPagerItems.clear();
        for (int i = 0; i < getPagerCount(emojiArrayList); i++) {
            viewPagerItems.add(getViewPagerItem(i, emojiArrayList));
        }
        FaceVPAdapter mVpAdapter = new FaceVPAdapter(viewPagerItems);
        viewPager.setAdapter(mVpAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int oldPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicatorView.playBy(oldPosition, position);
                oldPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 根据表情数量以及GridView设置的行数和列数计算Pager数量
     *
     * @return
     */
    private int getPagerCount(ArrayList<Emoji> list) {
        int count = list.size();
        return count % (columns * rows - 1) == 0 ? count / (columns * rows - 1)
                : count / (columns * rows - 1) + 1;
    }

    private View getViewPagerItem(int position, ArrayList<Emoji> list) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_face_grid, null);//表情布局
        GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);
        /**
         * 注：因为每一页末尾都有一个删除图标，所以每一页的实际表情columns *　rows　－　1; 空出最后一个位置给删除图标
         * */
        final List<Emoji> subList = new ArrayList<>();
        subList.addAll(list.subList(position * (columns * rows - 1),
                (columns * rows - 1) * (position + 1) > list
                        .size() ? list.size() : (columns
                        * rows - 1)
                        * (position + 1)));
        /**
         * 末尾添加删除图标
         * */
        if (subList.size() < (columns * rows - 1)) {
            for (int i = subList.size(); i < (columns * rows - 1); i++) {
                subList.add(null);
            }
        }
        Emoji deleteEmoji = new Emoji();
        deleteEmoji.setEmojiUri(R.drawable.emoji_delete);
        subList.add(deleteEmoji);
        FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, getActivity());
        gridview.setAdapter(mGvAdapter);
        gridview.setNumColumns(columns);
        // 单击表情执行的操作
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == columns * rows - 1) {
                    if (listener != null) {
                        listener.onEmojiDelete();
                    }
                    return;
                }
                if (listener != null) {
                    listener.onEmojiClick(subList.get(position));
                }
            }
        });

        return gridview;
    }

    private class FaceVPAdapter extends PagerAdapter {
        // 界面列表
        private List<View> views;

        public FaceVPAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
            (arg0).removeView((View) (arg2));
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            (container).addView(views.get(position));
            return views.get(position);
        }

        // 判断是否由对象生成界
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }
    }

    public interface OnEmojiClickListener {
        void onEmojiDelete();

        void onEmojiClick(Emoji emoji);
    }


    class FaceGVAdapter extends BaseAdapter {
        private List<Emoji> list;
        private Context mContext;

        FaceGVAdapter(List<Emoji> list, Context mContext) {
            super();
            this.list = list;
            this.mContext = mContext;
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_emoji, null);
                holder.iv = (ImageView) convertView.findViewById(R.id.img_emoji);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (list.get(position) != null) {
                holder.iv.setImageBitmap(EmoticonsUtils.decodeSampledBitmapFromResource(getActivity().getResources(), list.get(position).getEmojiUri(),
                        EmoticonsUtils.dip2px(getActivity(), 32), EmoticonsUtils.dip2px(getActivity(), 32)));
            }
            return convertView;
        }

        class ViewHolder {
            ImageView iv;
        }
    }
}
