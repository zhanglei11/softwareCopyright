# 产线智能视觉缺陷分类与追溯管理系统 · 追溯管理模块 PRD

> 所属系统：产线智能视觉缺陷分类与追溯管理系统
> 文档版本：V1.0 | 创建日期：2026-05-26

---

## 一、模块概述

追溯管理模块提供两种追溯维度：按生产批次追溯（了解整批次质量状况）和按产品序列号追溯（了解单件产品完整检测历史），是质量事故分析与根因定位的核心工具。

---

## 二、功能清单

| 功能点 | 优先级 | 说明 | 涉及角色 |
|--------|--------|------|----------|
| 批次追溯查询 | P0 | 按批次号查询质量全貌 | 质量工程师、管理人员 |
| 批次缺陷分布展示 | P0 | 饼图展示各类缺陷占比 | 质量工程师、管理人员 |
| 产品追溯查询 | P0 | 按序列号查询单品检测历史 | 质量工程师 |
| 追溯结果导出 | P1 | 导出追溯报告 Excel | 质量工程师、管理人员 |

---

## 三、批次追溯

### 3.1 操作步骤

1. 进入【追溯管理 - 批次追溯】
2. 输入批次号（支持模糊匹配）或选择生产日期范围 + 产线
3. 系统返回批次概览卡片：总产量、合格率、各类缺陷数量
4. 下方展示该批次明细记录列表，可下钻至单品追溯

### 3.2 接口

```
GET /api/v1/trace/batch?batchNo=BATCH-2026052601&lineId=1
Authorization: Bearer {token}

Response:
{
  "code": 200,
  "data": {
    "batchNo": "BATCH-2026052601",
    "lineName": "产线A",
    "totalCount": 1200,
    "qualifiedCount": 1155,
    "defectCount": 45,
    "qualifiedRate": 0.9625,
    "categoryDistribution": [
      { "categoryName": "划痕", "count": 20, "ratio": 0.444 },
      { "categoryName": "凹坑", "count": 15, "ratio": 0.333 },
      { "categoryName": "色差", "count": 10, "ratio": 0.222 }
    ],
    "records": [ "分页列表..." ]
  }
}
```

---

## 四、产品追溯

### 4.1 操作步骤

1. 进入【追溯管理 - 产品追溯】
2. 输入产品序列号（精确匹配）
3. 系统返回该产品所有检测节点的历史记录

### 4.2 接口

```
GET /api/v1/trace/product?serialNo=SN20260526001
Authorization: Bearer {token}

Response:
{
  "code": 200,
  "data": {
    "serialNo": "SN20260526001",
    "typeName": "MODEL-X1",
    "batchNo": "BATCH-2026052601",
    "lineName": "产线A",
    "detectRecords": [
      {
        "id": 1001,
        "detectTime": "2026-05-26 08:32:15",
        "result": 0,
        "resultLabel": "不合格",
        "categoryName": "划痕",
        "level": 2,
        "disposeStatus": 2,
        "disposeRemark": "已返工打磨"
      }
    ],
    "finalConclusion": "不合格品，已返工处置"
  }
}
```

---

## 五、权限设计

| 操作 | 权限标识 | 可见角色 |
|------|---------|----------|
| 批次追溯查询 | trace:query | 质量工程师、管理人员 |
| 产品追溯查询 | trace:query | 质量工程师 |

---

## 六、验收标准

**AC-001 批次追溯**
- Given：批次 BATCH-001 有 500 条检测记录
- When：在批次追溯页输入批次号 BATCH-001，点击查询
- Then：返回批次概览（总产量500、合格率、缺陷分布饼图）及明细列表，响应时间不超过 3 秒

**AC-002 产品追溯**
- Given：产品序列号 SN001 有 3 次检测记录
- When：输入序列号 SN001 查询
- Then：按时间顺序展示 3 条检测记录，含缺陷详情与处置结果
