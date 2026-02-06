# Ikuu VPN

基于 Clash Android 二次开发的 VPN 客户端，支持 ikuu VPN 服务管理。

## 功能特性

- ikuu 账号登录
- 订阅链接导入
- Clash Meta 核心管理
- 节点管理
- 流量统计

## 技术栈

- Kotlin
- Jetpack Compose
- Hilt (依赖注入)
- Retrofit (网络请求)
- Room (数据库)
- Clash Meta (代理核心)

## 构建要求

- Android Studio Hedgehog | 2023.1.1 或更高版本
- JDK 17
- Android SDK 34
- Gradle 8.2

## 构建步骤

1. 克隆项目
```bash
git clone <repository-url>
cd ikuuvpn_PP
```

2. 在 Android Studio 中打开项目

3. 等待 Gradle 同步完成

4. 点击 Build > Build Bundle(s) / APK(s) > Build APK(s)

## 使用说明

### 首次使用

1. 登录 ikuu 账号
2. 在设置页面下载 Clash Meta 核心
3. 下载 GeoIP 数据库
4. 导入订阅链接
5. 启动 Clash 服务

### 订阅链接格式

```
https://example.com/link/xxx?clash=3&extend=1
```

## 许可证

本项目仅供学习交流使用。