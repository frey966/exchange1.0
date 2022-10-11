package com.lagou.test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
public class ShardingConnectTest {
    //参考 https://help.aliyun.com/document_detail/411636.html?spm=a2c4g.11186623.0.0.51a32861lx9yJs
    //mongodb://exchange_dev1:exchange_dev321@54.169.76.123:25786/exchange_dev
    public static ServerAddress seed1 = new ServerAddress("54.169.76.123", 25786);
    //public static ServerAddress seed2 = new ServerAddress("11.238.XX.XX", 3717);
    public static String username = "exchange_dev1";
    public static String password = "exchange_dev321";
    //public static String ReplSetName = "mgset-**********";
    public static String DEFAULT_DB = "admin";
    public static String DEMO_DB = "exchange_dev";
    public static String DEMO_COLL = "chuangzhang01";

    public static MongoClient createMongoDBClient() {
        // 构建Seed列表
        List<ServerAddress> seedList = new ArrayList<ServerAddress>();
        seedList.add(seed1);
        //seedList.add(seed2);
        // 构建鉴权信息
        List<MongoCredential> credentials = new ArrayList<MongoCredential>();
        credentials.add(MongoCredential.createScramSha1Credential(username, DEFAULT_DB, password.toCharArray()));
        // 构建操作选项，requiredReplicaSetName属性外的选项根据自己的实际需求配置，默认参数满足大多数场景
        MongoClientOptions options = MongoClientOptions.builder().socketTimeout(2000).connectionsPerHost(1).build();
        return new MongoClient(seedList, credentials, options);
    }

    public static MongoClient createMongoDBClientWithURI() {
        // 另一种通过URI初始化
        // mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]
        // MongoClientURI connectionString = new MongoClientURI("mongodb://" + username + ":" + password + "@" + seed1 + "," + seed2 + "/" + DEFAULT_DB);
        MongoClientURI connectionString = new MongoClientURI("mongodb://" + username + ":" + password + "@" + seed1 + ","  + "/" + DEMO_DB);
        return new MongoClient(connectionString);
    }
    public static void main(String args[]) {
        MongoClient client = createMongoDBClientWithURI();
        // or
        // MongoClient client = createMongoDBClientWithURI();
        try {
            // 取得Collection句柄
            MongoDatabase database = client.getDatabase(DEMO_DB);
            MongoCollection<Document> collection = database.getCollection(DEMO_COLL);
            // 插入数据
            Document doc = new Document();
            String demoname = "JAVA:" + UUID.randomUUID();
            doc.append("DEMO", demoname);
            doc.append("MESG", "Hello AliCoudDB For MongoDB");
            //collection.insertOne(doc);
            System.out.println("insert document: " + doc);
            // 读取数据
            BsonDocument filter = new BsonDocument();
            filter.append("DEMO", new BsonString(demoname));
            MongoCursor<Document> cursor = collection.find(filter).iterator();
            while (cursor.hasNext()) {
                System.out.println("find document: " + cursor.next());
            }
        } finally {
            // 关闭Client，释放资源
            client.close();
        }
        return;
    }
}

