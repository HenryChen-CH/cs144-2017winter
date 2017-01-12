import java.security.MessageDigest;
import java.io.FileInputStream;

class ComputeSHA {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Wrong number of arguments");
            return;
        }
        FileInputStream fs;
        MessageDigest md;
        try {
            fs = new FileInputStream(args[0]);
            try {
                md = MessageDigest.getInstance("SHA-1");
                byte[] bytes = new byte[1024];
                int n = 0;
                while ((n = fs.read(bytes)) != -1) {
                    md.update(bytes, 0, n);
                }
                byte[] mdbytes = md.digest();
                StringBuffer sb = new StringBuffer();
                for (byte b: mdbytes) {
                    sb.append(Integer.toString((b&0xff)+0x100, 16).substring(1));
                }
                System.out.println(sb.toString());
            } catch (Exception e) {
                System.out.println("MessageDigest error");
            }
        } catch (Exception e) {
            System.out.println("File error");
        }
        return;
    }
}
