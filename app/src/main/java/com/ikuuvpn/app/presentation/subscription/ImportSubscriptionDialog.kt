package com.ikuuvpn.app.presentation.subscription

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ImportSubscriptionDialog(
    onDismiss: () -> Unit,
    onImport: (url: String, name: String) -> Unit
) {
    var url by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "导入订阅",
                    style = MaterialTheme.typography.titleLarge
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("订阅名称（可选）") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("订阅链接") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("https://example.com/link/xxx?clash=3") }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        enabled = !isLoading
                    ) {
                        Text("取消")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (url.isNotBlank()) {
                                isLoading = true
                                onImport(url, name)
                            }
                        },
                        enabled = url.isNotBlank() && !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("导入")
                        }
                    }
                }
            }
        }
    }
}