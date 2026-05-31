# 产线智能视觉缺陷分类与追溯管理系统 · 统计报表模块 PRD

> 所属系统：产线智能视觉缺陷分类与追溯管理系统
> 文档版本：V1.0 | 创建日期：2026-05-26

---

## 一、模块概述

统计报表模块提供实时缺陷统计看板、多维度趋势分析与报表导出功能，为管理层和质量工程师提供质量动态的可视化视图。

---

## 二、功能清单

| 功能点 | 优先级 | 说明 | 涉及角色 |
|--------|--------|------|----------|
| 缺陷统计看板 | P0 | 实时核心指标卡片+图表 | 全角色 |
| 合格率趋势分析 | P1 | 按日/周/月多维度趋势 | 质量工程师、管理人员 |
| 产线缺陷对比 | P1 | 各产线缺陷数量横向对比 | 质量工程师、管理人员 |
| 报表导出 | P1 | 导出 Excel 明细 / PDF 报告 | 质量工程师、管理人员 |

---

## 三、缺陷统计看板

### 3.1 指标卡片

| 指标卡片 | 计算口径 | 刷新周期 |
|---------|---------|---------|
| 今日检测总量 | 当日检测产品件数 | 实时 |
| 今日合格率 | 当日合格品 / 总检测数 | 实时 |
| 今日缺陷总数 | 当日缺陷记录条数 | 实时 |
| 本月合格率趋势 | 近30天按日折线图 | 每小时刷新 |
| 缺陷类型分布 | 近30天各类缺陷占比饼图 | 每小时刷新 |
| 各产线缺陷对比 | 各产线当日缺陷数量柱状图 | 实时 |

### 3.2 接口

```
GET /api/v1/stats/dashboard?date=2026-05-26
Authorization: Bearer {token}

Response:
{
  "code": 200,
  "data": {
    "todayTotal": 3500,
    "todayQualifiedRate": 0.978,
    "todayDefectCount": 77,
    "monthTrend": [
      { "date": "2026-05-01", "qualifiedRate": 0.982 },
      { "date": "2026-05-02", "qualifiedRate": 0.975 }
    ],
    "categoryDistribution": [
      { "name": "划痕", "value": 35 },
      { "name": "凹坑", "value": 22 }
    ],
    "lineComparison": [
      { "lineName": "产线A", "defectCount": 32 },
      { "lineName": "产线B", "defectCount": 28 }
    ]
  }
}
```

---

## 四、趋势分析

### 4.1 接口

```
GET /api/v1/stats/trend?startDate=2026-05-01&endDate=2026-05-26
  &granularity=day&lineId=&categoryId=
Authorization: Bearer {token}

Query 参数：
- granularity  string  day/week/month，默认 day
- lineId       long    产线筛选（可选）
- categoryId   long    缺陷类型筛选（可选）

Response:
{
  "code": 200,
  "data": {
    "summary": {
      "totalCount": 52300,
      "avgQualifiedRate": 97.8
    },
    "series": [
      { "date": "2026-05-01", "totalCount": 1800, "qualifiedRate": 97.5 }
    ]
  }
}
```

---

## 五、报表导出

| 报表类型 | 触发方式 | 格式 | 说明 |
|---------|---------|------|------|
| 缺陷明细报表 | 缺陷记录列表页导出 | Excel | 含所有筛选条件内的全部字段 |
| 追溯报告 | 批次/产品追溯页导出 | Excel | 含追溯概览+明细 |
| 质量分析报告 | 统计报表页导出 | PDF | 含看板图表截图+数据汇总 |

---

## 六、验收标准

**AC-001 统计看板加载**
- Given：系统有今日检测数据
- When：进入首页统计看板
- Then：4 个指标卡片正确展示，今日合格率折线图正常渲染，加载时间不超过 3 秒

**AC-002 Excel 导出**
- Given：查询结果为 500 条缺陷记录
- When：点击导出 Excel
- Then：1分钟内下载包含所有500条记录的 Excel 文件，字段与页面列表一致
