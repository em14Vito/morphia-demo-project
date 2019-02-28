package com.github.em14vito.repository.mongodb.config;

import com.github.em14vito.repository.mongodb.dataobject.TestDO;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;
import xyz.morphia.Datastore;
import xyz.morphia.Morphia;
import xyz.morphia.annotations.Embedded;
import xyz.morphia.annotations.Entity;
import xyz.morphia.mapping.Mapper;
import xyz.morphia.mapping.MapperOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Cathay-ins.com.cn Inc. Copyright (c) 2014-2018 All Rights Reserved.
 *
 * @version 1.0.0 2019/1/7 21:29
 **/

@Configuration
public class MorphiaConfig {


    @Value("${denny.mongodb.host}")
    private String mongoDBHost;

    @Value("${denny.mongodb.port}")
    private int mongoDBPort;

    @Value("${denny.mongodb.dbName}")
    private String mongoDBName;

    @Value("${denny.mongodb.userName}")
    private String mongoUserName;

    @Value("${denny.mongodb.userPasswd}")
    private String mongoUserPasswd;


    private final static String MONGO_ENTITY_PACAKGE = "com.github.em14vito.repository.mongodb.dataobject";

    @Bean
    @Primary
    public Datastore datastore() {
        MapperOptions options = new MapperOptions();
        options.setMapSubPackages(true);
        Mapper mapper = new Mapper(options);
        final Morphia morphia = new Morphia(mapper);
        morphia.mapPackage(MONGO_ENTITY_PACAKGE);

        final Datastore datastore = morphia
                .createDatastore(getMongoClient(), mongoDBName);
        datastore.ensureIndexes();
        System.out.println("********************************** Mongo has mapped TestDO? Result :" + morphia.isMapped(TestDO.class));
        return datastore;
    }

    private MongoClient getMongoClient() {
        MongoClient mongoClient;
        if (StringUtils.isEmpty(mongoUserName) || StringUtils.isEmpty(mongoUserPasswd)) {
            String clientUrl = "mongodb://" + mongoDBHost + ":" + mongoDBPort
                    + "/" + mongoDBName;
            MongoClientURI uri = new MongoClientURI(clientUrl);
            mongoClient = new MongoClient(uri);
        } else {
            List<ServerAddress> seeds = new ArrayList<>();
            seeds.add(new ServerAddress(mongoDBHost, mongoDBPort));
            List<MongoCredential> credentials = new ArrayList<>();
            credentials.add(
                    MongoCredential.createScramSha1Credential(
                            mongoUserName,
                            mongoDBName,
                            mongoUserPasswd.toCharArray()
                    )
            );
            mongoClient = new MongoClient(seeds, credentials);
            ;
        }
        return mongoClient;
    }
}
