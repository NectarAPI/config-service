package ke.co.nectar.config.service;

import ke.co.nectar.config.NectarConfigServiceApplication;
import ke.co.nectar.config.entity.PublicKey;
import ke.co.nectar.config.service.publickey.PublicKeyService;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NectarConfigServiceApplication.class)
@FixMethodOrder(MethodSorters.JVM)
@Transactional
public class PublicKeyServiceTest {

    @Autowired
    PublicKeyService publicKeyService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private PublicKey publicKey;
    private final String NAME = "Public Key";
    private final String PUBLIC_KEY = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAgZqr+BXGwQWe5UMY5CLM6a+XbFIZT0CAy/hx8Adhlb0PrwiQ9w4NNy9YMGTyvVTRyKBRgEjFNTJKBBDPFpWJyMa5BmL3JKmGZIYyWaggAILC2QbnEo2GqKbGfys3kiD/HfKCbxwohhNLieI+ULXw46IIriUEQCtx+AZyYTr620E26u1ANMvKzJLZQxTawUDNgy9S/YHSpMMftTF3LbEK5F2J33tLEbRBOVY4fvPL8w3YCx1Wu911+xz7UyVjdLDn26YoSl7+Fz5zZdwdhRMr+hDF8CInhbtAb1/cptFW4VBFVjDmHWn61bHUITbLWK5WRUzYoFWso4yOFYuq7JSMVYBKJE+27aMKZgPWiVrYaZVROxWoge7H//O+/NpWhyj9/K2Mzo6QzcLPTmw/1KN7CvIFIXDo+5wNZ+XFHuDeOaWrd2sMKvqXpEusdZYiuxy0e7Sze8/O5ada3BgFiM50DR1AIjZGONKEfAi2cGRXpBfCBUAU64RMeevobkrDzOSXCDy19o9wTfk4eRiWsuPIGm6zsJqA73+dW0KcSylBF5eaoPQbw/8WJjWClqlpQLfiKwnL2mjk6oFDAtVBfeRNjwd7Dyy1TvdbRJ5QwkfSHuwU2TphwPu/uMRJPOxvtMwgC3LXKnFEB2O9EzEDCrPmv6rOJn1i0tByDcNT0gL49MMCAwEAAQ==";
    private final String MOCK_USER_REF = "0c2914b1-b501-4827-8e49-44321346f52d";
    private final boolean ACTIVATED = true;

    @Before
    public void setup() throws Exception {
        publicKey = publicKeyService.add(NAME, PUBLIC_KEY, MOCK_USER_REF, ACTIVATED);
    }

    @Test
    public void testThatCanSavePublicKey() throws Exception {
        PublicKey publicKey = publicKeyService.findByRef(this.publicKey.getRef());

        Assert.assertNotNull(publicKey);
        Assert.assertEquals(NAME, publicKey.getName());
        Assert.assertEquals(PUBLIC_KEY, publicKey.getKey());
        Assert.assertEquals(MOCK_USER_REF, publicKey.getUserRef());
        Assert.assertEquals(this.publicKey.getRef(), publicKey.getRef());
    }

    @Test
    public void testThatCanFindPublicKeysByUserRef() throws Exception {
        List<PublicKey> publicKeys = publicKeyService.findByUserRef(MOCK_USER_REF);
        PublicKey publicKey = publicKeys.get(0);

        Assert.assertEquals(1, publicKeys.size());
        Assert.assertNotNull(publicKey);
        Assert.assertEquals(NAME, publicKey.getName());
        Assert.assertEquals(PUBLIC_KEY, publicKey.getKey());
        Assert.assertEquals(MOCK_USER_REF, publicKey.getUserRef());
        Assert.assertEquals(this.publicKey.getRef(), publicKey.getRef());
    }

    @Test
    public void testThatCanFindPublicKeysByUserRefActivated() throws Exception {
        List<PublicKey> publicKeys = publicKeyService.findByUserRefActivated(MOCK_USER_REF);

        PublicKey publicKey = publicKeys.get(0);

        Assert.assertEquals(1, publicKeys.size());
        Assert.assertNotNull(publicKey);
        Assert.assertEquals(NAME, publicKey.getName());
        Assert.assertEquals(PUBLIC_KEY, publicKey.getKey());
        Assert.assertEquals(MOCK_USER_REF, publicKey.getUserRef());
        Assert.assertEquals(this.publicKey.getRef(), publicKey.getRef());
    }

    @Test
    public void testThatCanFindPublicKeysByUserRefDeactivated() throws Exception {
        publicKeyService.deactivatePublicKey(publicKey.getRef(), MOCK_USER_REF);
        List<PublicKey> publicKeys = publicKeyService.findByUserRefActivated(MOCK_USER_REF);

        Assert.assertEquals(0, publicKeys.size());
    }

