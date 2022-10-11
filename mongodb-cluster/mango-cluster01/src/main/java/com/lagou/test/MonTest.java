package com.lagou.test;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MonTest {


    public static void main(String[] args) {
        mongodbfind2();
    }

    public static void mongodbfind2() {
        ServerAddress sa = new ServerAddress("192.168.0.201", 40000);
        List<MongoCredential> mongoCredentialList = new ArrayList<MongoCredential>();
        mongoCredentialList.add(MongoCredential.createCredential("test", "test", "123456".toCharArray()));
        MongoClient client = new MongoClient(sa, mongoCredentialList);
        MongoDatabase database = client.getDatabase("test");        // ********
        MongoCollection collection2 = database.getCollection("user");
        FindIterable<Document> findIterable = collection2.find();
        MongoCursor<Document> cursor2 = findIterable.iterator();
        while (cursor2.hasNext()) {
            Document doc = cursor2.next();
            System.out.println(doc.get("_id") + "," + doc.get("name") + "," + doc.get("age"));
        }
        // 关闭数据库连接
        client.close();
    }

}
