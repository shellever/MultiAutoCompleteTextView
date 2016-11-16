package com.shellever.multiautocompletetextview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

/**
 * Author: Shellever
 * Date:   11/11/2016
 * Email:  shellever@163.com
 */
// 执行流程猜想：
// 0. 每当用户输入一个字符或者删除一个字符时都会触发enoughToFilter()方法，进行判断是否包含'@'字符且不是第一个字符位置
// 1. 如当用户输入最后一个字符为'@'即"hello@"时，enoughToFilter()方法测试成功返回true，将触发Tokenizer.findTokenStart()方法，
//    返回当前光标所在位置，故得到的过滤后的提示信息将为全部的数据信息
// 2. 当用户再次输入如"hello@1"时，enoughToFilter()方法测试成功返回true，将触发Tokenizer.findTokenStart()方法，
//    '@'后面的字符串的开始位置，故得到的过滤后的提示信息将为以"1"开头的数据信息 (hello@1, 7, 6)
// 3. 当用户点击提示信息"163.com"时，会调用Tokenizer.findTokenStart()方法得到 (hello@1, 7, 6) 6的开始位置，
//    然后将以6开始的后面的字符串替换成"163.com"，完成邮箱后缀的自定补全
//
public class MailBoxAutoCompleteTextView extends MultiAutoCompleteTextView {

    private Context context;

    public MailBoxAutoCompleteTextView(Context context) {
        this(context, null);
    }

    public MailBoxAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    // 当输入@符号时，就会去调用Tokenizer.findTokenStart()方法一次
    // 当点击下拉提示框中的某个信息时，会再次调用Tokenizer.findTokenStart()方法一次，然后再调用terminateToken()方法一次
    @Override
    public boolean enoughToFilter() {
//        Toast.makeText(context, "call enoughToFilter()", Toast.LENGTH_SHORT).show();
        String text = getText().toString();
        // 若用户输入的文本字符串中包含'@'字符且不在第一位，则满足条件返回true，否则返回false
        return text.contains("@") && text.indexOf("@") > 0;
    }
}
