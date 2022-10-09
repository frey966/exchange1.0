package redis.clients.jedis.commands.jedis;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.*;

import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;

import redis.clients.jedis.CommandArguments;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisAccessControlException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.resps.AccessControlUser;
import redis.clients.jedis.util.RedisVersionUtil;
import redis.clients.jedis.util.SafeEncoder;

/**
 * TODO: properly define and test exceptions
 */
public class AccessControlListCommandsTest extends JedisCommandsTestBase {

  public static final String USER_NAME = "newuser";
  public static final String USER_PASSWORD = "secret";

  @BeforeClass
  public static void prepare() throws Exception {
    // Use to check if the ACL test should be ran. ACL are available only in 6.0 and later
    org.junit.Assume.assumeTrue("Not running ACL test on this version of Redis",
        RedisVersionUtil.checkRedisMajorVersionNumber(6));
  }

  @Test
  public void aclWhoAmI() {
    String string = jedis.aclWhoAmI();
    assertEquals("default", string);

    byte[] binary = jedis.aclWhoAmIBinary();
    assertArrayEquals(SafeEncoder.encode("default"), binary);
  }

  @Test
  public void aclListDefault() {
    assertTrue(jedis.aclList().size() > 0);
    assertTrue(jedis.aclListBinary().size() > 0);
  }

  @Test
  public void addAndRemoveUser() {
    int existingUsers = jedis.aclList().size();

    String status = jedis.aclSetUser(USER_NAME);
    assertEquals("OK", status);
    assertEquals(existingUsers + 1, jedis.aclList().size());
    assertEquals(existingUsers + 1, jedis.aclListBinary().size()); // test binary

    jedis.aclDelUser(USER_NAME);
    assertEquals(existingUsers, jedis.aclList().size());
    assertEquals(existingUsers, jedis.aclListBinary().size()); // test binary
  }

  @Test
  public void aclUsers() {
    List<String> users = jedis.aclUsers();
    assertEquals(2, users.size());
    assertTrue(users.contains("default"));

    assertEquals(2, jedis.aclUsersBinary().size()); // Test binary
  }

  @Test
  public void aclGetUser() {
    // get default user information
    AccessControlUser userInfo = jedis.aclGetUser("default");

    assertThat(userInfo.getFlags().size(), greaterThanOrEqualTo(1));
    assertEquals(1, userInfo.getPassword().size());
    assertEquals("+@all", userInfo.getCommands());
    assertEquals("~*", userInfo.getKeys().get(0));

    // create new user
    jedis.aclDelUser(USER_NAME);
    jedis.aclSetUser(USER_NAME);
    userInfo = jedis.aclGetUser(USER_NAME);
    assertThat(userInfo.getFlags().size(), greaterThanOrEqualTo(1));
    assertEquals("off", userInfo.getFlags().get(0));
    assertTrue(userInfo.getPassword().isEmpty());
    assertTrue(userInfo.getKeys().isEmpty());

    // reset user
    jedis.aclSetUser(USER_NAME, "reset", "+@all", "~*", "-@string", "+incr", "-debug",
      "+debug|digest");
    userInfo = jedis.aclGetUser(USER_NAME);
    assertThat(userInfo.getCommands(), containsString("+@all"));
    assertThat(userInfo.getCommands(), containsString("-@string"));
    assertThat(userInfo.getCommands(), containsString("+debug|digest"));

    jedis.aclDelUser(USER_NAME);
  }

  @Test
  public void createUserAndPasswords() {
    String status = jedis.aclSetUser(USER_NAME, ">" + USER_PASSWORD);
    assertEquals("OK", status);

    // create a new client to try to authenticate
    Jedis jedis2 = new Jedis();
    String authResult = null;

    // the user is just created without any permission the authentication should fail
    try {
      authResult = jedis2.auth(USER_NAME, USER_PASSWORD);
      fail("Should throw a WRONGPASS exception");
    } catch (JedisAccessControlException e) {
      assertNull(authResult);
      assertTrue(e.getMessage().startsWith("WRONGPASS "));
    }

    // now activate the user
    authResult = jedis.aclSetUser(USER_NAME, "on", "+acl");
    jedis2.auth(USER_NAME, USER_PASSWORD);
    assertEquals(USER_NAME, jedis2.aclWhoAmI());

    // test invalid password
    jedis2.close();

    try {
      authResult = jedis2.auth(USER_NAME, "wrong-password");
      fail("Should throw a WRONGPASS exception");
    } catch (JedisAccessControlException e) {
      assertEquals("OK", authResult);
      assertTrue(e.getMessage().startsWith("WRONGPASS "));
    }

    // remove password, and try to authenticate
    status = jedis.aclSetUser(USER_NAME, "<" + USER_PASSWORD);
    try {
      authResult = jedis2.auth(USER_NAME, USER_PASSWORD);
      fail("Should throw a WRONGPASS exception");
    } catch (JedisAccessControlException e) {
      assertEquals("OK", authResult);
      assertTrue(e.getMessage().startsWith("WRONGPASS "));
    }

    jedis.aclDelUser(USER_NAME); // delete the user
    try {
      authResult = jedis2.auth(USER_NAME, "wrong-password");
      fail("Should throw a WRONGPASS exception");
    } catch (JedisAccessControlException e) {
      assertEquals("OK", authResult);
      assertTrue(e.getMessage().startsWith("WRONGPASS "));
    }

    jedis2.close();
  }

