package com.li.videoapplication.framework;

/**
 * 基本控制器
 */
public abstract class BaseController {

    protected final String action = this.getClass().getName();

    protected final String tag = this.getClass().getSimpleName();
}
