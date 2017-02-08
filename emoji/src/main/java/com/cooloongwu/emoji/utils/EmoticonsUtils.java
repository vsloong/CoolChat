package com.cooloongwu.emoji.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cooloongwu.emoji.R;
import com.cooloongwu.emoji.entity.Emoji;

import java.util.ArrayList;

/**
 * Created by CooLoongWu on 2017-2-7 15:25.
 */

public class EmoticonsUtils {

    private static ArrayList<Emoji> emojiArrayList;

    private static final int[] emojiUriArray = {
            R.drawable.e001,
            R.drawable.e002,
            R.drawable.e003,
            R.drawable.e004,
            R.drawable.e005,
            R.drawable.e006,
            R.drawable.e007,
            R.drawable.e008,
            R.drawable.e009,
            R.drawable.e010,
            R.drawable.e011,
            R.drawable.e012,
            R.drawable.e013,
            R.drawable.e014,
            R.drawable.e015,
            R.drawable.e016,
            R.drawable.e017,
            R.drawable.e018,
            R.drawable.e019,
            R.drawable.e020,
            R.drawable.e021,
            R.drawable.e022,
            R.drawable.e023,
            R.drawable.e024,
            R.drawable.e025,
            R.drawable.e026,
            R.drawable.e027,
            R.drawable.e028,
            R.drawable.e029,
            R.drawable.e030,
            R.drawable.e031,
            R.drawable.e032,
            R.drawable.e033,
            R.drawable.e034,
            R.drawable.e035,
            R.drawable.e036,
            R.drawable.e037,
            R.drawable.e038,
            R.drawable.e039,
            R.drawable.e040,
            R.drawable.e041,
            R.drawable.e042,
            R.drawable.e043,
            R.drawable.e044,
            R.drawable.e045,
            R.drawable.e046,
            R.drawable.e047,
            R.drawable.e048,
            R.drawable.e049,
            R.drawable.e050,
            R.drawable.e051,
            R.drawable.e052,
            R.drawable.e053,
            R.drawable.e054,
            R.drawable.e055,
            R.drawable.e056,
            R.drawable.e057,
            R.drawable.e058,
            R.drawable.e059,
            R.drawable.e060,
            R.drawable.e061,
            R.drawable.e062,
            R.drawable.e063,
            R.drawable.e064,
            R.drawable.e065,
            R.drawable.e066,
            R.drawable.e067,
            R.drawable.e068,
            R.drawable.e069,
            R.drawable.e070,
            R.drawable.e071,
            R.drawable.e072,
            R.drawable.e073,
            R.drawable.e074,
            R.drawable.e075,
            R.drawable.e076,
            R.drawable.e077,
            R.drawable.e078,
            R.drawable.e079,
            R.drawable.e080,
            R.drawable.e081,
            R.drawable.e082,
            R.drawable.e083,
            R.drawable.e084,
            R.drawable.e085,
            R.drawable.e086,
            R.drawable.e087,
            R.drawable.e088,
            R.drawable.e089,
            R.drawable.e090,
            R.drawable.e091,
            R.drawable.e092,
            R.drawable.e093,
            R.drawable.e094,
            R.drawable.e095,
            R.drawable.e096,
            R.drawable.e097,
            R.drawable.e098,
            R.drawable.e099,
            R.drawable.e100,
            R.drawable.e101,
            R.drawable.e102,
            R.drawable.e103,
            R.drawable.e104,
            R.drawable.e105,
            R.drawable.e106,
            R.drawable.e107,
            R.drawable.e108,
            R.drawable.e109,
            R.drawable.e110,
            R.drawable.e111,
            R.drawable.e112,
            R.drawable.e113,
            R.drawable.e114,
            R.drawable.e115,
            R.drawable.e116,
            R.drawable.e117,
            R.drawable.e118,
            R.drawable.e119,
            R.drawable.e120,
            R.drawable.e121,
            R.drawable.e122,
            R.drawable.e123,
            R.drawable.e124,
            R.drawable.e125,
            R.drawable.e126,
            R.drawable.e127,
            R.drawable.e128,
            R.drawable.e129,
            R.drawable.e130,
            R.drawable.e131,
            R.drawable.e132,
            R.drawable.e133,
            R.drawable.e134,
            R.drawable.e135,
            R.drawable.e136,
            R.drawable.e137,
            R.drawable.e138,
            R.drawable.e139,
            R.drawable.e140,
            R.drawable.e141,
    };

