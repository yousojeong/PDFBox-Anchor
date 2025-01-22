# PDFBoxAnchor

**PDFBoxAnchor** is a Java library built on top of [Apache PDFBox](https://pdfbox.apache.org/) to assist in creating structured layouts in PDF documents.
This library uses the concepts of `row` and `col` to support intuitive layout configuration, enabling the addition of various content types (e.g., Text, Box) for rendering in PDFs.

---

## Features

### 1. Row and Column Structure
- **Row**: Represents a single line in the PDF layout. You can configure the following properties during creation:
  - `x`, `y`: The starting position of the Row.
  - `row_width`: The total width of the Row.
  - `useMaxHeight`: If set to `true`, the height of the Row will be determined by the tallest Col within the Row.
  - **Method to Add Row**:
    ```java
    Row row = new Row(x, y, row_width, useMaxHeight);
    ```
    - **`x`**: The starting x-coordinate of the Row.
    - **`y`**: The starting y-coordinate of the Row.
    - **`row_width`**: The total width of the Row.
    - **`useMaxHeight`**: If set to `true`, the height of the Row will be determined by the tallest Col within the Row.


- **Col**: Represents a cell within a Row. You can adjust its size using the `span` property.
  - **Col Width Calculation**:  
    `col_width = row_width / 12 * col_span`
  - **Method to Add Col**:
    ```java
    row.addCol(span, content);
    ```
    - **`span`**: A value between 1 and 12 to specify the proportional size of the Col.
    - **`content`**: Supports various content types, such as `TextContent` and `BoxContent`.

---

### 2. Relative Row Placement
- You can position new Rows relative to the previous Row.
- Use the **`getRowNextY()`** method to place a new Row directly below the previous one.

  #### Example:
  ```java
  Row r1 = new Row(0, 600, PDRectangle.A4.getWidth());
  Row r2 = new Row(0, r1.getRowNextY(), PDRectangle.A4.getWidth(), true);
  ```

---

### 3. **Column Content Types**
- **TextContent**: Adds text-based content to a Col.
    - Configurable properties include: `text`, `font`, `fontSize`, `alignment`
    #### Example:
    ```java
    TextContent textContent = TextContent.builder()
        .text("Sample Text")
        .font(PDType1Font.HELVETICA_BOLD)
        .fontSize(12)
        .build();
    row.addCol(12, textContent);
    ```

- **BoxContent**: Adds a box-shaped content, suitable for tables or styled sections.
    - Configurable properties include: `lines`, `backgroundColor`, `borderColor`, `font`, `fontSize`, `padding`, `borderSides`, `alignment`
      - `lines`: A list of text lines. 
      - `backgroundColor`: Background color of the box. 
      - `borderColor`: Border color of the box. 
      - `font`: Font for text inside the box. 
      - `fontSize`: Font size for text. 
      - `padding`: Padding inside the box. 
      - `borderSides`: Sides of the box where borders are displayed (e.g., "TBLR" - Top, Bottom, Left, Right). 
      - `alignment`: Text alignment within the box.
  - Automatically wraps text based on Col width and calculates height.
  - Fixed Height: You can specify a fixed height using the fixedHeight property.

  #### Example:
  ```java
  List<Line> lines = new ArrayList<>();
  lines.add(new Line("First Line", PDType1Font.HELVETICA, 10));
  lines.add(new Line("Second Line", PDType1Font.HELVETICA, 10));

  BoxContent boxContent = BoxContent.builder()
    .lines(lines)
    .fontSize(10)
    .padding(new float[]{5, 5, 5, 5})
    .borderSides("TBLR") // Top, Bottom, Left, Right
    .backgroundColor(Color.LIGHT_GRAY)
    .build();

  row.addCol(6, boxContent);
  ````

## INSTALL

### Maven
Add the following dependency to your `pom.xml`:
```xml
<dependency>
    <groupId>io.github.yousojeong</groupId>
    <artifactId>PDFBoxAnchor</artifactId>
    <version>1.0.1</version>
</dependency>
```

### Gradle
Add the following dependency to your `build.gradle`:
```gradle
dependencies {
    implementation 'io.github.yousojeong:PDFBoxAnchor:1.0.1'
}
```

## Example
```java
public class Main {

    public static void main(String[] args) throws Exception {

        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        doc.addPage(page);
        try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
            Row r1 = new Row(0, 600, PDRectangle.A4.getWidth());
            TextContent textContent = TextContent.builder()
                    .text("TEST")
                    .font(PDType1Font.HELVETICA_BOLD)
                    .fontSize(20)
                    .build();
            r1.addCol(12, textContent); // span: 12 == PDRectangle.A4.getWidth() / 12 * 12
            r1.render(contentStream);

            Row r2 = new Row(0, r1.getRowNextY(), PDRectangle.A4.getWidth(), true);
            List<Object> lines = new ArrayList<>();
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

## Dependencies
- Apache PDFBox: 2.0.24

## Contact
- Email: hielosan6@gmail.com
- Github: https://github.com/yousojeong
