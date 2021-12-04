package com.sroka.grouptripsorganizer.configuration.html_sanitizer;

import org.owasp.html.ElementPolicy;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PolicyFactoryConfig {

    @Bean
    public PolicyFactory policyFactory() {
        return new HtmlPolicyBuilder()
                .allowElements("a", "p", "b", "strong", "em", "u", "s", "span", "br", "ol", "li", "ul", "div",
                        "h1", "h2", "h3", "h4", "h5", "h6")
                .allowUrlProtocols("https")
                .allowAttributes("href", "style", "data-checked"
                ).onElements("a", "p", "b", "strong", "em", "u", "s", "span", "br", "ol", "li", "ul", "div",
                        "h1", "h2", "h3", "h4", "h5", "h6")
                .requireRelNofollowOnLinks()
                .toFactory();
    }
}
