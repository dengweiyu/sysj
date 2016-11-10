package com.li.videoapplication.data.model.entity;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.li.videoapplication.data.model.entity.Currency;

/**
 * 实体：商城（分组布局使用的实体，必须继承SectionEntity）
 */

public class CurrencySection extends SectionEntity<Currency> {

    public CurrencySection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public CurrencySection(Currency currency) {
        super(currency);
    }
}
