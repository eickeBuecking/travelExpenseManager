package de.eicke.receipts.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TravelExpense {
	@Id
	private String id;
	
	private String travelId;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
	private Date travelDate;

	private List<Receipt> receipts;
	
	private TravelExpenseStatus status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTravelId() {
		return travelId;
	}

	@Required
	public void setTravelId(String travelId) {
		this.travelId = travelId;
	}

	public Date getTravelDate() {
		return travelDate;
	}

	public void setTravelDate(Date travelDate) {
		this.travelDate = travelDate;
	}

	public List<Receipt> getReceipts() {
		return receipts;
	}

	public void setReceipts(List<Receipt> receipts) {
		this.receipts = receipts;
	}

	public TravelExpenseStatus getStatus() {
		return status;
	}

	public void setStatus(TravelExpenseStatus status) {
		this.status = status;
	}

	public void addReceipt(Receipt receipt) {
		if (receipts == null) {
			receipts = new ArrayList<Receipt>();
		}
		receipts.add(receipt);
	}
	
}
