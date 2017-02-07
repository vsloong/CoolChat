package com.cooloongwu.emoji.entity;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * emoji和文字的对应
 * Created by CooLoongWu on 2017-2-6 17:22.
 */

public class Emoticons {
    private static Map<String, String> emojiMap;

    static {
        emojiMap = new HashMap<>();

        //默认表情
        emojiMap.put("[呲牙]", "e001");
        emojiMap.put("[吐舌头]", "e002");
        emojiMap.put("[流汗]", "e003");
        emojiMap.put("[捂嘴笑]", "e004");
        emojiMap.put("[拜拜]", "e005");
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
        emojiMap.put("[奋斗]", "e043");
        emojiMap.put("[发呆]", "e044");
        emojiMap.put("[右哼哼]", "e045");
        emojiMap.put("[抱抱]", "e046");
        emojiMap.put("[坏笑]", "e047");
        emojiMap.put("[飞吻]", "e048");
        emojiMap.put("[鄙视]", "e049");
        emojiMap.put("[晕]", "e050");
        emojiMap.put("[大兵]", "e051");
        emojiMap.put("[快哭了]", "e052");
        emojiMap.put("[强]", "e053");
        emojiMap.put("[弱]", "e054");
        emojiMap.put("[握手]", "e055");
        emojiMap.put("[胜利]", "e056");
        emojiMap.put("[抱拳]", "e057");
        emojiMap.put("[凋零]", "e058");
        emojiMap.put("[饭]", "e059");
        emojiMap.put("[蛋糕]", "e060");
        emojiMap.put("[西瓜]", "e061");
        emojiMap.put("[啤酒]", "e062");
        emojiMap.put("[瓢虫]", "e063");
        emojiMap.put("[勾引]", "e064");
        emojiMap.put("[OK]", "e065");
        emojiMap.put("[爱你]", "e066");
        emojiMap.put("[咖啡]", "e067");
        emojiMap.put("[月亮]", "e068");
        emojiMap.put("[刀]", "e069");
        emojiMap.put("[发抖]", "e070");
        emojiMap.put("[差劲]", "e071");
        emojiMap.put("[拳头]", "e072");
        emojiMap.put("[心碎]", "e073");
        emojiMap.put("[太阳]", "e074");
        emojiMap.put("[礼物]", "e075");
        emojiMap.put("[足球]", "e076");
        emojiMap.put("[骷髅]", "e077");
        emojiMap.put("[挥手]", "e078");
        emojiMap.put("[闪电]", "e079");
        emojiMap.put("[饥饿]", "e080");
        emojiMap.put("[困]", "e081");
        emojiMap.put("[大骂]", "e082");
        emojiMap.put("[折磨]", "e083");
        emojiMap.put("[挖鼻]", "e084");
        emojiMap.put("[拍手]", "e085");
        emojiMap.put("[尴尬]", "e086");
        emojiMap.put("[左哼哼]", "e087");
        emojiMap.put("[哈欠]", "e088");
        emojiMap.put("[委屈]", "e089");
        emojiMap.put("[惊讶]", "e090");
        emojiMap.put("[篮球]", "e091");
        emojiMap.put("[乒乓]", "e092");
        emojiMap.put("[NO]", "e093");
        emojiMap.put("[跳跳]", "e094");
        emojiMap.put("[怄火]", "e095");
        emojiMap.put("[转圈]", "e096");
        emojiMap.put("[磕头]", "e097");
        emojiMap.put("[回头]", "e098");
        emojiMap.put("[跳绳]", "e099");
        emojiMap.put("[激动]", "e100");
        emojiMap.put("[街舞]", "e101");
        emojiMap.put("[献吻]", "e102");
        emojiMap.put("[左太极]", "e103");
        emojiMap.put("[右太极]", "e104");
        emojiMap.put("[闭嘴]", "e105");
        emojiMap.put("[闭嘴]", "e106");
        emojiMap.put("[双喜]", "e107");
        emojiMap.put("[鞭炮]", "e108");
        emojiMap.put("[灯笼]", "e109");
        emojiMap.put("[发财]", "e110");
        emojiMap.put("[K歌]", "e111");
        emojiMap.put("[购物]", "e112");
        emojiMap.put("[邮件]", "e113");
        emojiMap.put("[帅]", "e114");
        emojiMap.put("[喝彩]", "e115");
        emojiMap.put("[祈祷]", "e116");
        emojiMap.put("[爆筋]", "e117");
        emojiMap.put("[棒棒糖]", "e118");
        emojiMap.put("[喝奶]", "e119");
        emojiMap.put("[下面]", "e120");
        emojiMap.put("[香蕉]", "e121");
        emojiMap.put("[飞机]", "e122");
        emojiMap.put("[开车]", "e123");
        emojiMap.put("[高铁左车头]", "e124");
        emojiMap.put("[车厢]", "e125");
        emojiMap.put("[高铁右车头]", "e126");
        emojiMap.put("[多云]", "e127");
        emojiMap.put("[下雨]", "e128");
        emojiMap.put("[钞票]", "e129");
        emojiMap.put("[熊猫]", "e130");
        emojiMap.put("[灯泡]", "e131");
        emojiMap.put("[风车]", "e132");
        emojiMap.put("[闹钟]", "e133");
        emojiMap.put("[打伞]", "e134");
        emojiMap.put("[彩球]", "e135");
        emojiMap.put("[钻戒]", "e136");
        emojiMap.put("[沙发]", "e137");
        emojiMap.put("[纸巾]", "e138");
        emojiMap.put("[药]", "e139");
        emojiMap.put("[手枪]", "e140");
        emojiMap.put("[青蛙]", "e141");
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
