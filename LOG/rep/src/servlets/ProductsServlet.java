package servlets;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import com.logistics.lib.dao.ProductDAO2;

import com.logistics.lib.dto.Product;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.io.OutputStream;

import java.sql.SQLException;

import java.util.List;

import javax.annotation.Resource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import utils.ReportHelper;


@WebServlet("/api/products")
public class ProductsServlet extends HttpServlet {
    @Resource(name="jdbc/LOGConnDS")
    private DataSource dataSource;
    private ProductDAO2 productDAO;


    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO2(dataSource);
    }


    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        try {
                    List<Product> products = productDAO.list();
//                    request.setAttribute("products", products); // Will be available as ${products} in JSP
//                    request.getRequestDispatcher("/WEB-INF/products.jsp").forward(request, response);
            
                    try {
                    String text = request.getParameter("text");
                    if (text == null || text.trim().length() == 0) {
                    text = "You didn't enter any text.";
                    }
                     ByteArrayOutputStream baos = test1(text, request);
                    response.setHeader("Expires", "0");
                    response.setHeader("Cache-Control",
                    "must-revalidate, post-check=0, pre-check=0");
                    response.setHeader("Pragma", "public");
                    response.setContentType("application/pdf");
                    response.setContentLength(baos.size());
                    OutputStream os = response.getOutputStream();
                    baos.writeTo(os);
                    os.flush();
                    os.close();
                    }
                    catch(DocumentException e) {
                    throw new IOException(e.getMessage());
                    }
            
                } catch (SQLException e) {
                    throw new ServletException("Cannot obtain products from DB", e);
                }
    }
    
    public Paragraph pageHeader(){
        Chunk glue = new Chunk(new VerticalPositionMark());
        Paragraph p = new Paragraph("Text to the left");
        p.add(new Chunk(glue));
        p.add("Text to the Center");
        
        p.add(new Chunk(glue));
        p.add("Text to the right");
        return p;
    }
    
    public ByteArrayOutputStream  test1(String text, HttpServletRequest request) throws DocumentException {
        Document document = new Document();
        
        ByteArrayOutputStream baos
        = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos); 
        document.open();
       
        
        
        document.add(pageHeader());        
        //HeaderFooter header;
        
//        document.add(new Paragraph(String.format("You have submitted the following text using the %s method:",
//        request.getMethod())));
//        
//        
//        
//        
//        document.add(new Paragraph("Test Paragraph !!"));        
//        document.add(new Paragraph(text));   
//        
//        PdfPTable table = getTable1();
//        
//        table.setHorizontalAlignment(Element.ALIGN_LEFT);
//        
//        table.setTotalWidth(new float[]{ 144, 72, 72 });
//        table.setLockedWidth(true);
//        
//        
//        
//        table.setSpacingBefore(5);
//        table.setSpacingAfter(5);
//        Paragraph p2 = new Paragraph();
//        p2.setAlignment(500);
//        p2.add(table);
//        document.add(p2);
        document.close();
        
        return baos;
    }

    private PdfPTable getTable1() {
        PdfPTable table = new PdfPTable(3);
        for(int i=0; i<=700; i++){
        PdfPCell cHeader1 = new PdfPCell(new Phrase("abc1"));
        PdfPCell cHeader2 = new PdfPCell(new Phrase("abc2"));
        PdfPCell cHeader3 = new PdfPCell(new Phrase("abc3"));
        
        table.addCell(cHeader1);
            table.addCell(cHeader2);
            table.addCell(cHeader3);
            
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("Cell with colspan 3"));
        cell.setColspan(3);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
        cell.setRowspan(2);
        table.addCell(cell);
        table.addCell("row 1; cell 1");
        table.addCell("row 1; cell 2");
        table.addCell("row 2; cell 1");
        table.addCell("row 2; cell 2");
        }
        return table;
    }
    
    private PdfPTable header() throws DocumentException {
        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        //Paragraph leftP = new Paragraph();
        
//        Phrase phraseLeft = new Phrase("Left P");
//        Phrase phraseRight = new Phrase("Right P");
//        Phrase phraseCenter = new Phrase("Center P");
        
//
//        PdfPCell cellLeft = new PdfPCell(phraseLeft);
//        PdfPCell cellCenter = new PdfPCell(phraseCenter);
//        PdfPCell cellRight = new PdfPCell(phraseRight);
        
//        formatHeaderCell(cellLeft);
//        formatHeaderCell(cellRight);
//        formatHeaderCell(cellCenter);
       
        
        header.addCell(getCell("text right", PdfPCell.ALIGN_RIGHT));
        header.addCell(getCell("text left", PdfPCell.ALIGN_LEFT));
        header.addCell(getCell("text center", PdfPCell.ALIGN_CENTER));
        
        
        
        
////        header.setWidths(new int[]{2, 24});
////        header.setTotalWidth(527);
////        header.setLockedWidth(true);
//        header.getDefaultCell().setFixedHeight(40);
//        header.getDefaultCell().setBorder(Rectangle.BOTTOM);
//        header.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
        
        return header;
    }
    
    private void formatHeaderCell(PdfPCell cell){
        cell.setPaddingBottom(15);
        cell.setPaddingLeft(10);
        cell.setBorder(Rectangle.BOTTOM);
         
    }
    
    
    public PdfPCell getCell(String text , int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(cell.NO_BORDER); // cell.setBorderColor(BaseColor.LIGHT_GRAY);  
        return cell;
    }
    
}
