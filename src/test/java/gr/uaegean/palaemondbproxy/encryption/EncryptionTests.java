package gr.uaegean.palaemondbproxy.encryption;

import gr.uaegean.palaemondbproxy.utils.CryptoUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EncryptionTests {

    @Autowired
    CryptoUtils cryptoUtils;

    @Test
    public void testMakeKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();
        File publicKeyFile = new File("public.key");
        File privateKeyFile = new File("private.key");
        if (!publicKeyFile.exists()) {
            try (FileOutputStream fos = new FileOutputStream("public.key")) {
                fos.write(publicKey.getEncoded());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!privateKeyFile.exists()) {
            try (FileOutputStream fos = new FileOutputStream("private.key")) {
                fos.write(privateKey.getEncoded());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void testEncryption() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String secretMessage = "new test";
        File publicKeyFile = new File("public.key");
        byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);

        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] secretMessageBytes = secretMessage.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
        String encodedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);
        System.out.print(encodedMessage);

    }


    //2021-12-15 13:48:19.552  INFO 46081 --- [ionShutdownHook] o.a.k.clients.producer.KafkaProducer     : [Producer clientId=producer-1] Closing the Kafka producer with timeoutMillis = 9223372036854775807 ms.

    @Test
    public void testDecryptMessage() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        File privateKeyFile = new File("private.key");
        Cipher decryptCipher = Cipher.getInstance("RSA");
        byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
        String encryptedMessage = "ldfp032npoIEGGzl4RWzosiDT2QmnzUFB5nZ7mBR9u4zzrhxodFVCxHexahJgsDyTC7d5pCEcj5S2URS3qmRdL+q+09Yxthzs+QL+GI0c8O3T0j99LPB7xGXGAws980G86h9GAr5Gt1wHHnZ70dPVfGoTjG2f0YbTKCXg2maGGsYioA06vAdccmHpTl3OiGto7D2RNA6aa4T1Lavssc3TlHGvgfHUVOevn252N1ItHJ0D3qJwa1prjz5P75P5rzZYhQyyGUDhTG1Udi6JyySciUGW58PCPz3YiXNacRv80+YHPkQdTcE9daY6VbaQgxQPMDmtZ6H+YKOhNirbHHtVA==";
        byte[] encryptedMessageBytes = Base64.getDecoder().decode(encryptedMessage);

        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedMessageBytes = decryptCipher.doFinal(encryptedMessageBytes);
        String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);

        assertEquals("new test", decryptedMessage);
    }

    @Test
    public void testEncryptDecrypt() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String message = "test1";
        String encrypted = cryptoUtils.encryptBase64(message);
        System.out.println(encrypted);

        String decrypted = cryptoUtils.decryptBase64Message(encrypted);
        System.out.println(decrypted);

        assertEquals(decrypted, message);
    }

}
