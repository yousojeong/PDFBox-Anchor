package org.anchor;

import lombok.Builder;
import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class BoxContent implements ColContent {

    private final float LINE_HEIGHT = 1.5f; // 줄간격 높이
    @Builder.Default
    private Color backgroundColor = Color.WHITE;
    @Builder.Default
    private Color borderColor = Color.BLACK;
    private List<Object> lines;
    @Builder.Default
    private PDFont font = PDType1Font.HELVETICA;
    @Builder.Default
    private float fontSize = 12;
    @Builder.Default
    private float[] padding = new float[]{0, 0, 0, 0}; // 패딩 설정 ("TOP,RIGHT,BOTTOM,LEFT") 순서
    private Float fixedHeight; // 고정 높이 지정, DEFAULT: 글 높이 계산
    @Builder.Default
    private String borderSides = "TRBL"; // 테두리 설정 ("TOP,RIGHT,BOTTOM,LEFT")
    @Builder.Default
    private Alignment alignment = Alignment.LEFT;

    private float getPaddingTop() {
        return padding[0];
    }

    private float getPaddingRight() {
        return padding[1];
    }

    protected float getPaddingBottom() {
        return padding[2];
    }

    protected float getPaddingLeft() {
        return padding[3];
    }

    private static float[] processPadding(String padding) {
        if (padding == null || padding.isBlank()) {
            return new float[]{0, 0, 0, 0};
        }

        String[] parts = padding.split(",");
        try {
            if (parts.length == 1) {
                float value = Float.parseFloat(parts[0].trim());
                return new float[]{value, value, value, value};
            } else if (parts.length == 2) {
                float value1 = Float.parseFloat(parts[0].trim());
                float value2 = Float.parseFloat(parts[1].trim());
                return new float[]{value1, value2, value1, value2};
            } else if (parts.length == 4) {
                return new float[]{
                        Float.parseFloat(parts[0].trim()),
                        Float.parseFloat(parts[1].trim()),
                        Float.parseFloat(parts[2].trim()),
                        Float.parseFloat(parts[3].trim())
                };
            } else {
                throw new IllegalArgumentException("입력은 '전부 동일', '상하,좌우 동일', '각각 적용' 으로 받음" +
                        "INPUT BY ('ALL', 'TOP&BOTTOM,RIGHT&LEFT','EACH'");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ONLY NUMBER: " + padding, e);
        }
    }

    @Override
    public void render(PDPageContentStream contentStream, float x, float y, float width, float height) throws IOException {
        List<Object> wrappedLines = new ArrayList<>();
        for (Object line : lines) {
            if (line instanceof String) {
                String[] wrapped = wrapText((String) line, font, fontSize, width - getPaddingLeft() - getPaddingRight());
                for (String wrap : wrapped) {
                    wrappedLines.add(wrap);
                }
            } else if (line instanceof Line) {
                Line lineObj = (Line) line;
                String[] wrapped = wrapText(lineObj.getText(), lineObj.getFont(), lineObj.getFontSize(), width - getPaddingLeft() - getPaddingRight());
                for (String wrap : wrapped) {
                    wrappedLines.add(new Line(wrap, lineObj.getFont(), lineObj.getFontSize()));
                }
            }
        }

        float lineHeight = fontSize * LINE_HEIGHT;
        float boxHeight = fixedHeight != null ? fixedHeight : height; // 고정 값,

        // 배경색
        if (backgroundColor != null) {
            contentStream.setNonStrokingColor(backgroundColor);
            contentStream.addRect(x, y - boxHeight, width, boxHeight);
            contentStream.fill();
            contentStream.setNonStrokingColor(Color.BLACK); // 텍스트 색상 초기화
        }

        // 테두리
        if (borderColor != null) {
            contentStream.setStrokingColor(borderColor); // 중앙 정렬이라 좌우 여백 필요없음
            contentStream.setLineWidth(0.5f); // 테두리 두께 설정
            if (borderSides.contains("T")) {
                contentStream.moveTo(x, y);
                contentStream.lineTo(x + width, y);
            }
            if (borderSides.contains("R")) {
                contentStream.moveTo(x + width, y);
                contentStream.lineTo(x + width, y - boxHeight);
            }
            if (borderSides.contains("B")) {
                contentStream.moveTo(x, y - boxHeight);
                contentStream.lineTo(x + width, y - boxHeight);
            }
            if (borderSides.contains("L")) {
                contentStream.moveTo(x, y);
                contentStream.lineTo(x, y - boxHeight);
            }
            if (!borderSides.matches("[TBRL]*")) {
                throw new IllegalArgumentException("USE IN (T,R,B,L) " + borderSides);
            }
            contentStream.setNonStrokingColor(Color.BLACK); // 텍스트 색상 초기화
            contentStream.stroke();
        }

        // 텍스트 렌더링
        float textY = y - getPaddingTop() - fontSize; // 첫 시작 Y의 높이 - fontSize - (padding)
        for (Object line : wrappedLines) {
            if (fixedHeight != null && textY - lineHeight < y - boxHeight + getPaddingBottom()) {
                break; // 고정 높이를 초과하면 렌더링 중지
            }
            contentStream.beginText();
            if (line instanceof String) {
                contentStream.setFont(font, fontSize);
            } else if (line instanceof Line) {
                Line lineObj = (Line) line;
                contentStream.setFont(lineObj.getFont(), lineObj.getFontSize());
            }

            float textWidth = font.getStringWidth(line.toString()) / 1000 * fontSize;
            float textX = x;
            switch (alignment) {
                case CENTER:
                    textX = x + (width - textWidth) / 2;
                    break;
                case RIGHT:
                    textX = x + width - textWidth - getPaddingRight();
                    break;
                case LEFT:
                    textX = x + getPaddingLeft();
                    break;
                default:
                    throw new IllegalStateException("USE IN (CENTER, RIGHT, LEFT) : " + alignment);
            }

            contentStream.newLineAtOffset(textX, textY);
            contentStream.showText(line.toString().replace(" ", "\u00A0"));
            contentStream.endText();
            textY -= lineHeight; // 다음 줄로 이동
        }
    }

    @Override
    public float calculateHeight(float width) throws IOException {
        if (fixedHeight != null) {
            return fixedHeight;
        }
        List<Object> wrappedLines = new ArrayList<>();
        for (Object line : lines) {
            if (line instanceof String) {
                String[] wrapped = wrapText((String) line, font, fontSize, width - getPaddingLeft() - getPaddingRight());
                for (String wrap : wrapped) {
                    wrappedLines.add(wrap);
                }
            } else if (line instanceof Line) {
                Line lineObj = (Line) line;
                String[] wrapped = wrapText(lineObj.getText(), lineObj.getFont(), lineObj.getFontSize(), width - getPaddingLeft() - getPaddingRight());
                for (String wrap : wrapped) {
                    wrappedLines.add(new Line(wrap, lineObj.getFont(), lineObj.getFontSize()));
                }
            }
        }

        float totalHeight = 0;
        for (Object line : wrappedLines) {
            if (line instanceof String) {
                totalHeight += fontSize * LINE_HEIGHT;
            } else if (line instanceof Line) {
                Line lineObj = (Line) line;
                totalHeight += lineObj.getFontSize() * LINE_HEIGHT;
            }
        }
        return totalHeight + getPaddingTop() + getPaddingBottom();
    }

    private String[] wrapText(String text, PDFont font, float fontSize, float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        for (char c : text.toCharArray()) {
            // 줄바꿈 처리
            if (c == '\n') {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder();
                continue;
            }

            String testLine = currentLine.toString() + c;
            float textWidth = font.getStringWidth(testLine.replace(" ", "\u00A0")) / 1000 * fontSize; // 공백 처리

            if (textWidth > maxWidth) {
                lines.add(currentLine.toString().replace(" ", "\u00A0"));
                currentLine = new StringBuilder(String.valueOf(c));
            } else {
                currentLine.append(c);
            }
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString().replace(" ", "\u00A0"));
        }
        return lines.toArray(new String[0]);
    }
}