package com.thelazybattley.joserizalquizadmin.presentation.feature.release.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.release.ReleaseOverview
import com.thelazybattley.joserizalquizadmin.domain.model.release.isRevertible
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui.ModerateEmptyState
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui.ModerateSkeletonRow
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.PushPhase
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ReleaseActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ReleaseCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ReleaseOverviewState
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ReleaseState
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BACKGROUND

@Composable
fun ReleaseScreen(
    modifier: Modifier = Modifier,
    state: ReleaseState,
    callback: ReleaseCallback
) {
    val overview = (state.overview as? ReleaseOverviewState.Loaded)?.overview

    if (state.pushPhase == PushPhase.CONFIRMING && overview != null) {
        ReleaseConfirmSheet(changes = overview.changes, callback = callback)
    }

    state.pendingRevert?.let { change ->
        ReleaseRevertSheet(
            change = change,
            isReverting = state.isReverting,
            revertFailed = state.revertFailed,
            callback = callback
        )
    }

    Scaffold(
        modifier = modifier,
        containerColor = APP_BACKGROUND,
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.weight(weight = 1f),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(space = 10.dp)
            ) {
                statusBanners(state = state)
                when (val overviewState = state.overview) {
                    ReleaseOverviewState.Loading -> loadingItems()
                    ReleaseOverviewState.Failed -> item {
                        Column(horizontalAlignment = Alignment.Start) {
                            ReleaseStatusBanner(
                                modifier = Modifier.fillMaxWidth(),
                                type = ReleaseBannerType.ERROR,
                                title = stringResource(id = R.string.release_load_failed_title),
                                message = stringResource(id = R.string.release_load_failed_message)
                            )
                            TextButton(onClick = { callback.handleAction(action = ReleaseActions.Retry) }) {
                                Text(
                                    text = stringResource(id = R.string.try_again),
                                    style = typography.bold14,
                                    color = colors.maroon
                                )
                            }
                        }
                    }

                    is ReleaseOverviewState.Loaded -> overviewItems(
                        overview = overviewState.overview,
                        canRevert = state.pushPhase == PushPhase.IDLE && !state.isReverting,
                        callback = callback
                    )
                }
            }
            if (overview != null && (overview.changes.isNotEmpty() || state.pushPhase == PushPhase.PUSHING)) {
                ReleasePushBar(
                    modifier = Modifier.padding(top = 12.dp),
                    changeCount = overview.changes.size,
                    bookCount = overview.debug.bookCount,
                    isPushing = state.pushPhase == PushPhase.PUSHING,
                    callback = callback
                )
            }
        }
    }
}

private fun LazyListScope.statusBanners(state: ReleaseState) {
    if (state.revertSucceeded) {
        item {
            ReleaseStatusBanner(
                modifier = Modifier.fillMaxWidth(),
                type = ReleaseBannerType.SUCCESS,
                title = stringResource(id = R.string.reverted_title),
                message = stringResource(id = R.string.reverted_message)
            )
        }
    }
    state.pushedChangeCount?.let { count ->
        item {
            ReleaseStatusBanner(
                modifier = Modifier.fillMaxWidth(),
                type = ReleaseBannerType.SUCCESS,
                title = stringResource(id = R.string.release_up_to_date),
                message = pluralStringResource(id = R.plurals.changes_now_live, count = count, count)
            )
        }
    }
    if (state.pushFailed) {
        item {
            ReleaseStatusBanner(
                modifier = Modifier.fillMaxWidth(),
                type = ReleaseBannerType.ERROR,
                title = stringResource(id = R.string.push_failed_title),
                message = stringResource(id = R.string.push_failed_message)
            )
        }
    }
}

private fun LazyListScope.loadingItems() {
    item { SectionLabel(text = R.string.builds) }
    item {
        Row(horizontalArrangement = Arrangement.spacedBy(space = 10.dp)) {
            repeat(times = 2) {
                ModerateSkeletonRow(
                    modifier = Modifier.weight(weight = 1f),
                    titleWidthFraction = 0.6f,
                    subtitleWidthFraction = 0.8f
                )
            }
        }
    }
    item { SectionLabel(text = R.string.not_yet_live, modifier = Modifier.padding(top = 10.dp)) }
    items(items = listOf(0.6f, 0.75f, 0.5f)) { titleWidth ->
        ModerateSkeletonRow(
            modifier = Modifier.fillMaxWidth(),
            titleWidthFraction = titleWidth,
            subtitleWidthFraction = 0.45f
        )
    }
}

private fun LazyListScope.overviewItems(
    overview: ReleaseOverview,
    canRevert: Boolean,
    callback: ReleaseCallback
) {
    item { SectionLabel(text = R.string.builds) }
    item {
        Row(horizontalArrangement = Arrangement.spacedBy(space = 10.dp)) {
            ReleaseBuildCard(
                modifier = Modifier.weight(weight = 1f),
                name = stringResource(id = R.string.debug_build),
                tag = stringResource(id = R.string.working),
                tagContainerColor = colors.parchment,
                tagContentColor = colors.woodsmokeBrown,
                summary = overview.debug
            )
            ReleaseBuildCard(
                modifier = Modifier.weight(weight = 1f),
                name = stringResource(id = R.string.release_build),
                tag = stringResource(id = R.string.live),
                tagContainerColor = colors.softSage,
                tagContentColor = colors.deepMoss,
                summary = overview.release
            )
        }
    }
    if (overview.changes.isEmpty()) {
        item {
            ModerateEmptyState(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                glyph = "✓",
                messageRes = R.string.release_matches_debug
            )
        }
        return
    }
    item {
        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionLabel(text = R.string.not_yet_live)
            Text(
                text = pluralStringResource(id = R.plurals.change_count, count = overview.changes.size, overview.changes.size),
                style = typography.regular12,
                color = colors.woodsmokeBrown
            )
        }
    }
    items(items = overview.changes) { change ->
        ReleaseChangeCard(
            modifier = Modifier.fillMaxWidth(),
            change = change,
            onRevert = if (canRevert && change.isRevertible) {
                { callback.handleAction(action = ReleaseActions.RequestRevert(change = change)) }
            } else {
                null
            }
        )
    }
}

@Composable
private fun SectionLabel(text: Int, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = stringResource(id = text),
        style = typography.semiBold11.copy(fontFamily = FontFamily.Monospace),
        color = colors.taupe
    )
}

private class ReleaseStateProvider : PreviewParameterProvider<ReleaseState> {
    override val values = sequenceOf(
        ReleaseState(overview = ReleaseOverviewState.Loaded(overview = ReleaseOverview.dummy())),
        ReleaseState(),
        ReleaseState(
            overview = ReleaseOverviewState.Loaded(overview = ReleaseOverview.dummy()),
            pushPhase = PushPhase.PUSHING
        ),
        ReleaseState(
            overview = ReleaseOverviewState.Loaded(
                overview = ReleaseOverview.dummy().let { it.copy(release = it.debug, changes = emptyList()) }
            ),
            pushedChangeCount = 5
        )
    )
}

@PreviewLightDark
@Composable
private fun Preview(@PreviewParameter(ReleaseStateProvider::class) state: ReleaseState) {
    AppTheme {
        ReleaseScreen(state = state, callback = ReleaseCallback.default())
    }
}