    private static final String[] emojiContentArray = {
            "[呲牙]",
            "[吐舌头]",
            "[流汗]",
            "[捂嘴笑]",
            "[拜拜]",
            "[敲击]",
            "[擦汗]",
            "[猪头]",
            "[玫瑰]",
            "[大哭]",
            "[哭泣]",
            "[嘘]",
            "[酷]",
            "[抓狂]",
            "[委屈]",
            "[大便]",
            "[炸弹]",
            "[菜刀]",
            "[可爱]",
            "[色]",
            "[害羞]",
            "[得意]",
            "[吐]",
            "[微笑]",
            "[发怒]",
            "[尴尬]",
            "[惊恐]",
            "[冷汗]",
            "[爱心]",
            "[示爱]",
            "[白眼]",
            "[傲慢]",
            "[难过]",
            "[惊讶]",
            "[疑问]",
            "[睡]",
            "[亲亲]",
            "[憨笑]",
            "[爱情]",
            "[衰]",
            "[撇嘴]",
            "[奸笑]",
            "[奋斗]",
            "[发呆]",
            "[右哼哼]",
            "[抱抱]",
            "[坏笑]",
            "[飞吻]",
            "[鄙视]",
            "[晕]",
            "[大兵]",
            "[快哭了]",
            "[强]",
            "[弱]",
            "[握手]",
            "[胜利]",
            "[抱拳]",
            "[凋零]",
            "[饭]",
            "[蛋糕]",
            "[西瓜]",
            "[啤酒]",
            "[瓢虫]",
            "[勾引]",
            "[OK]",
            "[爱你]",
            "[咖啡]",
            "[月亮]",
            "[刀]",
            "[发抖]",
            "[差劲]",
            "[拳头]",
            "[心碎]",
            "[太阳]",
            "[礼物]",
            "[足球]",
            "[骷髅]",
            "[挥手]",
            "[闪电]",
            "[饥饿]",
            "[困]",
            "[大骂]",
            "[折磨]",
            "[挖鼻]",
            "[拍手]",
            "[尴尬]",
            "[左哼哼]",
            "[哈欠]",
            "[委屈]",
            "[惊讶]",
            "[篮球]",
            "[乒乓]",
            "[NO]",
            "[跳跳]",
            "[怄火]",
            "[转圈]",
            "[磕头]",
            "[回头]",
            "[跳绳]",
            "[激动]",
            "[街舞]",
            "[献吻]",
            "[左太极]",
            "[右太极]",
            "[闭嘴]",
            "[闭嘴]",
            "[双喜]",
            "[鞭炮]",
            "[灯笼]",
            "[发财]",
            "[K歌]",
            "[购物]",
            "[邮件]",
            "[帅]",
            "[喝彩]",
            "[祈祷]",
            "[爆筋]",
            "[棒棒糖]",
            "[喝奶]",
            "[下面]",
            "[香蕉]",
            "[飞机]",
            "[开车]",
            "[高铁左车头]",
            "[车厢]",
            "[高铁右车头]",
            "[多云]",
            "[下雨]",
            "[钞票]",
            "[熊猫]",
            "[灯泡]",
            "[风车]",
            "[闹钟]",
            "[打伞]",
            "[彩球]",
            "[钻戒]",
            "[沙发]",
            "[纸巾]",
            "[药]",
            "[手枪]",
            "[青蛙]",
    };

    public static ArrayList<Emoji> getEmojiList() {
        if (emojiArrayList == null) {
            emojiArrayList = generateEmojis();
        }
        return emojiArrayList;
    }

    private static ArrayList<Emoji> generateEmojis() {
        ArrayList<Emoji> list = new ArrayList<>();
        for (int i = 0; i < emojiUriArray.length; i++) {
            Emoji emoji = new Emoji();
            emoji.setEmojiUri(emojiUriArray[i]);
            emoji.setContent(emojiContentArray[i]);
            list.add(emoji);
        }
        return list;
    }

//    static {
//        emojiArrayList = generateEmojis();
//    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
