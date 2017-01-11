package org.solr.wikipedia.indexer;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.solr.wikipedia.model.Page;
import org.solr.wikipedia.model.Revision;

import java.util.UUID;

/**
 * Produces the Multimap of key/value pairs for a given Page to index in Solr.
 *
 * @author bryanbende
 */
public class PageMultimap {

    private Page page;

    public PageMultimap(Page page) {
        this.page = page;
        Validate.notNull(page);
    }

    public Multimap<String,Object> getMultimap() {
        Multimap<String,Object> multimap = HashMultimap.create();
        if (StringUtils.isNoneBlank(page.getNamespace(), page.getPageId())) {
            multimap.put(IndexField.id.name(), String.format("%s:%s", page.getNamespace(), page.getPageId()));
        } else {
            multimap.put(IndexField.id.name(), UUID.randomUUID().toString());
        }

        multimap.put(IndexField.namespace.name(), page.getNamespace());
        multimap.put(IndexField.pageId.name(), page.getPageId());

        multimap.put(IndexField.title.name(), page.getTitle());
        multimap.put(IndexField.url.name(), String.format("https://en.wikipedia.org/?curid=%s", page.getPageId()));

        for(Revision rev : page.getRevisions()) {
            multimap.put(IndexField.date.name(), rev.getTimestamp());
            multimap.put(IndexField.content.name(), rev.getText());
            multimap.put(IndexField.category.name(), rev.getCategories());
        }

        return multimap;
    }

}
