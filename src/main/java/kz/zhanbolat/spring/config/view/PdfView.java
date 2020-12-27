package kz.zhanbolat.spring.config.view;

import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import kz.zhanbolat.spring.entity.Ticket;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class PdfView extends AbstractPdfView {

    @Override
    @SuppressWarnings("unchecked")
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer pageSize = (Integer) model.get("pageSize");
        Integer pageNum = (Integer) model.get("pageNum");
        document.setPageSize(new Rectangle(pageSize, pageSize));
        document.setPageCount(pageNum);

        final PdfPTable table = new PdfPTable(4);

        table.addCell("Ticket id");
        table.addCell("User id");
        table.addCell("Event id");
        table.addCell("Booked");

        List<Ticket> tickets = (List<Ticket>) model.get("tickets");
        tickets.forEach(ticket -> {
            table.addCell(String.valueOf(ticket.getId()));
            table.addCell(ticket.getUserId() == 0 ? "Null" : String.valueOf(ticket.getUserId()));
            table.addCell(String.valueOf(ticket.getEventId()));
            table.addCell(ticket.isBooked() ? "Yes" : "No");
        });
        document.add(table);
    }
}
