# 多场景影像传感设备资源协同调度系统 · 统计分析模块 PRD

| 属性 | 内容 |
|------|------|
| 文档版本 | V1.0 |
| 创建日期 | 2026-05-26 |
| 作者 | — |
| 评审状态 | 待评审 |
| 所属系统 | 多场景影像传感设备资源协同调度系统 |

## 修订记录

| 版本 | 修改日期 | 修改人 | 修改内容 |
|------|----------|--------|----------|
| V1.0 | 2026-05-26 | — | 初稿 |

---

## 一、背景与目标

### 1.1 背景

管理层需要快速掌握数据接入量、处理效率和存储使用情况，业务分析师需要深入分析数据来源分布和趋势变化，当前缺少可视化看板支撑。

**核心痛点**：
- 管理层只能通过人工汇总获取数据规模信息，效率极低
- 数据接入趋势和处理成功率无法量化分析

### 1.2 目标

- 提供实时数据总览看板，满足管理层决策需求
- 支持多维度的处理任务统计分析
- 提供时间粒度可切换的数据趋势分析图表

---

## 二、用户角色

| 角色 | 描述 | 核心诉求 |
|------|------|----------|
| 管理人员 | 查看数据总览看板 | 一眼看清数据规模和系统健康状态 |
| 业务分析师 | 深度分析数据趋势 | 多维度分析数据来源与处理效率 |
| 数据工程师 | 参考统计优化任务 | 发现低效任务和数据瓶颈 |

---

## 三、功能说明

### 3.1 功能清单

| 功能点 | 优先级 | 说明 | 涉及角色 |
|--------|--------|------|----------|
| 数据总览看板 | P0 | 核心指标卡片 + 趋势图 | 管理人员、所有角色 |
| 处理任务统计 | P1 | 执行频次、成功率、类型分布 | 业务分析师、数据工程师 |
| 数据趋势分析 | P1 | 接入量趋势、数据源分布、文件类型分布 | 业务分析师 |

### 3.2 功能详细说明

#### 3.2.1 数据总览看板

**功能描述**：首页核心指标卡片，配合折线图展示近期趋势，数据自动刷新（5分钟间隔）。

**指标卡片**：

| 卡片 | 指标值 | 说明 |
|------|--------|------|
| 数据源总数 | 数值 | 已配置且启用的数据源数量 |
| 今日接入量 | 文件数 / 数据大小 | 今日已接入文件总数和总大小 |
| 今日处理量 | 文件数 | 今日处理任务执行的文件数 |
| 存储使用率 | 百分比 + 进度条 | 当前存储使用百分比 |

**图表**：

| 图表 | 类型 | 时间范围 | X轴 | Y轴 |
|------|------|---------|-----|-----|
| 近30天接入量趋势 | 折线图 | 近30天 | 日期 | 接入文件数 |
| 处理任务成功率趋势 | 折线图 | 近30天 | 日期 | 成功率(%) |

#### 3.2.2 处理任务统计

**功能描述**：对处理任务的执行情况进行多维度统计分析。

| 指标/图表 | 类型 | 说明 |
|---------|------|------|
| 任务总执行次数 | 数值卡片 | 指定周期内全部处理任务的执行总次数 |
| 整体成功率 | 数值+环形图 | 成功执行次数/总执行次数 |
| 各处理类型分布 | 饼图 | 按处理类型统计执行占比 |
| 各任务执行频次排行 | 柱状图（Top10） | 展示执行次数最多的10个任务 |
| 按周期统计 | 时间选择器 | 支持近7天/近30天/近3个月/自定义 |

#### 3.2.3 数据趋势分析

**功能描述**：多维度分析数据接入的时间趋势和来源分布。

| 图表/功能 | 类型 | 说明 |
|---------|------|------|
| 接入量趋势 | 折线图 | 支持日/周/月粒度，可切换 |
| 数据源贡献占比 | 饼图+数值列表 | 各数据源的接入量占比 |
| 文件类型分布 | 环形图 | JPEG/PNG/RAW/MP4等类型的数量占比 |
| 时间范围选择 | 日期选择器 | 支持自定义时间范围 |

