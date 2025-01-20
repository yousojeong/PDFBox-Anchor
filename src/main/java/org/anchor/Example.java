package org.anchor;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class Example {
    public static void main(String[] args) throws Exception {
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
            Row row = new Row(0, 600, 500);
            TextContent textContent = TextContent.builder()
                    .text("Hello, PDFBoxAnchor!")
                    .font(PDType1Font.HELVETICA_BOLD)
                    .fontSize(20)
                    .build();
            row.addCol(12, textContent);
            row.render(contentStream);
        }

        doc.save("src/main/resources/example.pdf");
        doc.close();
    }
}
