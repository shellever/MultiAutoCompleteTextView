package com.shellever.multiautocompletetextview;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

/**
 * Author: Shellever
 * Date:   11/10/2016
 * Email:  shellever@163.com
 */

// This simple Tokenizer can be used for lists where the items are
// separated by a hyphen.
public class ShellTokenizer implements MultiAutoCompleteTextView.Tokenizer {

    private Context context;
    private static int count;
    private char tokenChar;
    private boolean isOnlyToken;

    public ShellTokenizer(Context context) {
        this(context, ',', false);
    }

    public ShellTokenizer(Context context, char tokenChar, boolean isOnlyToken) {
        this.context = context;
        this.tokenChar = tokenChar;
        this.isOnlyToken = isOnlyToken;
        count = 0;
    }

    // Returns the start of the token that ends at offset cursor within text.
    // 5 findTokenStart: Italy, ger, 10     // 每当输入一个字符后就会调用5次
    // findTokenStart: Italy, I, 8, 7       //
    // findTokenStart: Italy, It, 9, 7       //
    @Override
    public int findTokenStart(CharSequence text, int cursor) {
        int i = cursor;

        while (i > 0 && text.charAt(i - 1) != tokenChar) {  // 测试当前光标的前一个位置非','的字符位置
            i--;
        }
        while (i < cursor && text.charAt(i) == ' ') {       // 测试','后面非空格的字符位置
            i++;
        }

        count++;
        Toast.makeText(context, count + " findTokenStart: " + text + ", " + cursor + ", " + i, Toast.LENGTH_SHORT).show();
        return i;       // 返回一个要加分隔符的字符串的开始位置
    }

    // Returns the end of the token (minus trailing punctuation)
    // that begins at offset cursor within text.
    @Override
    public int findTokenEnd(CharSequence text, int cursor) {
        Toast.makeText(context, "findTokenEnd: " + text + ", " + cursor, Toast.LENGTH_SHORT).show();
        int i = cursor;
        int len = text.length();

        while (i < len) {
            if (text.charAt(i) == tokenChar) {
                return i;
            } else {
                i++;
            }
        }

        return len;
    }

    // Returns text, modified, if necessary, to ensure that
    // it ends with a token terminator (for example a space or comma).
    // terminateToken: Italy    // 输入I点击提示信息Italy时被调用，在这之前会调用一次findTokenStart()
    // 回调terminateToken()方法时，传入的text是原始的数据 (CharSequence类型的数据有可能是富文本SpannableString)
    @Override
    public CharSequence terminateToken(CharSequence text) {
        Toast.makeText(context, "terminateToken: " + text, Toast.LENGTH_SHORT).show();
        int i = text.length();

        while (i > 0 && text.charAt(i - 1) == ' ') {    // 去掉原始匹配的数据的末尾空格
            i--;
        }

        if (i > 0 && text.charAt(i - 1) == tokenChar) {       // 判断原始匹配的数据去掉末尾空格后是否含有逗号，有则立即返回
            Toast.makeText(context, "direct return: " + text, Toast.LENGTH_SHORT).show();
            return text;
        } else {

            String result = text + String.valueOf(tokenChar);
            if (!isOnlyToken) {
                result += " ";
            }

            if (text instanceof Spanned) {      // 富文本
                Toast.makeText(context, "Rich Text", Toast.LENGTH_SHORT).show();

                // 创建一个新的SpannableString，传进来的text会被退化成String，导致sp中丢失掉了text中的样式配置
                SpannableString sp = new SpannableString(result);

                // 故需要借助TextUtils.copySpansFrom从text中复制原来的样式到新的sp中，以保持原先样式不变情况下添加一个逗号和空格
                TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, sp, 0);

                return sp;
            } else {
                Toast.makeText(context, "Plain Text", Toast.LENGTH_SHORT).show();   // Plaint Text +
                return result;     // 66Italy, 66
            }
        }
    }
}
