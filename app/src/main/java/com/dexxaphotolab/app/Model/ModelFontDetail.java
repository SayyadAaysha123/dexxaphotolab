package com.dexxaphotolab.app.Model;


public class ModelFontDetail {

    String text;
    String FontName;

    public ModelFontDetail(String text, String fontName) {
        this.text = text;
        FontName = fontName;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFontName(String fontName) {
        FontName = fontName;
    }

    public String getFontName() {
        return FontName;
    }

    public String getText() {
        return text;
    }
}
