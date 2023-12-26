package com.intern.futsalBookingSystem.db;

import com.intern.futsalBookingSystem.model.InvoiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepo extends JpaRepository<InvoiceModel,Integer> {
}
