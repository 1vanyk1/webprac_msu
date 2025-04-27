package msu.ru.webprac.db.datas;

import java.sql.Date;

public class TimeInterval {
    private Date from;
    private Date to;

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public TimeInterval(Date from, Date to) {
        this.from = from;
        this.to = to;
    }

    public TimeInterval() {
    }
}
