package com.shellever.multiautocompletetextview;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;


// AutoCompleteTextView是从第一个字符就开始联想，不依赖于光标所在的位置信息
// 而MultiAutoCompleteTextView则可以指定字符开始联想，依赖于光标所在的位置信息

// 1.
// AutoCompleteTextView不能实现自动补全邮箱功能原因：
// 它在补全时会替换当前输入的整个文本字符串数据

// 2.
// MultiAutoCompleteTextView可以指定分隔符，实现连续的替换功能，而不会改变分隔符之前的文本字符串数据
// 故可以实现自动补全邮箱后缀的域名信息

// http://blog.csdn.net/lcq5211314123/article/details/40346263
// http://www.oschina.net/code/snippet_135777_18552
public class MainActivity extends AppCompatActivity {

    private MultiAutoCompleteTextView mCommaTokenizerTv;
    private MultiAutoCompleteTextView mShellTokenizerTv;
    private MultiAutoCompleteTextView mMailboxTokenizerTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupCommaTokenizerTextView();
        setupShellTokenizerTextView();
        setupMailboxTokenizerTextView();

        testSpanned();
    }

    private void setupCommaTokenizerTextView() {
        String[] mOnePieceCnArray = getResources().getStringArray(R.array.onepiece_cn);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, mOnePieceCnArray);
        mCommaTokenizerTv = (MultiAutoCompleteTextView) findViewById(R.id.tv_comma);
        mCommaTokenizerTv.setAdapter(adapter);
        mCommaTokenizerTv.setThreshold(1);
        mCommaTokenizerTv.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());  // 逗号+空格
    }

    private void setupShellTokenizerTextView() {
        String[] mOnePieceArray = getResources().getStringArray(R.array.onepiece);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, mOnePieceArray);
        mShellTokenizerTv = (MultiAutoCompleteTextView) findViewById(R.id.tv_shell);
        mShellTokenizerTv.setAdapter(adapter);
        mShellTokenizerTv.setThreshold(1);
        mShellTokenizerTv.setTokenizer(new ShellTokenizer(this, '-', true));    // 只有分词符，不额外加空格
    }

    private void setupMailboxTokenizerTextView() {
        String[] mMailboxPostfixArray = getResources().getStringArray(R.array.mail_box_postfix);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mMailboxPostfixArray);
        mMailboxTokenizerTv = (MultiAutoCompleteTextView) findViewById(R.id.tv_mail_box);
        mMailboxTokenizerTv.setAdapter(adapter);
        mMailboxTokenizerTv.setThreshold(1);    // 因重写enoughToFilter()方法，故threshold设置已没有意义
        mMailboxTokenizerTv.setDropDownHeight(dp2px(200));     // 设置自动提示列表的高度为200dp
        mMailboxTokenizerTv.setTokenizer(new MailBoxTokenizer());
    }

    private int dp2px(int dip){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, dm);
    }

    private void testSpanned() {
        TextView test = (TextView) findViewById(R.id.tv_test);

        String text = "Hello world and Hello me";
        SpannableString sp = new SpannableString(text);
        sp.setSpan(new ForegroundColorSpan(Color.RED), 0, text.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        if (sp instanceof Spanned) {
            // 创建一个新的SpannableString，传进来的sp会被退化成String，导致sp2中丢失掉了sp中的样式配置
            SpannableString sp2 = new SpannableString(sp + ", ");
            // 故需要借助TextUtils.copySpansFrom从sp中复制原来的样式到新的sp2中，以保持原先样式不变情况下添加一个逗号和空格
            TextUtils.copySpansFrom((Spanned) sp, 0, sp.length(), Object.class, sp2, 0);
            test.setText(sp2);
        }

//        test.setText(sp);
    }
}
