package utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class ReportExecuter {
    public ReportExecuter() {
        super();
    }
    public PdfTemplate createTemplate(PdfContentByte content, Rectangle rect,
    int factor) throws IOException {
    PdfTemplate template = content.createTemplate(
    rect.getWidth(), rect.getHeight());
    template.concatCTM(factor, 0, 0, factor, 0, 0);
        File RESOURCE = new File("");
        FileReader reader = new FileReader(RESOURCE);
    int c;
    while ((c = reader.read()) > -1) {
    template.setLiteral((char)c);
    }
    return template;
    }
    public static void main(String[] args) {
//       float w = PageSize.A4.getWidth();
//       float h = PageSize.A4.getHeight();
//       Rectangle rect = new Rectangle(-2*w, -2*h, 2*w, 2*h);
//       Rectangle crop = new Rectangle(-2*w, h, -w, 2*h);
//       Document document = new Document(rect);
//       PdfWriter writer;
//        try {
////            writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
////            writer.setCropBoxSize(crop);
//       
//        
//       document.open();
//       PdfContentByte content = writer.getDirectContent();
//       PdfTemplate template = createTemplate(content, rect, 4);
//       float adjust;
//       while(true) {
//       content.addTemplate(template, -2*w, -2*h);
//       adjust = crop.getRight() + w;
//       if (adjust > 2 * w) {
//       adjust = crop.getBottom() - h;
//       if (adjust < - 2 * h)
//       break;
//       crop = new Rectangle(
//       -2*w, adjust, -w, crop.getBottom());
//       }
//       else {
//       crop = new Rectangle(
//       crop.getRight(), crop.getBottom(),
//       adjust, crop.getTop());
//       }
//       writer.setCropBoxSize(crop);
//       document.newPage();
//       }
//       document.close();
//            
//            
//       } catch (DocumentException e) {
//           e.printStackTrace();
//       }
   }
}
