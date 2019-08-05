package com.hyl.atcrowdfunding.utils;

import com.hyl.atcrowdfunding.model.Member;
import com.hyl.atcrowdfunding.model.Message;

/**
 * @author: hyl
 * @date: 2019/07/13
 **/
public class AjaxResult {

    private boolean success;
    private String message;

    private Page page;

    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
