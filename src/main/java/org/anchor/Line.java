package org.anchor;

import lombok.Getter;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

@Getter
public class Line {
    private String text;
    private PDFont font;
    private float fontSize;

    public Line(String text, float fontSize) {
        this(text, PDType1Font.HELVETICA, fontSize);
    }

    public Line(String text, PDFont font, float fontSize) {
        this.text = text;
        this.font = font != null ? font : PDType1Font.HELVETICA;
        this.fontSize = fontSize;
    }

    @Override
    public String toString() {
        return text;
    }
}