    @Test
    public void testThatPublicKeyIsActivated() throws Exception {
        publicKeyService.activatePublicKey(publicKey.getRef(), MOCK_USER_REF);
        PublicKey publicKey = publicKeyService.findByRef(this.publicKey.getRef());
        Assert.assertTrue(publicKey.isActivated());
    }

    @Test
    public void testThatPublicKeyIsDeactivated() throws Exception {
        publicKeyService.deactivatePublicKey(publicKey.getRef(), MOCK_USER_REF);
        PublicKey publicKey = publicKeyService.findByRef(this.publicKey.getRef());
        Assert.assertTrue(publicKey.isActivated());
    }

    @Test
    public void testThatAllUserPublicKeysAreActivated() throws Exception {
        final String PUBLIC_KEY_2 = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAgZqr+BXGwQWe5UMY5CLM6a+XbFIZT0CAy/hx8Adhlb0PrwiQ9w4NNy9YMGTyvVTRyKBRgEjFNTJKBBDPFpWJyMa5BmL3JKmGZIYyWaggAILC2QbnEo2GqKbGfys3kiD/HfKCbxwohhNLieI+ULXw46IIriUEQCtx+AZyYTr620E26u1ANMvKzJLZQxTawUDNgy9S/YHSpMMftTF3LbEK5F2J33tLEbRBOVY4fvPL8w3YCx1Wu911+xz7UyVjdLDn26YoSl7+Fz5zZdwdhRMr+hDF8CInhbtAb1/cptFW4VBFVjDmHWn61bHUITbLWK5WRUzYoFWso4yOFYuq7JSMVYBKJE+27aMKZgPWiVrYaZVROxWoge7H//O+/NpWhyj9/K2Mzo6QzcLPTmw/1KN7CvIFIXDo+5wNZ+XFHuDeOaWrd2sMKvqXpEusdZYiuxy0e7Sze8/O5ada3BgFiM50DR1AIjZGONKEfAi2cGRXpBfCBUAU64RMeevobkrDzOSXCDy19o9wTfk4eRiWsuPIGm6zsJqA73+dW0KcSylBF5eaoPQbw/8WJjWClqlpQLfiKwnL2mjk6oFDAtVBfeRNjwd7Dyy1TvdbRJ5QwkfSHuwU2TphwPu/uMRJPOxvtMwgC3LXKnFEB2O9EzEDCrPmv6rOJn1i0tByDcNT0gL49MMCAwEAAQ==";
        final String PUBLIC_KEY_3 = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA2qSjk1AlhfczZ/JL7peNIlNIAFhhovc+9mV9y8iQk5r2chvKeVRsy0C9O4lUY+kCDipo/wb8TWssk0xDdo1/GBW69cYm5tneAuHSTMZlv3GhCOhMYryG44Gk0nX+/IXwki+ghlSs/XwzTAL6TVojFmdi3MRgQgs+wH46Wn9zGEXJ7sDIcAtIyXSYDMvAz/cxfKuDxLf4xqvNwOBCkt3wPofLzEfoXP/EUN9JtifXPZWfaZLVSTfRusPzTEQgYSdDR/zrXrigiBiGIdzMflTck3PiZFiz4HUHf+GMiKEREi8ecmPwzChfIilhDA08utUYHw0nQDfUPaOpNEy1YSQiPRogThHDXXaMcX+RpG/Z9g87/wn97Ghc06x3aqBuuI4pjgy1s87ZmpLg3mBsDJ5+u9OOQIaaj7g3gJGqiKqmoWWTlFOCnTf49ktmZPBfsAcOoMQoke3dLNEnHozvQ6CJ2N22P/3BRfkVrTkfpBgd3v3/q7FMaN/MsLuYUQGL+fCuLV/9/iRh/eKqZPAM0SZhZL76F360dwaFIDf0ndJfFHEADsa+EkY1LjF2hiachkdX7AWtsQEzCkFtXHoVKkadtDs2YDoPIS/Gv898TPTO4Pt6f52X5F7Bn1tzko8pOIciQuECnB7FQvhpOiOeL+DXguO91ptPzpr/epdWNjGUIfECAwEAAQ==";

        publicKeyService.add(NAME, PUBLIC_KEY_2, MOCK_USER_REF, ACTIVATED);
        publicKeyService.add(NAME, PUBLIC_KEY_3, MOCK_USER_REF, ACTIVATED);

        List<PublicKey> publicKeys = publicKeyService.findByUserRef(MOCK_USER_REF);
        PublicKey publicKey = publicKeys.get(0);
        PublicKey publicKey2 = publicKeys.get(1);
        PublicKey publicKey3 = publicKeys.get(2);

        Assert.assertEquals(3, publicKeys.size());
        Assert.assertTrue(publicKey.isActivated());
        Assert.assertTrue(publicKey2.isActivated());
        Assert.assertTrue(publicKey3.isActivated());

    }

