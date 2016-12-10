package core.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

public class DisabledSwipeToRefresh extends SwipeRefreshLayout {
    public DisabledSwipeToRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}