  @Test
  public void aclSetUserWithAnyPassword() {
    jedis.aclDelUser(USER_NAME);
    String status = jedis.aclSetUser(USER_NAME, "nopass");
    assertEquals("OK", status);
    status = jedis.aclSetUser(USER_NAME, "on", "+acl");
    assertEquals("OK", status);

    // connect with this new user and try to get/set keys
    Jedis jedis2 = new Jedis();
    String authResult = jedis2.auth(USER_NAME, "any password");
    assertEquals("OK", authResult);

    jedis2.close();
    jedis.aclDelUser(USER_NAME);
  }

  @Test
  public void aclExcudeSingleCommand() {
    jedis.aclDelUser(USER_NAME);
    String status = jedis.aclSetUser(USER_NAME, "nopass");
    assertEquals("OK", status);

    status = jedis.aclSetUser(USER_NAME, "on", "+acl");
    assertEquals("OK", status);

    status = jedis.aclSetUser(USER_NAME, "allcommands", "allkeys");
    assertEquals("OK", status);

    status = jedis.aclSetUser(USER_NAME, "-ping");
    assertEquals("OK", status);

    // connect with this new user and try to get/set keys
    Jedis jedis2 = new Jedis();
    String authResult = jedis2.auth(USER_NAME, "any password");
    assertEquals("OK", authResult);

    jedis2.incr("mycounter");

    String result = null;
    try {
      result = jedis2.ping();
      fail("Should throw a NOPERM exception");
    } catch (JedisAccessControlException e) {
      assertNull(result);
      assertEquals(
        "NOPERM this user has no permissions to run the 'ping' command",
        e.getMessage());
    }

    jedis2.close();
    jedis.aclDelUser(USER_NAME);
  }

  @Test
  public void aclDryRun() {
    jedis.aclDelUser(USER_NAME);

    jedis.aclSetUser(USER_NAME, "nopass", "allkeys", "+set", "-get");

    assertEquals("OK", jedis.aclDryRun(USER_NAME, "SET", "key", "value"));
    assertEquals("This user has no permissions to run the 'get' command",
        jedis.aclDryRun(USER_NAME, "GET", "key"));

    assertEquals("OK", jedis.aclDryRun(USER_NAME,
        new CommandArguments(Protocol.Command.SET).key("key").add("value")));
    assertEquals("This user has no permissions to run the 'get' command", jedis.aclDryRun(USER_NAME,
        new CommandArguments(Protocol.Command.GET).key("key")));

    jedis.aclDelUser(USER_NAME);
  }

  @Test
  public void aclDryRunBinary() {
    byte[] username = USER_NAME.getBytes();
    jedis.aclDelUser(username);

    jedis.aclSetUser(username, "nopass".getBytes(), "allkeys".getBytes(), "+set".getBytes(), "-get".getBytes());

    assertArrayEquals("OK".getBytes(), jedis.aclDryRunBinary(username, "SET".getBytes(), "key".getBytes(), "value".getBytes()));
    assertArrayEquals("This user has no permissions to run the 'get' command".getBytes(),
        jedis.aclDryRunBinary(username, "GET".getBytes(), "key".getBytes()));

    assertArrayEquals("OK".getBytes(), jedis.aclDryRunBinary(username, new CommandArguments(Protocol.Command.SET).key("key").add("value")));
    assertArrayEquals("This user has no permissions to run the 'get' command".getBytes(),
        jedis.aclDryRunBinary(username, new CommandArguments(Protocol.Command.GET).key("key")));

    jedis.aclDelUser(USER_NAME);
  }

  @Test
  public void aclDelUser() {
    String statusSetUser = jedis.aclSetUser(USER_NAME);
    assertEquals("OK", statusSetUser);
    int before = jedis.aclList().size();
    assertEquals(1L, jedis.aclDelUser(USER_NAME));
    int after = jedis.aclList().size();
    assertEquals(before - 1, after);
  }

