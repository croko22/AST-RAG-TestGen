import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EntitiesDataTest {

    private EntitiesData entitiesData;

    @BeforeEach
    public void setup() {
        entitiesData = new EntitiesData();
    }

    @Test
    public void testXmlPoints() {
        // Given
        String expectedXmlPoints = "amp=12;1&gt=1q;3&lt=1o;2\"=y;0&";

        // When
        String actualXmlPoints = EntitiesData.xmlPoints;

        // Then
        assertEquals(expectedXmlPoints, actualXmlPoints);
    }

    @Test
    public void testBasePoints() {
        // Given
        String expectedBasePoints = "AElig=5i;1c&AMP=12;2&Aacute=5d;17&Acirc=5e;18&Agrave=5c;16&Aring=5h;1b&Atilde=5f;19&Auml=5g;1a&COPY=4p;h&Ccedil=5j;1d&ETH=5s;1m&Eacute=5l;1f&Ecirc=5m;1g&Egrave=5k;1e&Euml=5n;1h&GT=1q;6&Iacute=5p;1j&Icirc=5q;1k&Igrave=5o;1i&Iuml=5r;1l&LT=1o;4&Ntilde=5t;1n&Oacute=5v;1p&Ocirc=5w;1q&Ograve=5u;1o&Oslash=60;1u&Otilde=5x;1r&Ouml=5y;1s&QUOT=y;0&REG=4u;n&THORN=66;20&Uacute=62;1w&Ucirc=63;1x&Ugrave=61;1v&Uuml=64;1y&Yacute=65;1z&aacute=69;23&acirc=6a;24&acute=50;u&aelig=6e;28&agrave=68;22&amp=12;3&aring=6d;27&atilde=6b;25&auml=6c;26&brvbar=4m;e&ccedil=6f;29&cedil=54;y&cent=4i;a&copy=4p;i&curren=4k;c&deg=4w;q&divide=6v;2p&eacute=6h;2b&ecirc=6i;2c&egrave=6g;2a&eth=6o;2i&euml=6j;2d&frac12=59;13&frac14=58;12&frac34=5a;14&gt=1q;7&iacute=6l;2f&icirc=6m;2g&iexcl=4h;9&igrave=6k;2e&iquest=5b;15&iuml=6n;2h&laquo=4r;k&lt=1o;5&macr=4v;p&micro=51;v&middot=53;x&nbsp=4g;8&not=4s;l&ntilde=6p;2j&oacute=6r;2l&ocirc=6s;2m&ograve=6q;2k&ordf=4q;j&ordm=56;10&oslash=6w;2q&otilde=6t;2n&ouml=6u;2o&para=52;w&plusmn=4x;r&pound=4j;b\"=y;1&raquo=57;11&reg=4u;o&sect=4n;f&shy=4t;m&sup1=55;z&sup2=4y;s&sup3=4z;t&szlig=67;21&thorn=72;2w&times=5z;1t&uacute=6y;2s&ucirc=6z;2t&ugrave=6x;2r&uml=4o;g&uuml=70;2u&yacute=71;2v&yen=4l;d&yuml=73;2x&";

        // When
        String actualBasePoints = EntitiesData.basePoints;

        // Then
        assertEquals(expectedBasePoints, actualBasePoints);
    }

    @Test
    public void testFullPoints() {
        // Given
        String expectedFullPoints = "AElig=5i;2v&AMP=12;8&Aacute=5d;2p&Abreve=76;4k&Acirc=5e;2q&Acy=sw;av&Afr=2kn8;1kh&Agrave=5c;2o&Alpha=pd;8d&Amacr=74;4i&And=8cz;1e1&Aogon=78;4m&Aopf=2koo;1ls&ApplyFunction=6e9;ew&Aring=5h;2t&Ascr=2kkc;1jc&Assign=6s4;s6&Atilde=5f;2r&Auml=5g;2s&Backslash=6qe;o1&Barv=8h3;1it&Barwed=6x2;120&Bcy=sx;aw&Because=6r9;pw&Bernoullis=6jw;gn&Beta=pe;8e&Bfr=2kn9;1ki&Bopf=2kop;1lt&Breve=k8;82&Bscr=6jw;gp&Bumpeq=6ry;ro&CHcy=tj;bi&COPY=4p;1q&Cacute=7a;4o&Cap=6vm;zz&CapitalDifferentialD=6kl;h8&Cayleys=6jx;gq&Ccaron=7g;4u&Ccedil=5j;2w&Ccirc=7c;4q&Cconint=6r4;pn&Cdot=7e;4s&Cedilla=54;2e&CenterDot=53;2b&Cedilla=54;2e&CenterDot=53;2b&Cedilla=54;2e&CenterDot=53;2b&";

        // When
        String actualFullPoints = EntitiesData.fullPoints;

        // Then
        assertEquals(expectedFullPoints, actualFullPoints);
    }
}