---

## 四、业务边界

### In Scope（本期做）

- ✅ 数据总览看板（4个指标卡片 + 2个折线图）
- ✅ 处理任务统计（执行次数、成功率、类型分布、频次排行）
- ✅ 数据趋势分析（接入趋势、数据源分布、文件类型分布）
- ✅ 时间粒度切换（日/周/月）

### Out of Scope（本期不做）

- ❌ 自定义看板（拖拽配置图表布局）：后续迭代
- ❌ 数据导出为 PDF 报表：下期需求

---

## 五、数据模型

> 统计分析模块无独立业务表，通过聚合查询已有数据生成统计结果。依赖以下数据来源：
> - `DATASOURCE_CONFIG`：数据源数量统计
> - `INGEST_RECORD`：接入量统计
> - `PROCESS_EXECUTION`：处理量统计
> - `STORAGE_SNAPSHOT`：存储使用率

---

## 六、接口设计

### 6.1 接口清单

| 接口名称 | 方法 | 路径 | 权限标识 |
|----------|------|------|---------|
| 数据总览看板 | GET | `/api/stats/overview` | stats:overview:view |
| 近30天接入趋势 | GET | `/api/stats/ingest/trend` | stats:overview:view |
| 处理任务统计 | GET | `/api/stats/process/summary` | stats:process:view |
| 数据趋势分析 | GET | `/api/stats/ingest/analysis` | stats:analysis:view |
| 数据源贡献占比 | GET | `/api/stats/datasource/contribution` | stats:analysis:view |
| 文件类型分布 | GET | `/api/stats/filetype/distribution` | stats:analysis:view |

### 6.2 核心接口定义

#### 数据总览看板

```
GET /api/stats/overview
Authorization: Bearer {accessToken}

响应：
{
  "code": 200,
  "data": {
    "datasourceCount": 12,
    "todayIngestCount": 8520,
    "todayIngestSizeDisplay": "18.6 GB",
    "todayProcessCount": 7800,
    "storageUseRate": 80.00,
    "storageIsWarning": false,
    "updatedAt": "2026-05-26 10:00:00"
  }
}
```

#### 近30天接入趋势

```
GET /api/stats/ingest/trend?days=30
Authorization: Bearer {accessToken}

响应：
{
  "code": 200,
  "data": {
    "dates": ["2026-04-27", "2026-04-28", "...", "2026-05-26"],
    "ingestCounts": [6200, 7100, "...", 8520],
    "successRates": [98.5, 99.2, "...", 99.8]
  }
}
```

#### 处理任务统计

```
GET /api/stats/process/summary?period=30d
Authorization: Bearer {accessToken}

Query 参数：
- period  统计周期：7d/30d/90d/custom
- startDate  period=custom时必填
- endDate    period=custom时必填

响应：
{
  "code": 200,
  "data": {
    "totalExecutions": 420,
    "successCount": 408,
    "failCount": 12,
    "successRate": 97.14,
    "processTypeDistribution": [
      { "type": "IMAGE_COMPRESS", "typeLabel": "图像压缩", "count": 180, "percent": 42.86 },
      { "type": "FORMAT_CONVERT", "typeLabel": "格式转换", "count": 120, "percent": 28.57 },
      { "type": "RESOLUTION_RESIZE", "typeLabel": "分辨率调整", "count": 60, "percent": 14.29 },
      { "type": "BATCH_RENAME", "typeLabel": "批量重命名", "count": 40, "percent": 9.52 },
      { "type": "QUALITY_FILTER", "typeLabel": "质量过滤", "count": 20, "percent": 4.76 }
    ],
    "taskFrequencyRanking": [
      { "taskName": "产线图像压缩任务", "execCount": 90 },
      { "taskName": "备件库格式转换", "execCount": 60 }
    ]
  }
}
```

#### 数据趋势分析

```
GET /api/stats/ingest/analysis?granularity=day&startDate=2026-05-01&endDate=2026-05-26
Authorization: Bearer {accessToken}

Query 参数：
- granularity  粒度：day/week/month
- startDate    开始日期
- endDate      结束日期

响应：
{
  "code": 200,
  "data": {
    "labels": ["2026-05-01", "2026-05-02", "..."],
    "ingestCounts": [6000, 6200, "..."],
    "ingestSizeBytes": [12884901888, 13421772800, "..."]
  }
}
```

