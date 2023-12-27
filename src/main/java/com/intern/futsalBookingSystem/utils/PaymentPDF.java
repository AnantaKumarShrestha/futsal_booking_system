package com.intern.futsalBookingSystem.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

@Component
public class PaymentPDF {

   public byte[] asByteInvoice(String FutsalName,String location ,String InvoiceNumber,String customerName,String slotId,String startTime,String endTime,String price) throws IOException {

        PDDocument docs = new PDDocument();

        if (docs.getNumberOfPages() == 0) {
            PDPage newpage = new PDPage();
            docs.addPage(newpage);
        }

        LocalDate localDate=LocalDate.now();

        PDPage mypage = docs.getPage(0);

        //Prepare Content Stream
        PDPageContentStream cs = new PDPageContentStream(docs, mypage);

        //Writing Single Line text
        //Writing the Invoice title
        cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 22);
        cs.newLineAtOffset(250, 735);
        cs.showText(FutsalName);
        cs.endText();

        cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 10);
        cs.newLineAtOffset(500, 715);
        cs.showText(String.valueOf(localDate));
        cs.endText();

        cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 10);
        cs.newLineAtOffset(270, 705);
        cs.showText("Location : Baluwatar");
        cs.endText();

        cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 18);
        cs.newLineAtOffset(270, 675);
        cs.showText("INVOICE");
        cs.endText();

        cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 10);
        cs.newLineAtOffset(285, 655);
        cs.showText("NO : "+InvoiceNumber);
        cs.endText();

        //Writing Multiple Lines
        //writing the customer details
        cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 14);
        cs.setLeading(20f);
        cs.newLineAtOffset(60, 595);
        cs.showText("Customer Name: ");
        cs.endText();


        cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 14);
        cs.setLeading(20f);
        cs.newLineAtOffset(170, 595);
        cs.showText(customerName);
        cs.endText();

        //========================================================================================

        cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 14);
        cs.newLineAtOffset(80, 530);
        cs.showText("Customer Name");
        cs.endText();


        cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 14);
        cs.newLineAtOffset(410, 530);
        cs.showText("Price");
        cs.endText();

        cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 14);
        cs.newLineAtOffset(510, 530);
        cs.showText("Status");
        cs.endText();

        cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 12);
        cs.setLeading(20f);
        cs.newLineAtOffset(80, 530);
        cs.endText();


        //=========================================================================================

        cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 14);
        cs.newLineAtOffset(85, 510);
        cs.showText(customerName);
        cs.endText();

        cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 14);
        cs.newLineAtOffset(415, 510);
        cs.showText(price);
        cs.endText();

        cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 14);
        cs.newLineAtOffset(515, 510);
        cs.showText("Paid");
        cs.endText();


        cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 12);
        cs.newLineAtOffset(275, 100);
        cs.showText("Thank You");
        cs.endText();

        cs.close();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        docs.save(baos);
        return baos.toByteArray();

    }

}
