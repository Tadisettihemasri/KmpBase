package com.a4b.kmpbase.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.a4b.kmpbase.core.designsystem.theme.AppTheme

/**
 * LazyColumn over [LazyPagingItems] with full-screen loading/error/empty states and an
 * append footer (spinner or retry). Pair with `pager.flow.cachedIn(viewModelScope)` in the ViewModel.
 */
@Composable
fun <T : Any> PagingList(
    items: LazyPagingItems<T>,
    key: (T) -> Any,
    emptyTitle: String,
    errorMessage: (Throwable) -> String,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(),
    itemContent: @Composable (T) -> Unit,
) {
    val refresh = items.loadState.refresh
    when {
        refresh is LoadState.Loading && items.itemCount == 0 -> LoadingView(modifier)
        refresh is LoadState.Error && items.itemCount == 0 ->
            ErrorView(message = errorMessage(refresh.error), modifier = modifier, onRetry = items::retry)
        refresh is LoadState.NotLoading && items.itemCount == 0 -> EmptyView(title = emptyTitle, modifier = modifier)
        else -> LazyColumn(modifier = modifier, state = state, contentPadding = contentPadding) {
            items(count = items.itemCount, key = items.itemKey(key)) { index ->
                items[index]?.let { itemContent(it) }
            }
            item { AppendFooter(items.loadState.append, onRetry = items::retry) }
        }
    }
}

@Composable
private fun AppendFooter(state: LoadState, onRetry: () -> Unit) {
    Box(Modifier.fillMaxWidth().padding(AppTheme.spacing.lg), contentAlignment = Alignment.Center) {
        when (state) {
            is LoadState.Loading -> CircularProgressIndicator()
            is LoadState.Error -> TextButton(onClick = onRetry) { Text("Couldn't load more. Retry") }
            is LoadState.NotLoading -> Unit
        }
    }
}
