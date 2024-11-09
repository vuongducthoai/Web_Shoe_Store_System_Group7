package dto;

public class MessageDTO {
    private Integer messageId;
    private String content;
    private Boolean isRead;
    private LocalDateTime timeStamp;
    private Integer chatId;

    public Integer getMessageId() {
        return this.messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsRead() {
        return this.isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getChatId() {
        return this.chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }
}
