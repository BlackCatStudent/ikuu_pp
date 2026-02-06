package com.ikuuvpn.app.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ikuuvpn.app.presentation.settings.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val coreInstalled by viewModel.coreInstalled.collectAsState()
    val isDownloading by viewModel.isDownloading.collectAsState()
    val clashRunning by viewModel.clashRunning.collectAsState()
    val downloadProgress by viewModel.downloadProgress.collectAsState()
    val coreVersion by viewModel.coreVersion.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Clash Meta 核心",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (coreInstalled) "已安装" else "未安装",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (coreVersion.isNotBlank()) {
                            Text(
                                text = coreVersion,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (isDownloading) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            LinearProgressIndicator(
                                progress = downloadProgress / 100f,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = "下载中... ${downloadProgress.toInt()}%",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.downloadCore() },
                                modifier = Modifier.weight(1f),
                                enabled = !coreInstalled
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("下载核心")
                            }

                            OutlinedButton(
                                onClick = { viewModel.deleteCore() },
                                modifier = Modifier.weight(1f),
                                enabled = coreInstalled
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("删除核心")
                            }
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Clash 服务",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (clashRunning) "运行中" else "已停止",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (clashRunning) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                        Switch(
                            checked = clashRunning,
                            onCheckedChange = { checked ->
                                if (checked) {
                                    viewModel.startClash()
                                } else {
                                    viewModel.stopClash()
                                }
                            }
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "数据库",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Button(
                        onClick = { viewModel.downloadCountryDb() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.CloudDownload, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("下载 GeoIP 数据库")
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "缓存管理",
                        style = MaterialTheme.typography.titleMedium
                    )

                    OutlinedButton(
                        onClick = { viewModel.clearCache() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.CleaningServices, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("清除缓存")
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "关于",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "Ikuu VPN",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "版本 1.0.0",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}