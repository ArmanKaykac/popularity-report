package com.popularity.report.service;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.popularity.report.model.ImageData;
import com.popularity.report.model.PDFModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class PDFExporter {

    List<PDFModel> images;

    public PDFExporter(List<PDFModel> images) {
        this.images = images;
    }


    private void writeTableHeader(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Phrase("Image ID"));

        table.addCell(cell);

        cell.setPhrase(new Phrase("Name"));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Count"));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table){
        for (PDFModel image: images){
            table.addCell(String.valueOf(image.getImageData().getId()));
            table.addCell(image.getImageData().getName());
            table.addCell(String.valueOf(image.getCount()));
        }
    }

    public void export(HttpServletResponse response){
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        document.open();

        document.add(new Paragraph("List"));

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);

        writeTableHeader(table);
        writeTableData(table);


        document.add(table);
        document.close();
    }
}
