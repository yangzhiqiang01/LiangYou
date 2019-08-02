package com.inhim.pj.entity;


import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class HistoricalRecordEntity extends LitePalSupport implements Serializable {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
