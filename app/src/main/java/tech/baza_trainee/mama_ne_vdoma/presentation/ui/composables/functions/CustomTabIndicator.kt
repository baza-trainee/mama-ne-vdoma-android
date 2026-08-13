package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.TabIndicatorScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

private const val ANIMATION_DURATION = 250

/**
 * Indicator sized to [tabWidth] and centred under the selected tab.
 *
 * Positioning is delegated to [TabIndicatorScope.tabIndicatorOffset], which spans the selected tab
 * and reads the tab positions inside the tab row's own layout, so it is correct on the first frame.
 * The width is animated here in composition, because the tab row composes its indicator together
 * with the tabs, and a width fed back out of the layout phase would arrive a frame late.
 *
 * The scope is a parameter because it cannot be a second receiver alongside [Modifier].
 */
@Composable
fun Modifier.customTabIndicatorOffset(
    scope: TabIndicatorScope,
    selectedTabIndex: Int,
    tabWidth: Dp
): Modifier {
    val currentTabWidth by animateDpAsState(
        targetValue = tabWidth,
        animationSpec = tween(durationMillis = ANIMATION_DURATION, easing = FastOutSlowInEasing),
        label = ""
    )

    return with(scope) {
        this@customTabIndicatorOffset
            .tabIndicatorOffset(selectedTabIndex)
            .wrapContentSize(Alignment.BottomCenter)
            .width(currentTabWidth)
    }
}
