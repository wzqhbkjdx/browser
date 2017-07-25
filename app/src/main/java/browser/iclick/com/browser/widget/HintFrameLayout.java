package browser.iclick.com.browser.widget;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import browser.iclick.com.browser.R;

/**
 * Created by bym on 2017/6/26.
 */

public class HintFrameLayout extends FrameLayout implements TextWatcher {

    private final Rect bounds;
    private final Paint paint;

    private String hint;
    private boolean drawHint;
    private float padding;
    private float animationOffset;


    public HintFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.bounds = new Rect();
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        setWillNotDraw(false);
    }

    @Keep
    public float getAnimationOffset() {
        return animationOffset;
    }

    @Keep
    public void setAnimationOffset(float animationOffset) {
        this.animationOffset = animationOffset;
        invalidate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        final InlineAutocompleteEditText editText = (InlineAutocompleteEditText) findViewById(R.id.url_edit);
        final CharSequence editTextHint = editText.getHint();

        if(TextUtils.isEmpty(editTextHint)) {
            return;
        }

        editText.addTextChangedListener(this);

        this.padding = editText.getPaddingStart();
        this.hint = editTextHint.toString();


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
