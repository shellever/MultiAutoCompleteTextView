package com.shellever.multiautocompletetextview;

import android.content.Context;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

/**
 * Author: Shellever
 * Date:   11/11/2016
 * Email:  shellever@163.com
 */

public class MailBoxTokenizer implements MultiAutoCompleteTextView.Tokenizer {
    public int findTokenStart(CharSequence text, int cursor) {
        int i = cursor;

        while (i > 0 && text.charAt(i - 1) != '@') {
            i--;
        }
//        while (i < cursor && text.charAt(i) == ' ') {
//            i++;
//        }
        return i;
    }

    public int findTokenEnd(CharSequence text, int cursor) {
        int i = cursor;
        int len = text.length();

        while (i < len) {
            if (text.charAt(i) == '@') {
                return i;
            } else {
                i++;
            }
        }

        return len;
    }

    public CharSequence terminateToken(CharSequence text) {
        return text;
    }
}
