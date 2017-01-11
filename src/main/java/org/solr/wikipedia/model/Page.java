package org.solr.wikipedia.model;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class the represents a Page from the WikiMedia XML.
 *
 * @author bryanbende
 */
public class Page {

    private final String title, pageId, namespace;

    private final List<Revision> revisions;

    private Page(PageBuilder builder) {
        this.title = builder.title;
        this.pageId = builder.pageId;
        this.namespace = builder.namespace;
        this.revisions = builder.revisions;
        Validate.notEmpty(title);
        Validate.notEmpty(namespace);
        Validate.notEmpty(revisions);
    }

    public String getTitle() {
        return title;
    }

    public String getPageId() {
        return pageId;
    }

    public String getNamespace() {
        return namespace;
    }

    public List<Revision> getRevisions() {
        return this.revisions;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).
                append(title).
                append(revisions).
                toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }

        Page that = (Page) obj;
        return new EqualsBuilder()
                .append(this.title, that.title)
                .append(this.revisions, that.revisions)
                .isEquals();
    }

    @Override
    public String toString() {
        return "Page [title = " + title + "]";
    }

    /**
     * A builder for Page instances.
     */
    public static class PageBuilder {
        private String title;
        private String pageId;
        private String namespace;
        private List<Revision> revisions;

        public PageBuilder title(String title) {
            this.title = title;
            return this;
        }

        public PageBuilder revision(Revision revision) {
            if (this.revisions == null) {
                this.revisions = new ArrayList<>();
            }
            this.revisions.add(revision);
            return this;
        }

        public PageBuilder pageId(String pageId) {
            this.pageId = pageId;
            return this;
        }

        public PageBuilder namespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        public Page build() {
            return new Page(this);
        }
    }

}
