package org.solr.wikipedia.indexer;

/**
 * Enumeration of fields to index and search in Solr.
 *
 * @author bryanbende
 */
public enum IndexField {

    id,
    namespace,
    pageId,
    title,
    content,
    date,
    category,
    url;
}
