package dk.nykredit.jackson.dataformat.hal.deser;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.nykredit.jackson.dataformat.hal.HALLink;
import dk.nykredit.jackson.dataformat.hal.HALMapper;
import dk.nykredit.jackson.dataformat.hal.annotation.EmbeddedResource;
import dk.nykredit.jackson.dataformat.hal.annotation.Link;
import dk.nykredit.jackson.dataformat.hal.annotation.Resource;
import java.io.StringReader;
import java.util.Collection;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HALBeanDeserializerIT {
    static final String HAL_DOC = "{"
            + "\"_links\":{"
            + "\"child\":[{\"href\":\"/top/1/child/1\"},{\"href\":\"/top/1/child/2\"}],"
            + "\"empty:list\":[],"
            + "\"null:list\":[],"
            + "\"self\":{\"href\":\"/top/1\"},"
            + "\"templated\":{\"href\":\"/uri/{id}\",\"templated\":true}"
            + "},"
            + "\"_embedded\":{"
            + "\"child\":["
            + "{\"_links\":{\"self\":{\"href\":\"/top/1/child/1\"}},\"id\":\"1\"},"
            + "{\"_links\":{\"self\":{\"href\":\"/top/1/child/2\"}},\"id\":\"2\"}]"
            + "},"
            + "\"id\":\"1\"}";

    ObjectMapper om = new HALMapper();

    @Test
    public void testDeserialization() throws Exception {
        TopResource tr = om.readValue(new StringReader(HAL_DOC), TopResource.class);
        assertEquals("1", tr.id);
        assertEquals("/top/1", tr.self.getHref());
        assertEquals("/uri/{id}", tr.templated.getHref());
        assertTrue(tr.templated.getTemplated());
        assertEquals(2, tr.childLinks.size());
        assertEquals(2, tr.children.size());
        System.out.println("tr: " + tr);
    }

    @Resource
    public static class TopResource {

        public String id;

        @Link
        public HALLink self;

        @Link
        public HALLink templated;

        @Link
        public HALLink nullLink;

        @Link("child")
        public Collection<HALLink> childLinks;

        @Link("empty:list")
        public Collection<HALLink> childLinksEmpty;

        @Link("null:list")
        public Collection<HALLink> childLinksNull;

        @EmbeddedResource("child")
        public Collection<ChildResource> children;

        @EmbeddedResource
        public String nullString;
    }

    @Resource
    public static class ChildResource {

        public String id;

        @Link
        public HALLink self;
    }
    
}
