package com.microservices.demo.twitter.to.kafka.service.runner;



import com.microservices.demo.config.TwitterToKafkaServiceConfigData;
import com.microservices.demo.twitter.to.kafka.service.exception.TwitterToKafkaServiceException;
import com.microservices.demo.twitter.to.kafka.service.listener.TwitterKafkaStatusListener;
import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

@Slf4j
@Component
@ConditionalOnProperty(name = "twitter-to-kafka-service.enable-mock-tweets", havingValue = "true")
public class MockKafkaStreamRunner implements StreamRunner {

    private final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;
    private final TwitterKafkaStatusListener twitterKafkaStatusListener;

    private static SecureRandom RANDOM = new SecureRandom();

    private static final String[] WORDS = new String[]{
            "car",
            "box",
            "slide",
            "top",
            "award",
            "greed",
            "simultaneously",
            "dark",
            "throw",
            "bubble",
            "ask",
            "physics",
            "alternate",
            "adjacent",
            "funny",
            "thongs",
            "lap-top"
    };

    private static final String tweetAsJson = "{" +
            "\"created_at\":\"{0}\"," +
            "\"id\":\"{1}\"," +
            "\"text\":\"{2}\"," +
            "\"user\":{\"id\":\"{3}\"}" +
            "}";
    private static final String TWITTER_STATUS_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";

    public MockKafkaStreamRunner(TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData,
            TwitterKafkaStatusListener twitterKafkaStatusListener) {
        this.twitterToKafkaServiceConfigData = twitterToKafkaServiceConfigData;
        this.twitterKafkaStatusListener = twitterKafkaStatusListener;
    }


    @Override
    public void start() {
        log.info("start");
        String[] keywords = twitterToKafkaServiceConfigData.getTwitterKeywords().toArray(new String[0]);
        int mockMinTweetLength = twitterToKafkaServiceConfigData.getMockMinTweetLength();
        int mockMaxTweetLength = twitterToKafkaServiceConfigData.getMockMaxTweetLength();
        long mockSleepMs = twitterToKafkaServiceConfigData.getMockSleepMs();
        log.info("Starting mock filtering twitter stream for keywords {}", Arrays.toString(keywords));

        simulateTwitterStream(keywords, mockMinTweetLength, mockMaxTweetLength, mockSleepMs);

    }

    private void simulateTwitterStream(String[] keywords, int mockMinTweetLength, int mockMaxTweetLength,
            long mockSleepMs) {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                while (true) {
                    String formattedTweetAsRawJson = getFormattedTweet(keywords, mockMinTweetLength,
                            mockMaxTweetLength);
                    log.info("message: {}", formattedTweetAsRawJson);
                    Status status = TwitterObjectFactory.createStatus(formattedTweetAsRawJson);
                    twitterKafkaStatusListener.onStatus(status);
                    sleep(mockSleepMs);
                }
            } catch (TwitterException twitterException) {
                log.error("Error creating status", twitterException);
            }
        });

    }

    private void sleep(long milliseconds) {
        log.info("sleep");
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new TwitterToKafkaServiceException(
                    "Interrupted while sleeping for waiting to publish a new status!!!");
        }
    }

    private String getFormattedTweet(String[] keywords, int minLength, int maxLength) {

        String[] params = new String[]{
                ZonedDateTime.now().format(DateTimeFormatter.ofPattern(TWITTER_STATUS_DATE_FORMAT, Locale.ENGLISH)),
                String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE)),
                getRandomTweetContext(keywords, minLength, maxLength),
                String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE))
        };
        return formatTweetAsJsonWithParams(params);
    }

    private static String formatTweetAsJsonWithParams(String[] params) {
        String tweet = tweetAsJson;
        for (int i = 0; i < params.length; i++) {
            tweet = tweet.replace("{" + i + "}", params[i]);
        }
        return tweet;
    }

    private String getRandomTweetContext(String[] keywords, int minLength, int maxLength) {
        StringBuilder tweet = new StringBuilder();
        int tweetLength = RANDOM.nextInt(maxLength - minLength + 1) + minLength;
        return constructRandomTweet(keywords, tweet, tweetLength);
    }

    private static String constructRandomTweet(String[] keywords, StringBuilder tweet, int tweetLength) {
        for (int i = 0; i < tweetLength; i++) {
            tweet.append(WORDS[RANDOM.nextInt(WORDS.length)]).append(" ");
            if (i == tweetLength / 2) {
                tweet.append(keywords[RANDOM.nextInt(keywords.length)]).append(" ");
            }
        }
        return tweet.toString().trim();
    }
}
