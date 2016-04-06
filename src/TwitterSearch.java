/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import twitter4j.*;
import twitter4j.conf.*;
import java.util.List;

/**
 *
 * @author barbora
 */
public class TwitterSearch {
    
    public static Twitter configure(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey("veigydv1CdeTDWjpezQCKsLS6")
            .setOAuthConsumerSecret("LFmx23fl7qarJKn1OXF7HMOWI1PRWDa0UqXNfKM0HJPpogJ7jr")
            .setOAuthAccessToken("714871528022794241-WM3eiUHp2v2ZcWgbRIFZnU1gRwvHgj8")
            .setOAuthAccessTokenSecret("BtadIoKds0e9Z8IxS6cXHp6vpJMilBttVhpzJmpXxbCKH");
        return new TwitterFactory(cb.build()).getInstance();
    }
    public static String getTweets(String keywords, int amount){
        Twitter twitter = configure();
        try {
            Query query = new Query(keywords);
            QueryResult result;
            query.count(amount);
            String tweetsText = "";
            do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                for (Status tweet : tweets) {
                    tweetsText += tweet.getText();
                }
            } while ((query = result.nextQuery()) != null);
           return tweetsText;
        } catch (Exception te) {
            te.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) {
        String key = "Obama";
        int amount = 10;
        String t = TwitterSearch.getTweets(key, amount);
        int s = SentimentAnalyser.analyse(t);
        System.out.println("Key Word: "+ key );
        System.out.println("Amount: "+ amount );
        System.out.println("Tweets sentiment score: "+ s);
    }
}
