package msu.ru.webprac.db;

import java.io.Serializable;

public interface ListBase<SELF extends ListBase<SELF, I>, I extends Serializable> {
    public I getId();

    public void setId(I id);

    public void replace(SELF other);
}
