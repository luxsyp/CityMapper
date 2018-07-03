package john.snow.citymapper.stoppoint.ui;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        // hide the divider for the last child
        if (position == parent.getAdapter().getItemCount() - 1) {
            outRect.setEmpty();
        } else {
            outRect.bottom = verticalSpaceHeight;
        }
    }
}