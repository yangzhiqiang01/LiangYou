package com.inhim.pj.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class HistoricalRecordEntity extends DataSupport implements Serializable {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
