package com.stedu.mall.common.config;

import com.stedu.mall.common.bean.Rule;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "white-list")
public class WhiteListConfig {
    private List<Rule> rules;
}
