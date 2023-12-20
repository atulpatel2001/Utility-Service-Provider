package com.exe.online.services;

import com.exe.online.model.AllocateBooking;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    private Logger logger = LoggerFactory.getLogger(PdfService.class);

    public ByteArrayInputStream createPdf(AllocateBooking allocateBooking) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25);
            Paragraph title = new Paragraph("Invoice", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Add customer information
            Font customerFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph customerInfo = new Paragraph("Customer Information:\n\n", customerFont);
            customerInfo.add("Name: " + allocateBooking.getBookingDetail().getBooking_person() + "\n");
            customerInfo.add("Address: " + allocateBooking.getBookingDetail().getBooking_address() + "\n");
            customerInfo.add("City: " + allocateBooking.getBookingDetail().getBooking_city() + "\n");
            customerInfo.add("Email: " + allocateBooking.getBookingDetail().getBooking_email() + "\n");
            customerInfo.add("Phone: " + allocateBooking.getBookingDetail().getBooking_phone() + "\n\n");
            document.add(customerInfo);
            HeaderFooter footer = new HeaderFooter(true, new Phrase("Utility Service Provider"));
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setBorderWidthBottom(1);
            document.setFooter(footer);
            // Add order details
            Font orderFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph orderDetails = new Paragraph("Order Details:\n\n", orderFont);
            orderDetails.add("Order Number : " + allocateBooking.getBookingDetail().getService_request_number() + " \n\n");
            orderDetails.add("Order Date : " + allocateBooking.getBookingDetail().getBooking_date() + "\n");
            orderDetails.add("Payment Id : " + allocateBooking.getBookingDetail().getTranscation().getPayment_Id() + "\n");
            orderDetails.add("Transcation Status : " + allocateBooking.getBookingDetail().getTranscation().getTranscation_status() + "\n");
            document.add(orderDetails);

            // Add itemized list
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            PdfPCell cell1 = new PdfPCell(new Phrase("Item"));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);

            PdfPCell cell2 = new PdfPCell(new Phrase("Description"));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell2);

            PdfPCell cell3 = new PdfPCell(new Phrase("Quantity"));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell3);

            PdfPCell cell4 = new PdfPCell(new Phrase("Price"));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell4);

            // Add sample items
            addItemToTable(table, allocateBooking.getBookingDetail().getService().getService_name(), allocateBooking.getBookingDetail().getTranscation().getReceipt(), 1, allocateBooking.getBookingDetail().getService().getService_amount());

            document.add(table);

            // Add total amount
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Paragraph total = new Paragraph("Total: $" + allocateBooking.getBookingDetail().getService().getService_amount(), totalFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();
            writer.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private static void addItemToTable(PdfPTable table, String item, String description, int quantity, double price) {
        table.addCell(item);
        table.addCell(description);
        table.addCell(String.valueOf(quantity));
        table.addCell(String.valueOf(price));
    }


}
