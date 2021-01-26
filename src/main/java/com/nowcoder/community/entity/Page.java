package com.nowcoder.community.entity;

/**
 * 封装分页相关的信息.   （1.30课 1：00：58开始）
 */
public class Page {

    // 当前页码
    private int current = 1;
    // 显示上限
    private int limit = 10;
    // 数据总数(用于计算总页数)
    private int rows;
    // 查询路径(用于复用分页链接)
    private String path; //点击“上一页；下一页”这些，都是链接，返回给页面，方便使用

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {   //避免用户输入零或负数的情况
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始行
     *
     * @return
     */
    public int getOffset() {
        // current * limit - limit 通过当前页来算当前页的起始行
        return (current - 1) * limit;
    }

    /**
     * 获取总页数
     *
     * @return
     */
    public int getTotal() {
        // rows / limit [+1]  //显示页码范围——边界判断
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    /**
     * 获取起始页码（就是底下那个框框条，如果有10个分页并不完全展示出来，显示当前页的前后两页）
     *
     * @return
     */
    public int getFrom() {
        int from = current - 2;
        return from < 1 ? 1 : from;//如果减完发现小于1，那就定为1（最小页码为1）
    }

    /**
     * 获取结束页码
     *
     * @return
     */
    public int getTo() {
        int to = current + 2;
        int total = getTotal();
        return to > total ? total : to;//最大页码为total
    }

}
