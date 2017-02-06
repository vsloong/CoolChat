package com.cooloongwu.emoji.entity;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * emoji和文字的对应
 * Created by CooLoongWu on 2017-2-6 17:22.
 */

public class Emoticons {
    static Map<String, String> emojiMap;

    static {
        emojiMap = new HashMap<>();
        //默认表情
        emojiMap.put("[呲牙]", "e001");
        emojiMap.put("[吐舌头]", "e002");
        emojiMap.put("[流汗]", "e003");
        emojiMap.put("[捂嘴笑]", "e004");
        emojiMap.put("[挥手]", "e005");
        emojiMap.put("[敲击]", "e006");
        emojiMap.put("[擦汗]", "e007");
        emojiMap.put("[猪头]", "e008");
        emojiMap.put("[玫瑰]", "e009");
        emojiMap.put("[大哭]", "e010");
        emojiMap.put("[哭泣]", "e011");
        emojiMap.put("[嘘]", "e012");
        emojiMap.put("[酷]", "e013");
        emojiMap.put("[抓狂]", "e014");
        emojiMap.put("[委屈]", "e015");
        emojiMap.put("[大便]", "e016");
        emojiMap.put("[炸弹]", "e017");
        emojiMap.put("[菜刀]", "e018");
        emojiMap.put("[可爱]", "e019");
        emojiMap.put("[色]", "e020");
        emojiMap.put("[害羞]", "e021");
        emojiMap.put("[得意]", "e022");
        emojiMap.put("[吐]", "e023");
        emojiMap.put("[微笑]", "e024");
        emojiMap.put("[发怒]", "e025");
        emojiMap.put("[尴尬]", "e026");
        emojiMap.put("[惊恐]", "e027");
        emojiMap.put("[冷汗]", "e028");
        emojiMap.put("[爱心]", "e029");
        emojiMap.put("[示爱]", "e030");
        emojiMap.put("[白眼]", "e031");
        emojiMap.put("[傲慢]", "e032");
        emojiMap.put("[难过]", "e033");
        emojiMap.put("[惊讶]", "e034");
        emojiMap.put("[疑问]", "e035");
        emojiMap.put("[睡]", "e036");
        emojiMap.put("[亲亲]", "e037");
        emojiMap.put("[憨笑]", "e038");
        emojiMap.put("[爱情]", "e039");
        emojiMap.put("[衰]", "e040");
        emojiMap.put("[撇嘴]", "e041");
        emojiMap.put("[奸笑]", "e042");
    }

    /**
     * 得到Emoji的名字
     *
     * @param emojiText [挖鼻]
     * @return Emoji的名字
     */
    static String getEmojiName(String emojiText) {
        String emojiName = emojiMap.get(emojiText);
        if (TextUtils.isEmpty(emojiName)) {
            return "";
        } else {
            return emojiName;
        }
    }
}