  @Test
  public void basicPermissionsTest() {

    // create a user with login permissions

    jedis.aclDelUser(USER_NAME); // delete the user

    // users are not able to access any command
    String status = jedis.aclSetUser(USER_NAME, ">" + USER_PASSWORD);
    String authResult = jedis.aclSetUser(USER_NAME, "on", "+acl");

    // connect with this new user and try to get/set keys
    Jedis jedis2 = new Jedis();
    jedis2.auth(USER_NAME, USER_PASSWORD);

    String result = null;
    try {
      result = jedis2.set("foo", "bar");
      fail("Should throw a NOPERM exception");
    } catch (JedisAccessControlException e) {
      assertNull(result);
      assertEquals(
        "NOPERM this user has no permissions to run the 'set' command",
        e.getMessage());
    }

    // change permissions of the user
    // by default users are not able to access any key
    status = jedis.aclSetUser(USER_NAME, "+set");

    jedis2.close();
    jedis2.auth(USER_NAME, USER_PASSWORD);

    result = null;
    try {
      result = jedis2.set("foo", "bar");
      fail("Should throw a NOPERM exception");
    } catch (JedisAccessControlException e) {
      assertNull(result);
      assertEquals(
        "NOPERM this user has no permissions to access one of the keys used as arguments",
        e.getMessage());
    }

    // allow user to access a subset of the key
    result = jedis.aclSetUser(USER_NAME, "allcommands", "~foo:*", "~bar:*"); // TODO : Define a DSL

    // create key foo, bar and zap
    result = jedis2.set("foo:1", "a");
    assertEquals("OK", status);

    result = jedis2.set("bar:2", "b");
    assertEquals("OK", status);

    result = null;
    try {
      result = jedis2.set("zap:3", "c");
      fail("Should throw a NOPERM exception");
    } catch (JedisAccessControlException e) {
      assertNull(result);
      assertEquals(
        "NOPERM this user has no permissions to access one of the keys used as arguments",
        e.getMessage());
    }

    // remove user
    jedis.aclDelUser(USER_NAME); // delete the user

  }

  @Test
  public void aclCatTest() {
    List<String> categories = jedis.aclCat();
    assertTrue(!categories.isEmpty());

    // test binary
    List<byte[]> categoriesBinary = jedis.aclCatBinary();
    assertTrue(!categories.isEmpty());
    assertEquals(categories.size(), categoriesBinary.size());

    // test commands in a category
    assertTrue(!jedis.aclCat("scripting").isEmpty());

    try {
      jedis.aclCat("testcategory");
      fail("Should throw a ERR exception");
    } catch (Exception e) {
      assertEquals("ERR Unknown category 'testcategory'", e.getMessage());
    }
  }

  @Test
  public void aclLogTest() {
    jedis.aclLogReset();
    assertTrue(jedis.aclLog().isEmpty());

    // create new user and cconnect
    jedis.aclSetUser("antirez", ">foo", "on", "+set", "~object:1234");
    jedis.aclSetUser("antirez", "+eval", "+multi", "+exec");
    jedis.auth("antirez", "foo");

    // generate an error (antirez user does not have the permission to access foo)
    try {
      jedis.get("foo");
      fail("Should have thrown an JedisAccessControlException: user does not have the permission to get(\"foo\")");
    } catch (JedisAccessControlException e) {
    }

    // test the ACL Log
    jedis.auth("default", "foobared");

    assertEquals("Number of log messages ", 1, jedis.aclLog().size());
    assertEquals(1, jedis.aclLog().get(0).getCount());
    assertEquals("antirez", jedis.aclLog().get(0).getUsername());
    assertEquals("toplevel", jedis.aclLog().get(0).getContext());
    assertEquals("command", jedis.aclLog().get(0).getReason());
    assertEquals("get", jedis.aclLog().get(0).getObject());

    // Capture similar event
    jedis.aclLogReset();
    assertTrue(jedis.aclLog().isEmpty());

    jedis.auth("antirez", "foo");

    for (int i = 0; i < 10; i++) {
      // generate an error (antirez user does not have the permission to access foo)
      try {
        jedis.get("foo");
        fail("Should have thrown an JedisAccessControlException: user does not have the permission to get(\"foo\")");
      } catch (JedisAccessControlException e) {
      }
    }

    // test the ACL Log
    jedis.auth("default", "foobared");
    assertEquals("Number of log messages ", 1, jedis.aclLog().size());
    assertEquals(10, jedis.aclLog().get(0).getCount());
    assertEquals("get", jedis.aclLog().get(0).getObject());

    // Generate another type of error
    jedis.auth("antirez", "foo");
    try {
      jedis.set("somekeynotallowed", "1234");
      fail("Should have thrown an JedisAccessControlException: user does not have the permission to set(\"somekeynotallowed\", \"1234\")");
    } catch (JedisAccessControlException e) {
    }

    // test the ACL Log
    jedis.auth("default", "foobared");
    assertEquals("Number of log messages ", 2, jedis.aclLog().size());
    assertEquals(1, jedis.aclLog().get(0).getCount());
    assertEquals("somekeynotallowed", jedis.aclLog().get(0).getObject());
    assertEquals("key", jedis.aclLog().get(0).getReason());

    jedis.aclLogReset();
    assertTrue(jedis.aclLog().isEmpty());

    jedis.auth("antirez", "foo");
    Transaction t = jedis.multi();
    t.incr("foo");
    try {
      t.exec();
      fail("Should have thrown an JedisAccessControlException: user does not have the permission to incr(\"foo\")");
    } catch (Exception e) {
    }
    t.close();

    jedis.auth("default", "foobared");
    assertEquals("Number of log messages ", 1, jedis.aclLog().size());
    assertEquals(1, jedis.aclLog().get(0).getCount());
    assertEquals("multi", jedis.aclLog().get(0).getContext());
    assertEquals("incr", jedis.aclLog().get(0).getObject());

    // ACL LOG can accept a numerical argument to show less entries
    jedis.auth("antirez", "foo");
    for (int i = 0; i < 5; i++) {
      try {
        jedis.incr("foo");
        fail("Should have thrown an JedisAccessControlException: user does not have the permission to incr(\"foo\")");
      } catch (JedisAccessControlException e) {
      }
    }
    try {
      jedis.set("foo-2", "bar");
      fail("Should have thrown an JedisAccessControlException: user does not have the permission to set(\"foo-2\", \"bar\")");
    } catch (JedisAccessControlException e) {
    }

    jedis.auth("default", "foobared");
    assertEquals("Number of log messages ", 3, jedis.aclLog().size());
    assertEquals("Number of log messages ", 2, jedis.aclLog(2).size());

    // Binary tests
    assertEquals("Number of log messages ", 3, jedis.aclLogBinary().size());
    assertEquals("Number of log messages ", 2, jedis.aclLogBinary(2).size());

    // RESET
    String status = jedis.aclLogReset();
    assertEquals(status, "OK");

    jedis.aclDelUser("antirez");
  }

