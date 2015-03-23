package com.example.xiaofang.util;

import java.io.Serializable;

public class MyDict implements Serializable {
	private Integer id;
    private String text;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Override
    public String toString() {
        return text;
    }
	public MyDict(Integer id, String text) {
		super();
		this.id = id;
		this.text = text;
	}
	public MyDict() {
		super();
	}

}
