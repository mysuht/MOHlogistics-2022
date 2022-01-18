package utils;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ReportHelper extends PdfPageEventHelper {
    Phrase[] header = new Phrase[2];
    int pagenumber;
    
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        // TODO Implement this method
       

        Rectangle rect = writer.getBoxSize("art");
        switch (writer.getPageNumber() % 2) {
        case 0:
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase("even header"),
                                       rect.getRight(), rect.getTop(), 0);
            break;
        case 1:
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("odd header"),
                                       rect.getLeft(), rect.getTop(), 0);
            break;
        }
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
                                   new Phrase(String.format("page %d", writer.getPageNumber())),
                                   (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
        
        super.onEndPage(writer, document);
    }


    @Override
    public void onStartPage(PdfWriter pdfWriter, Document document) {
        System.out.println("onStartPage() method > Writing header in file");
        Rectangle rect = pdfWriter.getBoxSize("Rectangle");
        
        // Top Left
        
        pagenumber ++;
        //        document.addHeader("", "");
        //        super.onStartPage(pdfWriter, document);
        //        InputStream is = new ByteArrayInputStream(header?.getBytes());
        //        XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);

    }
    
    public ColumnText getHeaderBox(Rectangle rect, PdfWriter pdfWriter, String headerBox){
       
        ColumnText colText = new ColumnText(pdfWriter.getDirectContent());
        if(headerBox.equals("Center")){
            colText.showTextAligned(pdfWriter.getDirectContent(),
                        Element.ALIGN_CENTER, new Phrase("TOP Medium"), rect.getRight() / 2,
                        rect.getTop(), 0);
        }else         if(headerBox.equals("Left")){
            colText.showTextAligned(pdfWriter.getDirectContent(),
                        Element.ALIGN_CENTER, new Phrase("TOP LEFT"), rect.getLeft(),
                        rect.getTop(), 0);
        }
        colText.addText(new Phrase(""));
        colText.addText(Chunk.NEWLINE);
        return colText;
    }


    private PdfPTable header() throws DocumentException {
        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.addCell(getCell("text right", PdfPCell.ALIGN_RIGHT));
        header.addCell(getCell("text left", PdfPCell.ALIGN_LEFT));
        header.addCell(getCell("text center", PdfPCell.ALIGN_CENTER));
        return header;
    }

    public PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(cell.NO_BORDER); // cell.setBorderColor(BaseColor.LIGHT_GRAY);
        return cell;
    }
}
