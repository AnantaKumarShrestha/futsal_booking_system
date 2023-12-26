package com.intern.futsalBookingSystem.mapper;

import com.intern.futsalBookingSystem.dto.InvoiceDto;
import com.intern.futsalBookingSystem.model.InvoiceModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface InvoiceMapper {

    InvoiceMapper INSTANCE= Mappers.getMapper(InvoiceMapper.class);

    InvoiceModel invoiceDtoIntoInvoiceModel(InvoiceDto invoiceDto);

    InvoiceDto invoiceModelIntoInvoiceDto(InvoiceModel invoiceModel);

    List<InvoiceDto> invoiceModelListIntoInvoice(List<InvoiceModel> invoiceModels);

}
