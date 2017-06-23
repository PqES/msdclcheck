package Services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
public class Services {

    private static final String linkAuthentication = "";
    private static final String linkGetClient = "http://sample-env-3.psebiyinbj.us-east-2.elasticbeanstalk.com/getCustomerCpf/";
    private static final String linkUpdateStock = "http://192.168.1.119:8080/venda";
    private static final String linkGetProducts = "http://sample-env-4.psebiyinbj.us-east-2.elasticbeanstalk.com/getProducts";

    private static String callServiceViaGet(String link) throws IOException, ServiceException {
        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (!(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 299)) {
            throw new ServiceException();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }
        br.close();
        return sb.toString();
    }

    private static String callServiceViaPost(String link, byte[] valuesToSend) throws ServiceException, MalformedURLException, IOException {
        URL obj = new URL(link);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(valuesToSend);
        wr.flush();
        wr.close();
        if (!(con.getResponseCode() >= 200 && con.getResponseCode() <= 299)) {
            throw new ServiceException();
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    public static boolean serviceAuthentication(String user, String password) throws IOException, ServiceException {
        return true;
    }

    public static String serviceGetClient(String cpf) throws IOException, ServiceException {
        if (!cpf.isEmpty()) {
            String clientJson = callServiceViaGet(Services.linkGetClient + cpf);
            if (clientJson.isEmpty()) {
                return null;
            } else {
                return clientJson;
            }
        } else {
            return null;
        }
    }

    public static boolean serviceUpdateStock(String ids[], String qtds[]) throws IOException, ServiceException {
        StringBuilder urlParameters = new StringBuilder("");
        urlParameters.append("id=").append(ids[0]);
        for (int i = 1; i < ids.length; i++) {
            urlParameters.append("&id=").append(ids[i]);
        }
        for (int i = 0; i < qtds.length; i++) {
            urlParameters.append("&qt=").append(qtds[i]);
        }
        System.out.println(urlParameters.toString());
        String result = Services.callServiceViaPost(linkUpdateStock, urlParameters.toString().getBytes());
        System.out.println(result);
        return result.equalsIgnoreCase("sucess");
    }

    public static String serviceGetProducts() throws IOException, ServiceException {
        return callServiceViaGet(Services.linkGetProducts);
    }
}
