package core_kt.view

import android.content.Context
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class DisabledSwipeToRefresh(context: Context, attrs: AttributeSet) : SwipeRefreshLayout(context, attrs) {

    override fun isEnabled(): Boolean {
        return false
    }
}