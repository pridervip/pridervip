/**
 * Huobi.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.pridervip.entity;

public class Page implements java.io.Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 9175471403428757024L;
    /** total pages for the query 总共有多少页 */
    private int tp;
    /** page size */
    private int ps=10;
    
    /** current page of the query 当前页码*/
    private int p;
    
    /** total number of records for the query  满足条件的元素总数*/
    private int records;
    
    /** an array that contains the actual data  当前页对应的元素*/
//    private List<T> rows;
    
    public Page( Integer records,Integer pageSize, Integer currentPage ){
        this.records = records;//记录总数
        this.ps=pageSize;//页面大小
        this.tp = (Integer)(int) Math.ceil((double) records / pageSize);//总页数
        if( currentPage<=0){
            this.p = 1;
        } else {
            this.p = currentPage;//当前页
        }
        
    }
    
    /**
     * 
     */
    public Page() {
        // TODO Auto-generated constructor stub
    }

    public int getTp() {
        return tp;
    }

    public void setTp(int tp) {
        this.tp = tp;
    }

    public int getPs() {
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    public int getPn() {
        return p;
    }

    public void setPn(int cp) {
        this.p = cp;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

}
