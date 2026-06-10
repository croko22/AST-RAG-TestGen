import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.jsoup.internal.SharedConstants;

public class SharedConstantsTest {

    @Test
    public void testUserDataKey() {
        // Given: SharedConstants class
        // When: access UserDataKey
        String userDataKey = SharedConstants.UserDataKey;
        // Then: verify value
        assertEquals("/jsoup.userdata", userDataKey);
    }

    @Test
    public void testAttrRangeKey() {
        // Given: SharedConstants class
        // When: access AttrRangeKey
        String attrRangeKey = SharedConstants.AttrRangeKey;
        // Then: verify value
        assertEquals("jsoup.attrs", attrRangeKey);
    }

    @Test
    public void testRangeKey() {
        // Given: SharedConstants class
        // When: access RangeKey
        String rangeKey = SharedConstants.RangeKey;
        // Then: verify value
        assertEquals("jsoup.start", rangeKey);
    }

    @Test
    public void testEndRangeKey() {
        // Given: SharedConstants class
        // When: access EndRangeKey
        String endRangeKey = SharedConstants.EndRangeKey;
        // Then: verify value
        assertEquals("jsoup.end", endRangeKey);
    }

    @Test
    public void testXmlnsAttr() {
        // Given: SharedConstants class
        // When: access XmlnsAttr
        String xmlnsAttr = SharedConstants.XmlnsAttr;
        // Then: verify value
        assertEquals("jsoup.xmlns-", xmlnsAttr);
    }

    @Test
    public void testDefaultBufferSize() {
        // Given: SharedConstants class
        // When: access DefaultBufferSize
        int defaultBufferSize = SharedConstants.DefaultBufferSize;
        // Then: verify value
        assertEquals(8 * 1024, defaultBufferSize);
    }

    @Test
    public void testFormSubmitTags() {
        // Given: SharedConstants class
        // When: access FormSubmitTags
        String[] formSubmitTags = SharedConstants.FormSubmitTags;
        // Then: verify values
        assertArrayEquals(new String[] { "input", "keygen", "object", "select", "textarea" }, formSubmitTags);
    }

    @Test
    public void testDummyUri() {
        // Given: SharedConstants class
        // When: access DummyUri
        String dummyUri = SharedConstants.DummyUri;
        // Then: verify value
        assertEquals("https://dummy.example/", dummyUri);
    }

    @Test
    public void testUseHttpClient() {
        // Given: SharedConstants class
        // When: access UseHttpClient
        String useHttpClient = SharedConstants.UseHttpClient;
        // Then: verify value
        assertEquals("jsoup.useHttpClient", useHttpClient);
    }

    @Test
    public void testUseRe2j() {
        // Given: SharedConstants class
        // When: access UseRe2j
        String useRe2j = SharedConstants.UseRe2j;
        // Then: verify value
        assertEquals("jsoup.useRe2j", useRe2j);
    }

    @Test
    public void testPrivateConstructor() {
        // Given: SharedConstants class
        // When: try to instantiate
        assertThrows(InstantiationException.class, () -> {
            SharedConstants sharedConstants = (SharedConstants) SharedConstants.class.getDeclaredConstructor().newInstance();
        });
    }
}