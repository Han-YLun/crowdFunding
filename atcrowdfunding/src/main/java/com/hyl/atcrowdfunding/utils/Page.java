package com.hyl.atcrowdfunding.utils;

import java.util.List;

/**
 * @author: hyl
 * @date: 2019/07/14
 **/
public class Page<T> {

    private Integer pageno;   //页数
    private Integer pagesize; //每页条数
    private List<T> data;    //数据
    private Integer totalsize; //总共多少条
    private Integer totalno;    //总共多少页


    public Page(Integer pageno, Integer pagesize) {

        if(pageno <= 0){
           this.pageno = 1;
        }else{
           this.pageno = pageno;
        }

        if(pagesize <= 0){
            this.pagesize = 10;
        }else{
            this.pagesize = pagesize;
        }
    }



    public Integer getStartIndex(){
        return (this.pageno-1) * pagesize;
    }

    public Integer getPageno() {
        return pageno;
    }

    public void setPageno(Integer pageno) {
        this.pageno = pageno;
    }

    public Integer getPagesize() {
        return pagesize;
    }

    public void setPagesize(Integer pagesize) {
        this.pagesize = pagesize;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Integer getTotalsize() {
        return totalsize;
    }

    public void setTotalsize(Integer totalsize) {
        this.totalsize = totalsize;
        this.totalno = totalsize % pagesize != 0 ? (totalsize / pagesize)+1 : (totalsize / pagesize);
    }

    public Integer getTotalno() {
        return totalno;
    }

    public void setTotalno(Integer totalno) {
        this.totalno = totalno;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageno=" + pageno +
                ", pagesize=" + pagesize +
                ", data=" + data +
                ", totalsize=" + totalsize +
                ", totalno=" + totalno +
                '}';
    }
}
