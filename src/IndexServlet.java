import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "IndexServlet", description = "IndexServlet", urlPatterns = { "/", "/IndexServlet" , "/IndexServlet.do"})
public class IndexServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String key = request.getParameter("keyword");
        if (!request.getParameter("keyword").equals("")) {
            key = "obama";
        }
        int amount = 10;
        if (!request.getParameter("amount").equals("")) {
            amount = Integer.parseInt(request.getParameter("amount"));
        }
        String tweets = TwitterSearch.getTweets(key, amount);
        int sentiment = 0;
//        if (tweets!=null) {
//            sentiment = SentimentAnalyser.analyse(tweets);
//        }
//        else{
//            tweets = "No tweets found";
//        }
        String docType =
                "<!doctype html public \"-//w3c//dtd html 4.0 " +
                        "transitional//en\">\n";
        out.println(docType +
                "<html>\n" +
                "<head><title>Title</title></head>\n" +
                "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\">"+
                "<body style=\"padding: 30px;\">\n" +
                "<h1 style=\"margin-bottom: 30px;\">Twitter Sentiment Analyses</h1>\n" +
                "<form method=\"POST\">\n" +
                "<label for=\"keyword\">Key word:</label>\n" +
                "<input id=\"keyword\" name=\"keyword\" type=\"text\"/><br/>\n" +
                "<label for=\"amount\">Amount:</label>\n" +
                "<input id=\"amount\" name=\"amount\" type=\"number\" value=\"10\"/>\n" +
                "<span>(default: 100)<span><br/>\n" +
                "<input type=\"submit\" value=\"Submit\">\n" +
                "</form>\n" +
                "<h2>Tweets</h2>" +
                "<p>"+tweets+"</p>" +
                "</body></html>");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String docType =
                "<!doctype html public \"-//w3c//dtd html 4.0 " +
                        "transitional//en\">\n";
        out.println(docType +
                "<html>\n" +
                "<head><title>Title</title></head>\n" +
                "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\">"+
                "<body style=\"padding: 30px;\">\n" +
                "<h1 style=\"margin-bottom: 30px;\">Twitter Sentiment Analyses</h1>\n" +
                "<form method=\"POST\">\n" +
                "<label for=\"keyword\">Key word:</label>\n" +
                "<input id=\"keyword\" name=\"keyword\" type=\"text\"/><br/>\n" +
                "<label for=\"amount\">Amount:</label>\n" +
                "<input id=\"amount\" name=\"amount\" type=\"number\" value=\"10\"/>\n" +
                "<span>(default: 100)<span><br/>\n" +
                "<input type=\"submit\" value=\"Submit\">\n" +
                "</form>\n" +
                "</body></html>");
    }
}
