import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.StringTokenizer;

public class  Sha256Encoder {
    
    String encBase64(String str) {
        byte[] bytes = null;
        try {
            bytes = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encBase64(bytes);
    }

    String encBase64(byte[] str) {
        return Base64.getEncoder().encodeToString(str);
    }

    String decBase64(String str) {
        byte[] bytes = Base64.getDecoder().decode(str);
        String result  = null;
        try {
            result =  new String(bytes,"UTF-8");
        } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
        }
        return result;
    }

    String encSHA256(String str) {
        StringBuffer buf  = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes());
            byte bytes[] = md.digest();
            for (int i = 0 ; i < bytes.length; i++ ) {
                buf.append(String.format("%02x",bytes[i]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

    byte[] encSHA256ToByte(String str) {
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes());
            bytes = md.digest();
             
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    String encSHA1(String str) {
        StringBuffer buf  = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte bytes[] = md.digest();
            for (int i = 0 ; i < bytes.length; i++ ) {
                buf.append(String.format("%02x",bytes[i]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

    byte[] encSHA1ToByte(String str) {
        byte[] bytes =  null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            bytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("e256",false, "encode using SHA256");
        options.addOption("e1",false, "encode using SHA1");
        options.addOption("e64", false, "encode using base64");
        options.addOption("d64", false, "decode using base64");
        options.addOption("e25664", false, "encode using SHA256 and base64");
        options.addOption("e164", false, "encode using SHA1 and base64");
        options.addOption(Option.builder("o")
                .argName("outfile")
                .hasArg()
                .desc("output filename")
                .build());

        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Sha256Encoder -e256 [-o result.out] filename", options);

        // create the parser
        CommandLineParser parser = new DefaultParser();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            Sha256Encoder app = new Sha256Encoder();

            BufferedReader br = null;
            PrintWriter pw = null;

            String fn = line.getArgs()[0];
            String result_fn  = fn.substring(0,fn.lastIndexOf(".")) + ".out";

            if  (line.hasOption("o")) {
                result_fn = line.getOptionValue("o");
            }
            System.out.println("result filename : [ "+result_fn+" ]");

            try {
                br = new BufferedReader(new FileReader(new File(fn)));
                pw = new PrintWriter(new FileWriter(new File(result_fn)));

                String buf  = null;
                while ((buf = br.readLine()) != null) {
                    StringTokenizer tokens = new StringTokenizer(buf, "|");
                    String id  = tokens.nextToken().trim();
                    String password = tokens.nextToken().trim();
                    String resultString = null;
                    if  (line.hasOption("e256"))
                        resultString = id+"|"+app.encSHA256(password);
                    else if (line.hasOption("e1"))
                        resultString = id+"|"+app.encSHA1(password);
                    else if (line.hasOption("e64"))
                        resultString = id+"|"+app.encBase64(password);
                    else if (line.hasOption("d64"))
                        resultString = id+"|"+app.decBase64(password);
                    else if (line.hasOption("e25664"))
                        resultString = id+"|"+app.encBase64(app.encSHA256ToByte(password));
                        // SHA256 처리후 base64 encoding 을 연속적으로 처리하는데
                        // sha256 encoding 결과물을 byte[] 로 받아서 base64 encoding 을 태우는것과
                        // sha256 encoding 결과를 string 으로 받아서 base64 encoding 을 태우는 결과가 다르다
                        // 여기서는 byte 배열로 받아서 base64 처리 하는 것으로 구현함
                    else if (line.hasOption("e164"))
                        resultString = id+"|"+app.encBase64(app.encSHA1ToByte(password));

                    System.out.println(id + "|" +  password + "  >>> result string: " +  resultString);
                    pw.println(resultString);
                }
            } catch (FileNotFoundException e)  {
                System.out.println(e.getMessage());
            } catch (IOException e ) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pw.close();
            }
        }
        catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }

    }

}