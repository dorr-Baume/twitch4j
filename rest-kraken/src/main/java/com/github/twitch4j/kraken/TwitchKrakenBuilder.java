package com.github.twitch4j.kraken;

import com.github.twitch4j.common.builder.TwitchAPIBuilder;
import com.github.twitch4j.common.feign.interceptor.TwitchClientIdInterceptor;
import com.netflix.config.ConfigurationManager;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.hystrix.HystrixFeign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TwitchKrakenBuilder extends TwitchAPIBuilder<TwitchKrakenBuilder> {

    /**
     * BaseUrl
     */
    private String baseUrl = "https://api.twitch.tv/kraken";

    /**
     * Default Timeout
     */
    @Wither
    private Integer timeout = 5000;

    /**
     * Initialize the builder
     *
     * @return Twitch Kraken Builder
     */
    public static TwitchKrakenBuilder builder() {
        return new TwitchKrakenBuilder();
    }

    /**
     * Twitch API Client (Kraken)
     *
     * @return TwitchKraken
     */
    public TwitchKraken build() {
        log.debug("Kraken: Initializing Module ...");
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", 2500);
        TwitchKraken client = HystrixFeign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .logger(new Logger.ErrorLogger())
            .errorDecoder(new TwitchKrakenErrorDecoder(new JacksonDecoder()))
            .logLevel(Logger.Level.BASIC)
            .requestInterceptor(new TwitchClientIdInterceptor(this))
            .options(new Request.Options(timeout / 3, timeout))
            .retryer(new Retryer.Default(500, timeout, 2))
            .target(TwitchKraken.class, baseUrl);

        // register with serviceMediator
        getEventManager().getServiceMediator().addService("twitch4j-kraken", client);

        return client;
    }
}