    @Test
    public void testThatAllUserPublicKeysAreDeactivated() throws Exception {
        final String PUBLIC_KEY_2 = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAgZqr+BXGwQWe5UMY5CLM6a+XbFIZT0CAy/hx8Adhlb0PrwiQ9w4NNy9YMGTyvVTRyKBRgEjFNTJKBBDPFpWJyMa5BmL3JKmGZIYyWaggAILC2QbnEo2GqKbGfys3kiD/HfKCbxwohhNLieI+ULXw46IIriUEQCtx+AZyYTr620E26u1ANMvKzJLZQxTawUDNgy9S/YHSpMMftTF3LbEK5F2J33tLEbRBOVY4fvPL8w3YCx1Wu911+xz7UyVjdLDn26YoSl7+Fz5zZdwdhRMr+hDF8CInhbtAb1/cptFW4VBFVjDmHWn61bHUITbLWK5WRUzYoFWso4yOFYuq7JSMVYBKJE+27aMKZgPWiVrYaZVROxWoge7H//O+/NpWhyj9/K2Mzo6QzcLPTmw/1KN7CvIFIXDo+5wNZ+XFHuDeOaWrd2sMKvqXpEusdZYiuxy0e7Sze8/O5ada3BgFiM50DR1AIjZGONKEfAi2cGRXpBfCBUAU64RMeevobkrDzOSXCDy19o9wTfk4eRiWsuPIGm6zsJqA73+dW0KcSylBF5eaoPQbw/8WJjWClqlpQLfiKwnL2mjk6oFDAtVBfeRNjwd7Dyy1TvdbRJ5QwkfSHuwU2TphwPu/uMRJPOxvtMwgC3LXKnFEB2O9EzEDCrPmv6rOJn1i0tByDcNT0gL49MMCAwEAAQ==";
        final String PUBLIC_KEY_3 = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA2qSjk1AlhfczZ/JL7peNIlNIAFhhovc+9mV9y8iQk5r2chvKeVRsy0C9O4lUY+kCDipo/wb8TWssk0xDdo1/GBW69cYm5tneAuHSTMZlv3GhCOhMYryG44Gk0nX+/IXwki+ghlSs/XwzTAL6TVojFmdi3MRgQgs+wH46Wn9zGEXJ7sDIcAtIyXSYDMvAz/cxfKuDxLf4xqvNwOBCkt3wPofLzEfoXP/EUN9JtifXPZWfaZLVSTfRusPzTEQgYSdDR/zrXrigiBiGIdzMflTck3PiZFiz4HUHf+GMiKEREi8ecmPwzChfIilhDA08utUYHw0nQDfUPaOpNEy1YSQiPRogThHDXXaMcX+RpG/Z9g87/wn97Ghc06x3aqBuuI4pjgy1s87ZmpLg3mBsDJ5+u9OOQIaaj7g3gJGqiKqmoWWTlFOCnTf49ktmZPBfsAcOoMQoke3dLNEnHozvQ6CJ2N22P/3BRfkVrTkfpBgd3v3/q7FMaN/MsLuYUQGL+fCuLV/9/iRh/eKqZPAM0SZhZL76F360dwaFIDf0ndJfFHEADsa+EkY1LjF2hiachkdX7AWtsQEzCkFtXHoVKkadtDs2YDoPIS/Gv898TPTO4Pt6f52X5F7Bn1tzko8pOIciQuECnB7FQvhpOiOeL+DXguO91ptPzpr/epdWNjGUIfECAwEAAQ==";

        publicKeyService.add(NAME, PUBLIC_KEY_2, MOCK_USER_REF, ACTIVATED);
        publicKeyService.add(NAME, PUBLIC_KEY_3, MOCK_USER_REF, ACTIVATED);

        List<PublicKey> publicKeys = publicKeyService.findByUserRef(MOCK_USER_REF);
        PublicKey publicKey = publicKeys.get(0);
        publicKey.setActivated(false);

        PublicKey publicKey2 = publicKeys.get(1);
        publicKey2.setActivated(false);

        PublicKey publicKey3 = publicKeys.get(2);
        publicKey3.setActivated(false);

        Assert.assertEquals(3, publicKeys.size());
        Assert.assertFalse(publicKey.isActivated());
        Assert.assertFalse(publicKey2.isActivated());
        Assert.assertFalse(publicKey3.isActivated());

    }
}