  @Test
  public void aclGenPass() {
    assertNotNull(jedis.aclGenPass());

    // bit length case
    assertNotNull(jedis.aclGenPassBinary(16));
    assertNotNull(jedis.aclGenPassBinary(32));
  }

  @Test
  public void aclGenPassBinary() {
    assertNotNull(jedis.aclGenPassBinary());

    // bit length case
    assertNotNull(jedis.aclGenPassBinary(16));
    assertNotNull(jedis.aclGenPassBinary(32));
  }

  @Test
  public void aclBinaryCommandsTest() {
    jedis.aclSetUser(USER_NAME.getBytes());
    assertNotNull(jedis.aclGetUser(USER_NAME));

    assertEquals(1L, jedis.aclDelUser(USER_NAME.getBytes()));

    jedis.aclSetUser(USER_NAME.getBytes(), "reset".getBytes(), "+@all".getBytes(), "~*".getBytes(),
      "-@string".getBytes(), "+incr".getBytes(), "-debug".getBytes(), "+debug|digest".getBytes(),
            "resetchannels".getBytes(), "&testchannel:*".getBytes());

    AccessControlUser userInfo = jedis.aclGetUser(USER_NAME.getBytes());

    assertThat(userInfo.getCommands(), containsString("+@all"));
    assertThat(userInfo.getCommands(), containsString("-@string"));
    assertThat(userInfo.getCommands(), containsString("+debug|digest"));
    assertEquals("&testchannel:*", userInfo.getChannels().get(0));

    jedis.aclDelUser(USER_NAME.getBytes());

    jedis.aclSetUser("TEST_USER".getBytes());
    jedis.aclSetUser("ANOTHER_TEST_USER".getBytes());
    jedis.aclSetUser("MORE_TEST_USERS".getBytes());
    assertEquals(3L, jedis.aclDelUser(
            "TEST_USER".getBytes(),
            "ANOTHER_TEST_USER".getBytes(),
            "MORE_TEST_USERS".getBytes()));
  }

  @Test
  public void aclLoadTest() {
    try {
      jedis.aclLoad();
      fail("Should throw a JedisDataException: ERR This Redis instance is not configured to use an ACL file...");
    } catch (JedisDataException e) {
      assertTrue(e.getMessage().contains("ERR This Redis instance is not configured to use an ACL file."));
    }

    // TODO test with ACL file
  }

  @Test
  public void aclSaveTest() {
    try {
      jedis.aclSave();
      fail("Should throw a JedisDataException: ERR This Redis instance is not configured to use an ACL file...");
    } catch (JedisDataException e) {
      assertTrue(e.getMessage().contains("ERR This Redis instance is not configured to use an ACL file."));
    }

    // TODO test with ACL file
  }
}
