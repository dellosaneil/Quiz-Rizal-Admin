package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookDestinations
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookState
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookTextFieldTypes
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookViewModel
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.PublishPhase
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BACKGROUND
import com.thelazybattley.joserizalquizadmin.presentation.util.Category

@Composable
fun AddBookScreen(
    modifier: Modifier = Modifier,
    navigate: (AddBookDestinations) -> Unit
) {
    val viewModel = hiltViewModel<AddBookViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = state.destination) {
        state.destination?.let { destination ->
            navigate(destination)
            viewModel.handleAction(action = AddBookActions.NavigateDestination(destination = null))
        }
    }

    AddBookScreen(
        modifier = modifier,
        state = state,
        callback = viewModel
    )
}

@Composable
private fun AddBookScreen(
    modifier: Modifier = Modifier,
    callback: AddBookCallback,
    state: AddBookState
) {
    val isPublishing = state.publishPhase == PublishPhase.PUBLISHING
    // Leaving mid-publish would hide whether the book was saved.
    BackHandler(enabled = isPublishing) {}

    val goBack = {
        callback.handleAction(action = AddBookActions.NavigateDestination(destination = AddBookDestinations.Back))
    }
    val published = state.publishPhase as? PublishPhase.Published

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AddBookTopBar(
                modifier = Modifier.fillMaxWidth(),
                showTitle = published == null,
                icon = if (published == null) Icons.AutoMirrored.Rounded.KeyboardArrowLeft else Icons.Rounded.Close,
                iconDescription = stringResource(id = if (published == null) R.string.back else R.string.close),
                iconEnabled = !isPublishing,
                onIconClicked = goBack
            )
        },
        containerColor = APP_BACKGROUND,
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        if (published != null) {
            AddBookPublishedContent(
                modifier = Modifier
                    .padding(paddingValues = innerPadding)
                    .fillMaxSize(),
                state = state,
                quizId = published.quizId,
                callback = callback
            )
        } else {
            AddBookForm(
                modifier = Modifier
                    .padding(paddingValues = innerPadding)
                    .fillMaxSize(),
                state = state,
                callback = callback
            )
        }
    }
}

@Composable
private fun AddBookTopBar(
    modifier: Modifier = Modifier,
    showTitle: Boolean,
    icon: ImageVector,
    iconDescription: String,
    iconEnabled: Boolean,
    onIconClicked: () -> Unit
) {
    Row(
        modifier = modifier.height(height = 60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 4.dp)
    ) {
        IconButton(
            onClick = onIconClicked,
            enabled = iconEnabled,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = colors.espresso,
                disabledContentColor = colors.taupe
            )
        ) {
            Icon(imageVector = icon, contentDescription = iconDescription)
        }
        if (showTitle) {
            Text(
                modifier = Modifier.semantics { heading() },
                text = stringResource(id = R.string.add_a_book),
                style = typography.bold16.copy(fontSize = 20.sp),
                color = colors.espresso
            )
        }
    }
}

@Composable
private fun AddBookForm(
    modifier: Modifier = Modifier,
    state: AddBookState,
    callback: AddBookCallback
) {
    val isEditing = state.publishPhase == PublishPhase.EDITING
    val lazyColumnState = rememberLazyListState()
    val previousChapterCount = remember { mutableIntStateOf(value = state.chapters.size) }
    LaunchedEffect(key1 = state.chapters.size) {
        // Keep the Add chapter button in view after adding one, not on removal or first load.
        if (state.chapters.size > previousChapterCount.intValue) {
            lazyColumnState.animateScrollToItem(index = lazyColumnState.layoutInfo.totalItemsCount.dec())
        }
        previousChapterCount.intValue = state.chapters.size
    }

    Column(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .weight(weight = 1f)
                .alpha(alpha = if (isEditing) 1f else 0.6f),
            state = lazyColumnState,
            contentPadding = PaddingValues(top = 6.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(space = 10.dp)
        ) {
            if (state.isFromSuggestion) {
                item {
                    AddBookSuggestionBanner(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    )
                }
            }
            item { SectionLabel(text = stringResource(id = R.string.category)) }
            item {
                AddBookCategory(
                    modifier = Modifier.fillMaxWidth(),
                    selectedCategory = state.category,
                    enabled = isEditing,
                    callback = callback
                )
            }
            item { SectionLabel(modifier = Modifier.padding(top = 12.dp), text = stringResource(id = R.string.details)) }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(space = 14.dp)) {
                    AddBookTextFieldTypes.entries.forEach { type ->
                        AddBookTextField(
                            modifier = Modifier.fillMaxWidth(),
                            type = type,
                            initialText = when (type) {
                                AddBookTextFieldTypes.TITLE -> state.title
                                AddBookTextFieldTypes.AUTHOR -> state.author
                            },
                            enabled = isEditing,
                            callback = callback
                        )
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionLabel(text = stringResource(id = R.string.chapters))
                    Text(
                        text = pluralStringResource(
                            id = R.plurals.chapter_count,
                            count = state.chapters.size,
                            state.chapters.size
                        ),
                        style = typography.regular12,
                        color = colors.woodsmokeBrown
                    )
                }
            }
            itemsIndexed(items = state.chapters) { index, chapter ->
                AddBookChapterRow(
                    modifier = Modifier.fillMaxWidth(),
                    index = index,
                    text = chapter,
                    canRemove = state.chapters.size > 1,
                    enabled = isEditing,
                    callback = callback
                )
            }
            item {
                AddBookAddChapterButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEditing
                ) {
                    callback.handleAction(action = AddBookActions.Chapter.Add)
                }
            }
        }
        AddBookPublishBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            state = state,
            callback = callback
        )
    }
}

@Composable
private fun SectionLabel(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        style = typography.semiBold11.copy(fontFamily = FontFamily.Monospace),
        color = colors.taupe
    )
}

private class AddBookStateProvider : PreviewParameterProvider<AddBookState> {
    private val filled = AddBookState(
        title = "Noli Me Tangere",
        author = "José Rizal",
        category = Category.NOVEL,
        chapters = listOf("Isang Pagtitipon", "Crisostomo Ibarra", "Ang Hapunan")
    )
    override val values = sequenceOf(
        AddBookState(),
        AddBookState(title = "Makamisa", author = "José Rizal", isFromSuggestion = true),
        filled.copy(publishPhase = PublishPhase.PUBLISHING),
        filled.copy(publishFailed = true),
        filled.copy(publishPhase = PublishPhase.Published(quizId = "preview"))
    )
}

@PreviewLightDark
@Composable
private fun Preview(@PreviewParameter(AddBookStateProvider::class) state: AddBookState) {
    AppTheme {
        AddBookScreen(
            modifier = Modifier,
            callback = AddBookCallback.default(),
            state = state
        )
    }
}
