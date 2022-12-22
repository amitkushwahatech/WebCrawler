package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;

public class Crawler {

    // using to store uniq url links --we don't want to visit same url again and again so we use uniq set of url
    private HashSet<String> urlLink;
    // set max depth for crawler to get only that depth
    private int MAX_DEPTH =2;
    //connection object
    public Connection connection;
    // constructor to initialise HashSet
    public  Crawler(){
        //set up connection to MySQL
        connection = DatabaseConnection.getConnection();
        urlLink = new HashSet<String>();
    }
    // recursive method crawler over the internet
    public void getPageTextAndLinks(String url, int depth){
        if(!urlLink.contains(url)){
            if(urlLink.add(url)){
                System.out.println(url);
            }
            try{
            //get HTML page as a document
            // Library Jsoup --> to get the data from HTML page to the java
            // Convert the HTML Object to java Object
            // help to parse the HTML Page
                // Parsing HTML object to Java Document object
                Document document = Jsoup.connect(url).timeout(5000).get();
                // Get text from document object
                String  text  =document.text().length()<500?document.text(): document.text().substring(0,499);
                //Print text
                System.out.println(text);

                //Insert data into pages table
               // preparedStarement --> write insertion command from the java
                PreparedStatement preparedStatement = connection.prepareStatement("Insert into pages values(?,?,?)");
                preparedStatement.setString(1,document.title());
                preparedStatement.setString(2,url);
                preparedStatement.setString(3, text);
                preparedStatement.executeUpdate();

                // increase depth
                depth++;
                //if depth is greater then max_depth(or magit x limit ) then return
                if(depth>MAX_DEPTH){
                    return;
                }
                //check availabel link of the page ;
                // get hyperLink availabel on current page
                Elements availableLinkOnPage = document.select("a[href]");
                // every availabel link run recursive method
                // run method recursively for every link availabel on current page
                for(Element currentLink: availableLinkOnPage){
                    // href--> url of the page , a --> anchor tag, abs--> to create hyperlink
                    getPageTextAndLinks(currentLink.attr("abs:href"), depth);
                }
            }catch (IOException ioException){
                ioException.printStackTrace();
            }
            catch (SQLException sqlException){
                sqlException.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        Crawler crawler = new Crawler();
        crawler.getPageTextAndLinks("https://www.javatpoint.com/",0);
        // store the webpages
    }
}