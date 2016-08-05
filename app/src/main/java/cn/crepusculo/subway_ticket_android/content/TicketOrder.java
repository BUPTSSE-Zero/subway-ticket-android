package cn.crepusculo.subway_ticket_android.content;

import com.subwayticket.database.model.Account;
import com.subwayticket.database.model.SubwayStation;
import com.subwayticket.database.model.TicketPrice;

import java.util.Date;

public class TicketOrder {
    public static final char ORDER_STATUS_NOT_PAY = 'A';
    public static final char ORDER_STATUS_NOT_EXTRACT_TICKET = 'B';
    public static final char ORDER_STATUS_FINISHED = 'C';
    public static final char ORDER_STATUS_REFUNDED = 'D';
    private String ticketOrderId;
    private Date ticketOrderTime;
    private transient Account user;
    private SubwayStation endStation;
    private SubwayStation startStation;
    private float ticketPrice;
    private int extractAmount;
    private int amount;
    private char status;
    private String extractCode;
    private String comment;

    public TicketOrder(com.subwayticket.database.model.TicketOrder server) {
        this.ticketOrderId = server.getTicketOrderId();
        this.ticketOrderTime = server.getTicketOrderTime();
        this.user = server.getUser();
        this.endStation = server.getEndStation();
        this.startStation = server.getStartStation();
        this.ticketPrice = server.getTicketPrice();
        this.amount = server.getAmount();
        this.status = server.getStatus();
        this.extractAmount = server.getExtractAmount();
    }

    public TicketOrder() {
    }

    public TicketOrder(String ticketOrderId, Date ticketOrderTime, Account user, TicketPrice ticketPrice, int amount) {
        this(ticketOrderId, ticketOrderTime, user, ticketPrice.getSubwayStationA(), ticketPrice.getSubwayStationB(), ticketPrice.getPrice(), amount);
    }

    public TicketOrder(String ticketOrderId, Date ticketOrderTime, Account user, SubwayStation startStation, SubwayStation endStation, float ticketPrice, int amount) {
        this.ticketOrderId = ticketOrderId;
        this.ticketOrderTime = ticketOrderTime;
        this.user = user;
        this.endStation = endStation;
        this.startStation = startStation;
        this.ticketPrice = ticketPrice;
        this.amount = amount;
        this.status = 65;
        this.extractAmount = 0;
    }

    public String getTicketOrderId() {
        return this.ticketOrderId;
    }

    public void setTicketOrderId(String ticketOrderId) {
        this.ticketOrderId = ticketOrderId;
    }

    public Date getTicketOrderTime() {
        return this.ticketOrderTime;
    }

    public void setTicketOrderTime(Date ticketOrderTime) {
        this.ticketOrderTime = ticketOrderTime;
    }

    public Account getUser() {
        return this.user;
    }

    public void setUser(Account user) {
        this.user = user;
    }

    public SubwayStation getStartStation() {
        return this.startStation;
    }

    public void setStartStation(SubwayStation startStation) {
        this.startStation = startStation;
    }

    public SubwayStation getEndStation() {
        return this.endStation;
    }

    public void setEndStation(SubwayStation endStation) {
        this.endStation = endStation;
    }

    public float getTicketPrice() {
        return this.ticketPrice;
    }

    public void setTicketPrice(float ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public int getExtractAmount() {
        return this.extractAmount;
    }

    public void setExtractAmount(int drawAmount) {
        this.extractAmount = drawAmount;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public char getStatus() {
        return this.status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public String getExtractCode() {
        return this.extractCode;
    }

    public void setExtractCode(String ticketKey) {
        this.extractCode = ticketKey;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean equals(Object that) {
        if (this == that) {
            return true;
        } else if (that != null && this.getClass() == that.getClass()) {
            TicketOrder that1;
            if (Double.compare((double) (that1 = (TicketOrder) that).ticketPrice, (double) this.ticketPrice) != 0) {
                return false;
            } else if (this.extractAmount != that1.extractAmount) {
                return false;
            } else if (this.amount != that1.amount) {
                return false;
            } else {
                if (this.ticketOrderId != null) {
                    if (!this.ticketOrderId.equals(that1.ticketOrderId)) {
                        return false;
                    }
                } else if (that1.ticketOrderId != null) {
                    return false;
                }

                if (this.status != that1.status) {
                    return false;
                } else {
                    if (this.extractCode != null) {
                        if (!this.extractCode.equals(that1.extractCode)) {
                            return false;
                        }
                    } else if (that1.extractCode != null) {
                        return false;
                    }

                    if (this.comment != null) {
                        if (!this.comment.equals(that1.comment)) {
                            return false;
                        }
                    } else if (that1.comment != null) {
                        return false;
                    }

                    return true;
                }
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.ticketOrderId != null ? this.ticketOrderId.hashCode() : 0;
        long temp = Double.doubleToLongBits((double) this.ticketPrice);
        result = result * 31 + (int) (temp ^ temp >>> 32);
        result = result * 31 + this.extractAmount;
        result = result * 31 + this.amount;
        result = result * 31 + (Character.valueOf(this.status)).hashCode();
        result = result * 31 + (this.extractCode != null ? this.extractCode.hashCode() : 0);
        return result * 31 + (this.comment != null ? this.comment.hashCode() : 0);
    }
}
