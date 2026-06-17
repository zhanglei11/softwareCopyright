package com.vqcc.common.result;

import java.io.Serializable;
import java.util.List;

public class TableDataInfo<T> implements Serializable {

    private int code = 200;
    private String msg = "查询成功";
    private long total;
    private List<T> rows;

    public TableDataInfo() {}

    public TableDataInfo(List<T> rows, long total) {
        this.rows = rows;
        this.total = total;
    }

    /**
     * 直接返回 list，total 由 PageHelper 的 Page 代理填充（若未分页则等于 list.size()）
     */
    @SuppressWarnings("unchecked")
    public static <T> TableDataInfo<T> ok(List<T> list) {
        long total = list.size();
        // 若是 PageHelper 的 Page 对象，则取真实 total
        try {
            Class<?> pageClass = Class.forName("com.github.pagehelper.Page");
            if (pageClass.isInstance(list)) {
                total = ((Number) pageClass.getMethod("getTotal").invoke(list)).longValue();
            }
        } catch (Exception ignored) {}
        return new TableDataInfo<>(list, total);
    }

    public int getCode() { return code; }
    public String getMsg() { return msg; }
    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    public List<T> getRows() { return rows; }
    public void setRows(List<T> rows) { this.rows = rows; }
}
