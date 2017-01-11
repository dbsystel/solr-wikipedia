package org.solr.wikipedia.indexer;

import com.google.common.collect.Multimap;
import org.junit.Assert;
import org.junit.Test;
import org.solr.wikipedia.model.Page;
import org.solr.wikipedia.util.WikiMediaTestData;

import java.util.List;

/**
 * @author bryanbende
 */
public class PageMultimapTest {

    @Test
    public void testMultimap() {
        List<Page> pages = WikiMediaTestData.getPages();

        Page page = pages.get(0);
        PageMultimap pageMultimap = new PageMultimap(page);

        Multimap<String,Object> multimap = pageMultimap.getMultimap();
        Assert.assertNotNull(multimap);

        Assert.assertTrue(multimap.containsKey(
                IndexField.id.name()));

        Assert.assertTrue(multimap.containsKey(IndexField.title.name()));
        Assert.assertEquals(page.getTitle(),
                multimap.get(IndexField.title.name()).iterator().next());

        Assert.assertTrue(multimap.containsKey(
                IndexField.content.name()));
        Assert.assertTrue(multimap.containsKey(
                IndexField.content.name()));
    }

}
