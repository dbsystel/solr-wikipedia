package org.solr.wikipedia.handler;

import java.util.Collection;

public interface PageHandler<T> {

    public void startPage();

    public void title(String title);

    void pageId(String pageId);

    void namespace(String namespace);

    public void startRevision();

    public void timestamp(String timestamp);

    public void text(String text);

    void categories(Collection<String> categories);

    public void endRevision();

    public T endPage();

}
