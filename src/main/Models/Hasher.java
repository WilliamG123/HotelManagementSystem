import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
    private final String algo;

    private Hasher(String algo){
        this.algo = algo;
    }

    public String hash(String toHash) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(this.algo);
        digest.update(toHash.getBytes(StandardCharsets.UTF_8));
        return new String(digest.digest());
    }

    public static Hasher getInstance(String algo){
        return new Hasher(algo);
    }
}
