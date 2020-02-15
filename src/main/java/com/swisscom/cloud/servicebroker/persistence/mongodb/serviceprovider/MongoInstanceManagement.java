package com.swisscom.cloud.servicebroker.persistence.mongodb.serviceprovider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class MongoInstanceManagement {

    private static final Logger logger = LoggerFactory.getLogger(MongoInstanceManagement.class);

    public static String getStatus() {
        Process p = null;
        String status = "";
        String pkey = "-----BEGIN RSA PRIVATE KEY-----\n" +
                "MIIEowIBAAKCAQEAyKoh2kWmL2CdWEMbKvpnmQg9Y206O62ugA3tOtv44PR8xyUa\n" +
                "uOAZv3eqJsyONey79MeUN6wpaHguI/RQkuC4oyHJe3jxbDD2yZUIWdyjs1UR0snO\n" +
                "2tAwBge+O6w8Xzjf59aXsWByvaTJ6/mXCKmmUEP2x5hogmLYONQ00q0zE4irvunx\n" +
                "buTT1o8VHtNNnJsZy2mgnLX4XXSjkNnTpwTHalw7kXbM06xQ42JFuLOCCyEvQ/dL\n" +
                "WnncoztaCiDNhAjtX7apsQAjqOU5/jYj5mpsyB1VlpGvA0p+WGfdFFecKa311esy\n" +
                "8mVFCCP50xLaKFNeaLpWjGELtu2N3RUerkSvuQIDAQABAoIBAGXM6h+x9f1TJoAA\n" +
                "i2fiYWxhocMvRrvAASc16YRS1JBLeIIPcN8Z0fYw30GPxRXWZARqu+cCkH9PeRkE\n" +
                "eVwKZaFdIrphQRmCPbFGylXMecAhCEnH2AlqVPK6OuhLGNW2JOGZHJwAMjXDxCzT\n" +
                "EqxyZM2TrT7VMy/ytX8fKS04cREW/uroW5V99NN4uColi8iudS/12WRTwggJf2C8\n" +
                "LzGLD4G8b6e26bgtubu7umF1UpWgq+yeKozgFnZF/Xf40E+0vzDIXV+5E71TFb8U\n" +
                "kkcV9vwr0Fy1bQjwXCN9ouEd1YxANTkKm1M/nFRoq5NyaaczKQbEYH3RdSgXHGpQ\n" +
                "iiNm4AECgYEA6xVCO5Gtt9yB00rwiyeZu8dRlbPMUQEQwmSIXpAWHENvsERj+BBf\n" +
                "+wl7kWJC3mnBmtMxqkCDWwzXQbjnkm3YeVakBqSNFEI9zuXTTc5N33YgRZcgKZB/\n" +
                "BNgtAfhVt3Q9Tb3JWG6+zVJ5H+rVW50yGrP2EJY0Myt4G2FNai5qLFkCgYEA2oTj\n" +
                "CmSIlc5WGWXESOB53mXTeVZ0kbX81MZYufkpy1SpJUNp61CLxClR9eY2FxpzCGNj\n" +
                "oj+UK6vatA1LZmLHITSgM903SKM2q8GTvAKcsKkwdi8y3O4CoIYw84L+RPhKdGd4\n" +
                "rDWVWMGzuKyhDvpG1lr6LU4+r7GODTS4RXhUsmECgYEArnkYrx1v0QXXVPpJjP3X\n" +
                "G4bTBkhrW7NuvNfgXk6SGmfyfOBFUdbVZXukOkKzXl8fUcltEu7OjsT4/c4+EG92\n" +
                "OpgTJNr72g1P0j7AlP6IzVDeoseHF1Im83F99fhZpc6fsRMqXHfVJI4RV57yuwvw\n" +
                "I+FsvuK72z5n5epRinzoUAECgYBXQvfTf6AXiqVRURVYMOQZzrnrW34DbhQ/99EY\n" +
                "uwpYmz9MlSYoBSs7M3D6bGsvQK2tLNttwEvwQJl+i0yP32Q7u2M6Mt3P0PWVEyFm\n" +
                "0/g0h+vFVkXOefFsnwnPRIE7k5dP8r36rw8ruXaGidFrRMCi+wOqsfNQf+wFFAd+\n" +
                "HnZcIQKBgCll0PL2d4C/QxwXhW+3iJqglfcrv5jIktPlC/nS8l+fp/J4QiVv9Eem\n" +
                "zRRAFdK3M9/dYM7a7WKAezdU5S8IqdPiusUqFIVvyxzTLVkxz4EgSW7RJsAqKH/o\n" +
                "P+DWjxx3K/LBUjUyHAo/RUp0SPgQez3AJIda+/4gb8lY8Z3tlkBC\n" +
                "-----END RSA PRIVATE KEY-----\n";
        try {
            p = Runtime.getRuntime().exec("ssh -i " + pkey + "user@192.168.1.134");
            PrintStream out = new PrintStream(p.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

            out.println("docker ps --filter \"name\"=\"mongodb1");
            logger.info("Docker Farm with MongoDBs - connceted");
            while (in.ready()) {
                String s = in.readLine();
                logger.info("Status from Docker Farm: " + s);
            }
            out.println("exit");
            status = "completed";
            p.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return status;
    }

}