#### 数据源贡献占比

```
GET /api/stats/datasource/contribution?startDate=2026-05-01&endDate=2026-05-26
Authorization: Bearer {accessToken}

响应：
{
  "code": 200,
  "data": [
    { "datasourceName": "产线1号相机", "ingestCount": 45000, "percent": 35.2 },
    { "datasourceName": "产线2号相机", "ingestCount": 38000, "percent": 29.8 },
    { "datasourceName": "备件库相机", "ingestCount": 28000, "percent": 21.9 },
    { "datasourceName": "其他", "ingestCount": 16700, "percent": 13.1 }
  ]
}
```

#### 文件类型分布

```
GET /api/stats/filetype/distribution?startDate=2026-05-01&endDate=2026-05-26
Authorization: Bearer {accessToken}

响应：
{
  "code": 200,
  "data": [
    { "fileType": "JPEG", "count": 85000, "percent": 66.5 },
    { "fileType": "PNG", "count": 25000, "percent": 19.6 },
    { "fileType": "RAW", "count": 12000, "percent": 9.4 },
    { "fileType": "MP4", "count": 5800, "percent": 4.5 }
  ]
}
```

---

## 七、异常流程

| 异常场景 | 触发条件 | 处理方式 | 用户提示 |
|----------|----------|----------|----------|
| 数据为空 | 所选时间范围内无数据 | 返回空数组，图表显示空状态 | 图表区域显示"暂无数据" |
| 自定义时间范围过大 | 自定义范围超过1年 | 返回400限制 | "查询时间范围不能超过1年" |
| 统计接口超时 | 大量数据聚合超过5秒 | 返回503，前端展示loading失败 | "数据加载超时，请稍后重试" |

---

## 八、权限设计

| 操作 | 权限标识 | 可见角色 |
|------|---------|---------|
| 查看数据总览看板 | stats:overview:view | 所有角色（MANAGER/DATA_ENG/ANALYST/SUPER_ADMIN） |
| 查看处理任务统计 | stats:process:view | DATA_ENG, ANALYST, SUPER_ADMIN |
| 查看数据趋势分析 | stats:analysis:view | ANALYST, DATA_ENG, SUPER_ADMIN |

---

## 九、验收标准

**AC-001 总览看板数据准确**
- Given：今日已有接入记录，存储快照已更新
- When：进入数据总览看板
- Then：今日接入量、存储使用率与实际数据一致，误差不超过5分钟的刷新延迟

**AC-002 存储告警展示**
- Given：存储使用率超过90%
- When：查看总览看板的存储使用率卡片
- Then：卡片背景或进度条以红色高亮展示，与存储管理页面告警状态一致

**AC-003 折线图时间粒度切换**
- Given：在数据趋势分析页面，默认按"日"展示
- When：切换粒度为"月"
- Then：X轴由日期改为月份，Y轴数据聚合为月度合计，图表正确重绘

**AC-004 处理类型分布饼图**
- Given：近30天内有多种处理类型的执行记录
- When：查看处理任务统计页面
- Then：饼图各扇区占比之和为100%，与各类型执行次数比例一致

**AC-005 空数据状态处理**
- Given：自定义时间范围内无接入记录
- When：查看该时间段的趋势图
- Then：图表显示"暂无数据"空状态，不报错、不显示异常图形

**AC-006 时间范围超限校验**
- Given：在趋势分析页面选择自定义时间范围
- When：设置范围超过1年（如 2025-01-01 至 2026-06-01）
- Then：提示"查询时间范围不能超过1年"，图表不触发请求

### 性能验收

| 指标 | 要求 | 测试方式 |
|------|------|----------|
| 总览看板接口响应 | P99 < 1000ms | 压测 |
| 趋势分析（日粒度，365天） | P99 < 3000ms | 压测 |
| 页面首屏加载 | < 3s | 前端监控 |
