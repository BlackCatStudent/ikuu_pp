# GitHub Actions 编译故障排除指南

## 🔍 如何查看详细错误

### 方法一：在 GitHub Actions 页面查看
1. 访问：https://github.com/BlackCatStudent/ikuu_pp/actions
2. 点击最新的工作流运行
3. 展开失败的步骤（红色❌）
4. 查看详细的错误输出

### 方法二：查看原始日志
如果 GitHub Actions 页面无法显示日志：
- 点击 "View raw logs" 链接
- 或直接访问：`https://github.com/BlackCatStudent/ikuu_pp/commit/[commit-id]/checks/[check-id]/logs/[log-id]`

### 方法三：手动触发诊断工作流
1. 访问 Actions 页面
2. 找到 "Diagnose Build" 工作流
3. 点击 "Run workflow" 按钮
4. 查看诊断报告

## 🐛 常见编译错误及解决方案

### 错误 1：Gradle Wrapper 配置问题
**错误信息**：
```
Could not determine java version from '.../gradle/wrapper/gradle-wrapper.properties'
```

**解决方案**：
- 检查 `gradle/wrapper/gradle-wrapper.properties` 文件
- 确保 `distributionUrl` 使用 `https://` 而不是 `https\://`

### 错误 2：Android SDK 未找到
**错误信息**：
```
SDK location not found
ANDROID_HOME is not set
```

**解决方案**：
- 确保工作流包含 `Setup Android SDK` 步骤
- 检查 Android SDK 版本配置

### 错误 3：依赖下载失败
**错误信息**：
```
Could not resolve com.squareup.retrofit2:retrofit:2.9.0
```

**解决方案**：
- 检查网络连接
- 检查依赖版本是否正确
- 尝试使用镜像源

### 错误 4：Kotlin 编译错误
**错误信息**：
```
Unresolved reference: LoginScreen
Unresolved reference: MainActivity
```

**解决方案**：
- 检查导入语句是否正确
- 确保所有文件都在正确的包路径下
- 检查 Kotlin 插件配置

### 错误 5：NDK 编译错误
**错误信息**：
```
CMake Error: Could not find CMAKE_C_COMPILER
```

**解决方案**：
- 检查 `clash-core/build.gradle.kts` 中的 NDK 配置
- 确保 CMake 版本正确
- 检查 C++ 源文件语法

### 错误 6：内存不足
**错误信息**：
```
Java heap space
Out of memory
```

**解决方案**：
- 在 `build.gradle.kts` 中增加内存配置：
```kotlin
android {
    defaultConfig {
        ...
    }
    buildTypes {
        release {
            ...
        }
    }
}

tasks.withType<Release>().configure {
    jvmArgs("-Xmx4g")
}
```

### 错误 7：签名配置错误
**错误信息**：
```
Execution failed for task ':app:packageRelease'
```

**解决方案**：
- 检查 ProGuard 规则
- 检查签名配置
- 暂时禁用混淆

## 🛠️ 快速修复建议

### 临时禁用 C++ 模块
如果 C++ 编译持续失败，可以暂时移除 `clash-core` 依赖：

在 `app/build.gradle.kts` 中注释掉：
```kotlin
dependencies {
    // implementation(project(":clash-core"))
    implementation(project(":ikuu-api"))
    implementation(project(":common"))
    ...
}
```

### 使用最低 SDK 版本
降低 `minSdk` 和 `targetSdk`：
```kotlin
android {
    compileSdk = 31  // 从 34 降低到 31
    defaultConfig {
        minSdk = 24  // 从 26 降低到 24
        ...
    }
}
```

### 清理 Gradle 缓存
在本地运行：
```bash
./gradlew clean
./gradlew --refresh-dependencies
```

## 📋 需要提供的信息

如果以上方法都无法解决问题，请提供：

1. **完整的错误日志**
   - 从 GitHub Actions 页面复制
   - 或截图错误信息

2. **工作流运行 ID**
   - 例如：`#62813146274`

3. **失败的步骤**
   - 例如："Build Debug APK (No Native)"

4. **错误信息的前几行**
   - 通常包含 `FAILURE:` 或 `ERROR:` 关键字

## 🎯 下一步

1. **查看简化版工作流**
   - "Build Android APK (Simplified)" 应该更容易成功
   - 如果成功，说明问题是 C++ 代码

2. **如果简化版也失败**
   - 提供详细错误信息
   - 我可以帮你分析和修复

3. **考虑替代方案**
   - 暂时不使用 Clash Meta 核心
   - 使用外部 Clash 应用
   - 通过 API 控制外部应用

## 💡 提示

- GitHub Actions 日志有时会延迟显示
- 尝试刷新页面或等待几分钟
- 检查仓库设置中的 Actions 权限
- 确保所有配置文件都已推送到 GitHub