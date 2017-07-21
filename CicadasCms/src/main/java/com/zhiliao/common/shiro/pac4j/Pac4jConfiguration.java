package com.zhiliao.common.shiro.pac4j;

import org.pac4j.cas.client.CasClient;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.cas.config.CasProtocol;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.logout.CasLogoutActionBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:cas config
 *
 * @author Jin
 * @create 2017-06-17
 **/
@Configuration
public class Pac4jConfiguration {

    @Value("${sso.client.serverName}")
    private String clientServerName;

    @Value("${sso.client.localLoginUrl}")
    private String localLoginUrl;

    @Value("${sso.server.url}")
    private String serverUrl;

    @Bean
    public Clients casClient(CasConfiguration casConfiguration){
        Clients clients = new Clients();
        CasClient client= new CasClient(casConfiguration);
        client.setLogoutActionBuilder(new CasLogoutActionBuilder(serverUrl+"/logins.jsp","url"));
        clients.setClients(client);
        clients.setCallbackUrl(localLoginUrl);
        return clients;
    }


    @Bean
    public CasConfiguration casConfig(){
        CasConfiguration config = new CasConfiguration();
        config.setLoginUrl(serverUrl+"/login.jsp");
        config.setPrefixUrl(serverUrl);
        config.setGateway(false);
        config.setRenew(false);
        config.setEncoding("GBK");
        config.setProtocol(CasProtocol.CAS20);
        config.setTimeTolerance(100);
        config.setLogoutHandler(new ShiroCasLogoutHandler());
        return config;
    }



    @Bean("config")
    public Config pac4jConfig(Clients clients){
        Config config = new Config();
        config.setClients(clients);
        CustomAuthorizer customAuthorizer = new CustomAuthorizer();
        config.setAuthorizer(customAuthorizer);
        return config;
    }
}
