package com.ikuuvpn.app.presentation.subscription

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ikuuvpn.app.presentation.subscription.viewmodel.SubscriptionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionScreen(
    viewModel: SubscriptionViewModel = hiltViewModel(),
    onImportClick: () -> Unit = {}
) {
    val subscriptions by viewModel.subscriptions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("订阅管理") },
                actions = {
                    IconButton(onClick = { viewModel.refreshAllSubscriptions() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "刷新")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onImportClick) {
                Icon(Icons.Default.Add, contentDescription = "导入订阅")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading && subscriptions.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (subscriptions.isEmpty()) {
                Text(
                    text = "暂无订阅，点击右下角按钮导入",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(subscriptions) { subscription ->
                        SubscriptionItem(
                            subscription = subscription,
                            onDelete = { viewModel.deleteSubscription(subscription.id) },
                            onUpdate = { viewModel.updateSubscription(subscription.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SubscriptionItem(
    subscription: com.ikuuvpn.ikuu.model.SubscriptionConfig,
    onDelete: () -> Unit,
    onUpdate: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = subscription.metadata.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Row {
                    IconButton(onClick = onUpdate) {
                        Icon(Icons.Default.Refresh, contentDescription = "更新")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "删除")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "节点数量: ${subscription.proxies.size}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "更新时间: ${android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", subscription.metadata.updateTime)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}