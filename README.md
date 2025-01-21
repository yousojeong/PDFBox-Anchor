# PDFBoxAnchor

## Use
- 

## INSTALL

### Maven
Add the following dependency to your `pom.xml`:
```xml
<dependency>
    <groupId>io.github.yousojeong</groupId>
    <artifactId>PDFBoxAnchor</artifactId>
    <version>1.0.0</version>
</dependency>
```


## Example
```java
public class Main {

    public static void main(String[] args) throws Exception {

        List<Object> lines = new ArrayList<>();

        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        doc.addPage(page);
        try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
            // CONTENT
            Row r1 = new Row(0, 600, PDRectangle.A4.getWidth());
            TextContent textContent = TextContent.builder()
                    .text("TEST")
                    .font(PDType1Font.HELVETICA_BOLD)
                    .fontSize(20)
                    .build();
            r1.addCol(12, textContent);
            r1.render(contentStream);

            Row r2 = new Row(0, r1.getRowNextY(), PDRectangle.A4.getWidth(), true);
            lines.add(new Line("test1", PDType1Font.HELVETICA_BOLD, 10));
            lines.add(new Line("test2", PDType1Font.HELVETICA_BOLD, 10));
            BoxContent boxContent = BoxContent.builder()
                    .lines(lines)
                    .fontSize(8)
                    .padding(new float[5])
                    .borderSides("TBL")
                    .build();
            r2.addCol(12, boxContent);
            r2.render(contentStream);

        }
        doc.save("src/main/resources/example.pdf");
        doc.close();
    }
}
```