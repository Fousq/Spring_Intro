package kz.zhanbolat.spring.controller.model;

public class BookUnbookDto {
    private Integer userId;
    private Integer ticketId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }


}
